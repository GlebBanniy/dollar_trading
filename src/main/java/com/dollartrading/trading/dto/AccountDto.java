package com.dollartrading.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AccountDto {
    @Size(min=2, max = 10)
    @NotBlank(message = "Необходимо указать название банка")
    private final String paymentName;

    @Size(min=2, max = 10)
    @NotBlank(message = "Необходимо ввести имя")
    private final String username;

    @Size(min=2, max = 10)
    @NotBlank(message = "Необходимо ввести пароль")
    private final String password;
}
