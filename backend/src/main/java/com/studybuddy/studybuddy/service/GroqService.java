package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.*;
import com.studybuddy.studybuddy.entity.StudyPlanEntry;
import com.studybuddy.studybuddy.entity.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroqService {
    @Value("${GROQ_API_KEY}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateContent(String prompt){
        String url="https://api.groq.com/openai/v1/chat/completions";
        GroqRequest requestBody=new GroqRequest(prompt);
        String jsonBody=objectMapper.writeValueAsString(requestBody); //java objekat pretvara u json/string

        String responseV1=restClient.post()//sa;ji zahtjev
                .uri(url)//na ovu adresu
                .header("Content-Type", "application/json")//tijelo zahtjeva json format
                .header("Authorization", "Bearer " + apiKey)
                .body(jsonBody)
                .retrieve()//posalji zahtjev
                .body(String.class);//vrati odgovor kao obicni string

        GroqResponse response=objectMapper.readValue(responseV1,GroqResponse.class); //json/string pretvara u java objekat prema formatu groqresponse klase


        return response.getChoices().get(0).getMessage().getContent(); //izvlaci samo ono sto nam treba(tekst) iz odgovora
    }

    public List<FlashcardAIItem> generateFlashcards(String topicTitle){
        String prompt = """
    Generate exactly 5 flashcards (question-answer pairs) for the topic "%s".
    Respond with a valid JSON array, without any additional text before or after, in exactly this format:
    [
      {"question": "question here", "answer": "answer here"},
      {"question": "question here", "answer": "answer here"}
    ]
    """.formatted(topicTitle);

        String responseV1=generateContent(prompt);

        String responseJson=responseV1
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        return objectMapper.readValue(responseJson, new TypeReference<List<FlashcardAIItem>>() {});
    }

    public List<QuizQuestionAIItem> generateQuizQuestions(String topicTitle) throws Exception{
        String prompt = """
    Generate exactly 5 multiple choice questions, each with 4 answer options, for the topic "%s".

    IMPORTANT RULE for the "correctAnswer" field: it MUST contain ONLY one of the following four letters: "A", "B", "C", or "D" - nothing else, never the full text of the answer, always exactly one capital letter matching the correct option.

    Example of a correct answer: "correctAnswer": "C"
    Example of a WRONG answer: "correctAnswer": "Integral of the function" (this NEVER works this way)

    Respond ONLY with a valid JSON array, without any additional text before or after, in exactly this format:
    [
      {
        "questionText": "question text here",
        "optionA": "first option",
        "optionB": "second option",
        "optionC": "third option",
        "optionD": "fourth option",
        "correctAnswer": "A"
      }
    ]
    """.formatted(topicTitle);

        String responseV1=generateContent(prompt);

        String response=responseV1
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        return objectMapper.readValue(response, new TypeReference<List<QuizQuestionAIItem>>() {});
    }

    public String chat(String topicTitle, List<ChatMessageDTO> messageHistory) throws Exception{
        List<GroqRequest.Message> messages=new ArrayList<>();

        String systemPrompt = topicTitle != null
                ? "You are an AI study assistant. Current topic: " + topicTitle + ". Answer concisely, clearly, and in English. For mathematical formulas, use $formula$ for inline math and $$formula$$ for block math, never \\( \\) or \\[ \\]."
                : "You are an AI study assistant. Answer concisely, clearly, and in English. For mathematical formulas, use $formula$ for inline math and $$formula$$ for block math, never \\( \\) or \\[ \\].";
        messages.add(new GroqRequest.Message("system", systemPrompt));

        for(ChatMessageDTO msg:messageHistory){
            messages.add(new GroqRequest.Message(msg.getRole(), msg.getContent()));
        }

        GroqRequest requestBody=new GroqRequest("openai/gpt-oss-120b", messages);
        String jsonBody=objectMapper.writeValueAsString(requestBody);

        String url = "https://api.groq.com/openai/v1/chat/completions";

        String responseV1=restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .body(jsonBody)
                .retrieve()
                .body(String.class);

        GroqResponse response=objectMapper.readValue(responseV1, GroqResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public List<StudyPlanAIItem> generateStudyPlan(String subjectName, LocalDate examDate, List<Topic> topics){
        long daysUntilExam= ChronoUnit.DAYS.between(LocalDate.now(), examDate);
        StringBuilder topicsListText=new StringBuilder();
        for(Topic topic:topics){
            topicsListText.append("- ").append(topic.getTitle()).append(" (procijenjeno ").append(topic.getEstimatedHours()).append(" sati)\n");
        }

        String prompt = """
    Subject: %s
    The exam is in %d days (exam date: %s).
    Today's date: %s

    Topics to cover (with the student's own estimate of how many hours each needs):
    %s

    Create a SMART study schedule, from today until the exam day (not including the exam day itself).

    IMPORTANT - HOW OFTEN TO STUDY:
    - If the exam is CLOSE (less than 10 days) - plan studying EVERY day, since there's no time for breaks.
    - If the exam is FAR (10 or more days) - you DON'T have to plan every day. Space out study days SMARTLY across the remaining time, with break days in between, so the total number of study days stays reasonable (e.g. for 30 days until the exam, maybe 12-15 days of actual studying is enough, the rest are breaks or free days).
    - The goal is a REALISTIC plan the student can actually follow, not an overloaded schedule.

    Other rules:
    1. DON'T do all the hours of one topic in a row - MIX topics across time
    2. Respect the student's estimated total hours per topic
    3. Harder/more extensive topics should be scheduled EARLIER
    4. The last few days RIGHT BEFORE THE EXAM should be planned as a REVIEW of all topics
    5. NEVER put "hours": 0 - if a day isn't for studying, simply DON'T INCLUDE it in the list (skip it)
    6. For every day you include, add a short, CONCRETE "focus" tip - never repeat the same text

    Respond ONLY with a valid JSON array (only the days WHEN studying actually happens, not every day until the exam), without any additional text, in exactly this format:
    [
      {"date": "2026-08-12", "topicTitle": "exact topic name from the list above", "hours": 2, "focus": "short tip on what to do that day"}
    ]

    IMPORTANT: "date" must be in YYYY-MM-DD format. "topicTitle" must be the EXACT name of one of the topics listed above.
    """.formatted(subjectName, daysUntilExam, examDate, LocalDate.now(), topicsListText.toString());

        String responseV1=generateContent(prompt);

        String response=responseV1
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        try {
            return objectMapper.readValue(response, new TypeReference<List<StudyPlanAIItem>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate a valid study plan. Please try again.");
        }    }
}
