package com.dollartrading.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CurrencyLayerDto {
    private Map<String, Double> quotes;
    private Boolean success;
    private String terms;
    private String privacy;
    private Long timestamp;
    private String source;
}
