package com.bookshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bookshop.service.impl.UserSecurityService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    public SecurityConfig(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userSecurityService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())   // <<< FIX
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/image/**",
                        "/",
                        "/newUser",
            			"/forgetPassword",
                        "/login",
            			"/fonts/**",
                        "/bookshelf"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
                    .deleteCookies("remember-me")
                    .permitAll()
            )
            .rememberMe(remember -> remember
                    .key("uniqueAndSecretKey")
                    .userDetailsService(userSecurityService)
            );

        return http.build();
    }
}

