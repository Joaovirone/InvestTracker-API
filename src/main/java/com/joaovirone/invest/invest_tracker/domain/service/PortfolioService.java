package com.joaovirone.invest.invest_tracker.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import static java.math.RoundingMode.HALF_UP;

import org.springframework.stereotype.Service;

import com.joaovirone.invest.invest_tracker.domain.model.Asset;
import com.joaovirone.invest.invest_tracker.domain.model.Position;
import com.joaovirone.invest.invest_tracker.domain.port.QuoteProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    
    private final QuoteProvider quoteProvider;
    
    public Position calculatePosition (Asset asset) {
        
        // Busca o preço atual na API Externa (através da Interface)
        BigDecimal currentPrice = quoteProvider.getPrice(asset.getTicker());

        // Calcula o valor total atual
        BigDecimal totalValue = currentPrice.multiply(asset.getQuantity());

        // Calcula o custo total original
        BigDecimal totalCost = asset.getTotalCost();

        // Calcula o Lucro/Prejuízo (Valor atual - Custo)
        BigDecimal profitOrLoss = totalValue.subtract(totalCost);


        // Calcula o ROI %: ((Valor atual - Custo)/ Custo) * 100
        BigDecimal roi = BigDecimal.ZERO;

        if(totalCost.compareTo(BigDecimal.ZERO) > 0){
            roi = profitOrLoss.divide(totalCost, 4 , RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2,RoundingMode.HALF_UP);
        }


        // Retorna o objeto rico montado
        return Position.builder()
                .asset(asset)
                .currentPrice(currentPrice)
                .totalValue(totalValue)
                .profitOrLoss(profitOrLoss)
                .roi(roi)
                .build();
    }

}
