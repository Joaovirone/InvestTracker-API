package com.joaovirone.invest.invest_tracker.domain.port;

import java.math.BigDecimal;

public interface QuoteProvider {
    
    //O domínio só quer o preço, não importa de onde vem
    BigDecimal getPrice(String ticker);
}
