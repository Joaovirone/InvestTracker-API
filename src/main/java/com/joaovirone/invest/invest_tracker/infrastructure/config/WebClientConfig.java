package com.joaovirone.invest.invest_tracker.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    

    @Bean
    public WebClient webClient(WebClient.Builder builder) {

        
        return builder //aqui definimos a URL base para não repetir em todo lugar
                .baseUrl("https://brapi.dev/api")
                .build();
    }
}
