package com.dollartrading.trading.controllers;


import com.google.common.base.Charsets;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/live")
public class CurrencyLayerControllerTest {

    @GetMapping(value = "")
    public String getCurrencyData() throws IOException {
        return Files.readString(Path.of("src/test/resources/currencyData.json"), Charsets.UTF_8);
    }
}
