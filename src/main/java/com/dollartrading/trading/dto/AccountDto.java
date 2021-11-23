package com.dollartrading.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AccountDto {

    @NotBlank(message = "Необходимо указать название банка")
    private final String payment_name;
    @NotBlank(message = "Необходимо ввести имя")
    private final String username;
    @NotBlank(message = "Необходимо ввести пароль")
    private final String password;
}
