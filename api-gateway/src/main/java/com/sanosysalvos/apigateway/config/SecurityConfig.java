package com.sanosysalvos.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // Permitimos el paso para pruebas en local sin Token Real Auth0
                // .pathMatchers("/actuator/health/**", "/actuator/info/**").permitAll() 
                // .anyExchange().authenticated() 
            );
            // Comentado para no requerir validación JWT temporalmente
            // .oauth2ResourceServer(oauth2 -> oauth2
            //    .jwt(jwt -> {}) 
            // );
            
        return http.build();
    }
}
