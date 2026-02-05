package com.bookshop.utility;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityUtility {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
private static final String SALT = "salt"; // Salt should be protected carefully
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder1() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}
	
	
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

	public static Object passwordEncoder() {
		// TODO Auto-generated method stub
		return null;
	}
}
