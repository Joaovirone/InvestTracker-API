package com.joaovirone.invest.invest_tracker.infrastructure.adapter.external;

import com.joaovirone.invest.invest_tracker.domain.port.*;
import com.joaovirone.invest.invest_tracker.infrastructure.adapter.external.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;

@Slf4j // Para logs
@Service // Diz pro Spring: "Isso aqui é um componente injetável"
@RequiredArgsConstructor // Cria construtor com o WebClient automaticamente
public class BrapiAdapter implements QuoteProvider {

    private final WebClient webClient;
    
    // Pegue seu token gratuito em https://brapi.dev/dashboard e coloque aqui ou no application.properties
    private final String TOKEN = "etVtCMN1grJQcgk4dkesvB"; 

    @Override
    public BigDecimal getPrice(String ticker) {
        log.info("Buscando cotação para: {}", ticker);

        try {
            BrapiResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/quote/{ticker}")
                            .queryParam("token", TOKEN) // Brapi exige token na maioria das vezes
                            .build(ticker))
                    .retrieve()
                    .bodyToMono(BrapiResponse.class) // Transforma JSON em Objeto Java
                    .block(); //  block() torna síncrono. Em alta escala usaríamos subscribe(), mas para este MVP está ok.

            if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                return response.getResults().get(0).getRegularMarketPrice();
            }
            
            throw new RuntimeException("Ativo não encontrado na Brapi");

        } catch (Exception e) {
            log.error("Erro ao buscar cotação", e);
            throw new RuntimeException("Erro ao consultar API externa");
        }
    }
}