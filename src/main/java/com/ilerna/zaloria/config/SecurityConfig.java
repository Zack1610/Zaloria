package com.ilerna.zaloria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean // Le decimos a Spring: "Guarda esta configuración"
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                    
    // 1. "A estos sitios puede entrar todo el mundo sin preguntar"
    .requestMatchers("/", "/css/**", "/images/**", "/js/**").permitAll() 

        // 2. "Para entrar aquí, tienes que haber pasado por el login sí o sí"
        .requestMatchers("/admin/**", "/torneos/**", "/equipos/**", "/jugadores/**").authenticated() 

            // 3. "Cualquier otra cosa que no esté arriba, también es libre"
            .anyRequest().permitAll()
                )
            .formLogin((form) -> form
                .defaultSuccessUrl("/admin", true) // Si entras bien, vas al panel
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/") // <--- ESTO ES LO QUE QUERÍAS: Al salir, a la portada
                .permitAll()
            );

        return http.build(); // "Vigilante, aquí tienes tus instrucciones terminadas"
    }

    // Aquí creas la "pieza" que contiene los datos del usuario admin
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("admin")
            .password("{noop}zaloria2026")
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}