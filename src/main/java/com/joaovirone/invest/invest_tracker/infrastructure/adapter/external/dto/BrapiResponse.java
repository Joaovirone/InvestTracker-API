package com.joaovirone.invest.invest_tracker.infrastructure.adapter.external.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class BrapiResponse {
    

    @JsonProperty("results")
    private List<BrapiResult> results;


    @Data
    public static class BrapiResult {

        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("regularMarketPrice")
        private BigDecimal regularMarketPrice;
    }


}
