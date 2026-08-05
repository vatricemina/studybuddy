package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

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

        String systemPrompt="Ti si AI pomoćnik za učenje. Trenutna tema koju korisnik uči je: "+topicTitle+". Odgovaraj kratko, jasno, i na bosanskom jeziku.";
        messages.add(new GroqRequest.Message("system", systemPrompt));

        for(ChatMessageDTO msg:messageHistory){
            messages.add(new GroqRequest.Message(msg.getRole(), msg.getContent()));
        }

        GroqRequest requestBody=new GroqRequest("llama-3.3-70b-versatile", messages);
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
}
