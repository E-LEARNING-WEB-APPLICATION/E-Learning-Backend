package com.learnease.server.util;


import com.learnease.server.model.UserAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/*
    we need the bean of this class in different classes
    that's why we are registering this as a spring managed bean
*/
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private SecretKey key;

    @PostConstruct
    public void myInit(){
        //here we are converting the string key into the SecretKey Object of javax.crypto.SecretKey
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    //generate the jwt token
    public String generateToken(UserAuth user){
        Date createdOn = new Date();
        Date expirationDate =new Date(createdOn.getTime() + expiration);
        return Jwts.builder() //Createing the Jwt builder
                .subject(user.getEmail()) //set the subject (issuer)
                .issuedAt(createdOn) //set issuedAt
                .expiration(expirationDate) //set Expiration
                //payload of jwt token - custom claims
                .claims(Map.of("user_id" , user.getId(),
                    "role" , user.getRole().name()))
                .signWith(key) // Signed with key for signature of jwt
                .compact(); //generate the token string
    };

}
