package com.dollartrading.trading.controllers;

import com.dollartrading.trading.dto.CurrencyLayerDto;
import com.dollartrading.trading.remote.CurrencyLayerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public class CurrencyLayerController {

    @Value("${url.access.key}")
    private String key;
    @Value("${url.currencies}")
    private String currencies;
    @Value("${url.format}")
    private Integer format;

    private final CurrencyLayerClient currencyLayerClient;

    @Autowired
    public CurrencyLayerController(CurrencyLayerClient currencyLayerClient) {
        this.currencyLayerClient = currencyLayerClient;
    }

    @GetMapping("/data")
    public CurrencyLayerDto getCurrencyData(){
        return currencyLayerClient.getData(key, currencies, format);
    }
}
