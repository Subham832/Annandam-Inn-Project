package com.annandam_inn.service;

import com.annandam_inn.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}") //write this same to the appliction.properties file.
    private String algorithmKey; //When the project will start then the value of application.properties file will come autmaatically to this.
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry.duration}")
    private int expiryTime;
    private Algorithm algorithm; //It came while putting Dependency.
    private final static String USER_NAME = "username";

    @PostConstruct
    public void postConstruct(){

       algorithm =  algorithm.HMAC256(algorithmKey);

    }
    public String generateToken (PropertyUser propertyUser){ //this all will build the token

      return  JWT.create()
                .withClaim(USER_NAME, propertyUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime)) //In this we gave the currentTimeMillis a expire time.
                .withIssuer(issuer)
                .sign(algorithm); //In this algorithm and secret key both comes here.

    }

    public String getUsername(String token){
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJWT.getClaim(USER_NAME).asString();
    }

}
