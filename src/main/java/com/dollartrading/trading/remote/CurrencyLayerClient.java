package com.dollartrading.trading.remote;

import com.dollartrading.trading.dto.CurrencyLayerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currencyLayer", url = "${url}")
public interface CurrencyLayerClient {

    @GetMapping(value = "/live", consumes = "application/json", produces = "application/json")
    CurrencyLayerDto getData(@RequestParam String access_key,
                             @RequestParam String currencies,
                             @RequestParam Integer format);
}
