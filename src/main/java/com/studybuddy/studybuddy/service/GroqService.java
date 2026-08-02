package com.studybuddy.studybuddy.service;

import com.studybuddy.studybuddy.dto.FlashcardAIItem;
import com.studybuddy.studybuddy.dto.GroqRequest;
import com.studybuddy.studybuddy.dto.GroqResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

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
        String jsonBody=objectMapper.writeValueAsString(requestBody); //java ovjekat pretvara u json

        String responseV1=restClient.post()//sa;ji zahtjev
                .uri(url)//na ovu adresu
                .header("Content-Type", "application/json")//tijelo zahtjeva json format
                .header("Authorization", "Bearer " + apiKey)
                .body(jsonBody)
                .retrieve()//posalji zahtjev
                .body(String.class);//vrati odgovor kao obicni string

        GroqResponse response=objectMapper.readValue(responseV1,GroqResponse.class); //json string pretvara u java objekat prema formatu groqresponse klase


        return response.getChoices().get(0).getMessage().getContent();
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
}
