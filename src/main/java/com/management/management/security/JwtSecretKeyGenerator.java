package com.management.management.security;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecretKeyGenerator {
    @Bean
    public String secretKeyGenerator() throws Exception{
        KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
        keygen.init(256); // set keysize to 256 bits
        SecretKey secretKey = keygen.generateKey();
       return Base64.getEncoder().encodeToString(secretKey.getEncoded());


        
    }

}
