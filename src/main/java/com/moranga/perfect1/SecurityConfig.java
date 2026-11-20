package com.moranga.perfect1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encrypts passwords
    }

    // NEW: Ignore static resources so they can still be cached for performance
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow everyone to see these pages
                        .requestMatchers("/", "/register", "/login").permitAll()

                        // 2. ONLY sellers can access the dashboard (Must be before anyRequest!)
                        .requestMatchers("/seller/**").hasRole("SELLER")

                        // 3. Any other page requires you to be logged in
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // Ensures server session is destroyed
                        .deleteCookies("JSESSIONID") // Deletes the session cookie
                        .permitAll()
                )
                // NEW: Force browser to not cache protected pages
                .headers(headers -> headers
                        .cacheControl(cache -> {})
                );

        return http.build();
    }
}