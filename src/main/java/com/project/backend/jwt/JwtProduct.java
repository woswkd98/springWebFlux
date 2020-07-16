package com.project.backend.jwt;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.ParseException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;
import com.nimbusds.jwt.*;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.jose.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;

@Component
public class JwtProduct {
    
    private static byte[] sharedSecret;

    public static final String ISSUER = "jinheung";
    public static final Long EXPIRATION_TIME = 30 * 60 * 1000L; //30 * 60 * 1000L; // 1초에 1000 * 60초 * 30 즉 30분

    
    private JWSSigner signer  = null;
    private JWSVerifier verifier = null;
    @PostConstruct
    public void init() throws JOSEException {
        sharedSecret = new byte[32];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(sharedSecret);
        try {
            signer  = new MACSigner(sharedSecret);
            verifier = new MACVerifier(sharedSecret);
        } catch (KeyLengthException e) {
         
            e.printStackTrace();
        }

    }

    public String getKey(String subject) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(subject) // Claim의 주제(Subject)로 토큰이 갖는 문맥을 의미한다.
        .issuer(ISSUER) // 토큰을 발급한 발급자(Issuer)
        .expirationTime(new Date(new Date().getTime() + EXPIRATION_TIME)) // 만료시간
        .build();

        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(this.signer);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return signedJWT.serialize();
    }

    public boolean verify(String subject, String serialized) throws ParseException, JOSEException {
        SignedJWT signedJWT = null;
        signedJWT = SignedJWT.parse(serialized);
        if(signedJWT == null) {
            return false;
        }

        if(!signedJWT.verify(verifier)) {
            return false;
        }
        
        if(!subject.equals(signedJWT.getJWTClaimsSet().getSubject())) {
            System.out.println("subject");
            System.out.println(signedJWT.getJWTClaimsSet().getSubject());
            return false;
        }

        if(!new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {
            System.out.println("Date");
            System.out.println(signedJWT.getJWTClaimsSet().getExpirationTime());
            return false;
        }
        if(!ISSUER.equals(signedJWT.getJWTClaimsSet().getIssuer())) {
            System.out.println("getIssuer");
            System.out.println(signedJWT.getJWTClaimsSet().getIssuer());
            return false;
        }
        System.out.println("success");
        return true;
        
    }

}