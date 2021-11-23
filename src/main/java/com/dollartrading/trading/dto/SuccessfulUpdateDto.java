package com.dollartrading.trading.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SuccessfulUpdateDto {
    @NotBlank
    private final Long id;

    @NotBlank
    private final String message;
}
