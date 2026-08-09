package com.studybuddy.studybuddy.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey=Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private final long expirationMs=1000*60*60*24; //24 sata

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()        //gradi citac za jwt token
                .verifyWith(secretKey) //postavlja tajni kljuc za provjeru
                .build() //zavrsava konfig i kreira gotov jwtparser
                .parseSignedClaims(token) //dekodira token, provjerava rok trajanja i potpis
                .getPayload() //dohvata podatke unutar tokena
                .getSubject(); //vraca vrijednost subject polja unutar tokena, ovdje email
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }



}
