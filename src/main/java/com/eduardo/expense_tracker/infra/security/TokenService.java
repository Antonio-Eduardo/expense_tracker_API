package com.eduardo.expense_tracker.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.expense_tracker.entities.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private String secret;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(User user) {
        System.out.println(">>>>> SECRET NO GENERATE: [" + secret + "]");
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("expense-tracker-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpirationTime())
                    .sign((algorithm));
            return token;
        }catch (JWTCreationException e){
            throw new RuntimeException("Error generating token", e);
        }
    }
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String email = JWT.require(algorithm)
                    .withIssuer("expense-tracker-api")
                    .build()
                    .verify(token)
                    .getSubject();
            System.out.println(">>>>> EMAIL EXTRAÍDO: [" + email + "]");
            return email;
        } catch (JWTVerificationException e) {
            System.out.println(">>>>> ERRO NA VERIFICAÇÃO: " + e.getMessage());
            return "";
        }
    }
    private Instant getExpirationTime() {
        return Instant.now().plusSeconds(86400); //24 horas
    }
}
