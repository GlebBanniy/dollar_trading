package com.dollartrading.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class BidDto {
    @NotBlank(message = "Необходимо указать имя")
    private final Integer userId;
    @NotBlank(message = "Необходимо указать валюту")
    private final String currency;
    @Pattern(regexp = "[0-9]{1,8}\\.[0-9]{2}", message = "Укажите правильный курс, например 72.66")
    private final Integer bidValue;
}
