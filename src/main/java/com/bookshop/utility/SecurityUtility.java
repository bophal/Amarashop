package com.bookshop.utility;

import java.util.Random;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityUtility {
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	/*@Bean
    public  PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    // remove @Bean passwordEncoder() - handled in SecurityConfig

    public static String randomPassword() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < 18) {
            int index = rnd.nextInt(SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }
}
