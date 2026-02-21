package com.soiree;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Classe principale de l'application Spring Boot
 * @SpringBootApplication : Active la configuration automatique de Spring
 */
@SpringBootApplication
public class SoireeJeuxBackendApplication {

    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    public static void main(String[] args) {

        SpringApplication.run(SoireeJeuxBackendApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("🎉 Application Soirée Jeux démarrée !");
        System.out.println("📍 Backend : http://localhost:8080");
        System.out.println("🗄️ Console H2 : http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }

    /**
     * Configuration CORS pour permettre à Angular de communiquer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(List.of(allowedOrigins).toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
