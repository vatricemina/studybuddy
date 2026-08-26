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
        Generiši tačno 5 flashcards (pitanje-odgovor parova) za temu "%s".
        Odgovori validnim JSON nizom, bez ikakvog dodatnog teksta prije ili poslije, u tačno ovom formatu:
        [
          {"question": "pitanje ovdje", "answer": "odgovor ovdje"},
          {"question": "pitanje ovdje", "answer": "odgovor ovdje"}
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
        Generiši tačno 5 pitanja sa po 4 ponuđena odgovora (multiple choice) za temu "%s".
        
        VAŽNO PRAVILO za polje "correctAnswer": ono MORA sadržavati SAMO jedno od sledeća četiri slova: "A", "B", "C" ili "D" -ništa drugo, nikad puni tekst odgovora, uvijek tačno jedno veliko slovo koje odgovara tačnoj opciji.
        
        Primjer ispravnog odgovora: "correctAnswer": "C"
        Primjer POGREŠNOG odgovora: "correctAnswer": "Integral funkcije" (ovo NIKAD ne radi ovako)
        
        Odgovori ISKLJUČIVO validnim JSON nizom, bez ikakvog dodatnog teksta prije ili poslije, u tačno ovom formatu:
        [
          {
            "questionText": "tekst pitanja ovdje",
            "optionA": "prva opcija",
            "optionB": "druga opcija",
            "optionC": "treca opcija",
            "optionD": "cetvrta opcija",
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
                ? "Ti si AI pomoćnik za učenje. Trenutna tema: " + topicTitle + ". Odgovaraj kratko, jasno, i na bosanskom jeziku."
                : "Ti si AI pomoćnik za učenje. Odgovaraj kratko, jasno, i na bosanskom jeziku.";
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
        Predmet: %s
        Ispit je za %d dana (datum ispita: %s).
        Danasnji datum: %s
        
        Teme koje treba obraditi (sa studentovom ličnom procjenom koliko sati mu treba):
        %s
        
        Napravi PAMETAN raspored učenja, od danas do dana ispita (ne uključujući sam dan ispita).
        
        VAŽNO - KOLIKO ČESTO UČITI:
        - Ako je ispit BLIZU (manje od 10 dana) - planiraj učenje SVAKI dan, jer nema vremena za pauze.
        - Ako je ispit DALEKO (10 ili više dana) - NE moraš planirati svaki dan. Rasporedi dane učenja PAMETNO kroz preostalo vrijeme, sa danima pauze između, tako da ukupan broj dana učenja bude razuman (npr. za 30 dana do ispita, možda 12-15 dana stvarnog učenja je dovoljno, ostalo su pauze ili slobodni dani).
        - Cilj je REALAN plan koji student stvarno može ispratiti, ne prenatrpan raspored.
        
        Ostala pravila:
        1. NE radi sve sate jedne teme uzastopno - MIKSAJ teme kroz vrijeme
        2. Poštuj studentovu procjenu ukupnih sati po temi
        3. Teže/opsežnije teme rasporedi RANIJE
        4. Zadnjih par dana PRED SAM ISPIT planiraj kao PONAVLJANJE svih tema
        5. NIKAD ne stavljaj "hours": 0 - ako neki dan nije za učenje, jednostavno ga NE UKLJUČUJ u listu (preskoči ga)
        6. Za svaki dan koji uključiš, dodaj kratak, KONKRETAN "focus" savjet - nikad ne ponavljaj isti tekst
        
        Odgovori ISKLJUČIVO validnim JSON nizom (samo dani KADA se stvarno uči, ne svi dani do ispita), bez ikakvog dodatnog teksta, u tačno ovom formatu:
        [
          {"date": "2026-08-12", "topicTitle": "tacan naziv teme iz liste iznad", "hours": 2, "focus": "kratak savjet sta raditi tog dana"}
        ]
        
        VAŽNO: "date" mora biti u formatu YYYY-MM-DD. "topicTitle" mora biti TAČAN naziv jedne od tema navedenih iznad.
        """.formatted(subjectName, daysUntilExam, examDate, LocalDate.now(), topicsListText.toString());


        String responseV1=generateContent(prompt);

        String response=responseV1
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        return objectMapper.readValue(response, new TypeReference<List<StudyPlanAIItem>>() {});
    }
}
