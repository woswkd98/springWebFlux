package com.project.backend.Configurations;

import java.net.Authenticator;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class Email {
    private final String emailConfirmKey = "emailConfirmKey";

    String host = "smtp.naver.com";
    final String username = "woswkd98";
    final String password = "3208WLs03!";

    int port = 465;


    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    Email(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }
    Session session = null;
    public static final String redisKey = "emailConfirm";
    @PostConstruct
    public void init() {

        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", host);

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            String un = username;
            String pw = password;

            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(un, pw);
            }
        });
    }

    @PreDestroy
    public void destroy() {}

    public void sendMail(String email) {
        ReactiveHashOperations<String, String, String> hashOps = reactiveRedisTemplate.opsForHash();
        int rand  = (int)(Math.random() * 100000);
        hashOps.put(redisKey, email ,String.valueOf(rand))
        .then(hashOps.size(redisKey).flatMap(u -> {
            return Mono.just(u);
        })).then(Mono.defer(()-> {
            Message mimeMessage = new MimeMessage(session);
            try {
                mimeMessage.setFrom(new InternetAddress("woswkd98@naver.com"));
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            try {
                mimeMessage.setSubject("인증");
                mimeMessage.setText("인증번호  "  +  rand); 
                Transport.send(mimeMessage); 
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return Mono.just(1);
        })).subscribe();

            

      
        //session.setDebug(true);
       
    }
}