package com.dollartrading.trading.controllers;

import com.dollartrading.trading.dto.AccountDto;
import com.dollartrading.trading.service.Messages;
import com.dollartrading.trading.service.TestsVars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseControllerTest{

    private AccountDto accountDto;
    private AccountDto existingAccountDto;
    private AccountDto badAccountDto;
    private AccountDto updateAccountDto;

    @BeforeEach
    void setUp() {
        accountDto = new AccountDto(
                TestsVars.BANK_NAME.getString(),
                TestsVars.NAME.getString(),
                TestsVars.PASSWORD.getString());
        existingAccountDto = new AccountDto(
                TestsVars.BANK_NAME.getString(),
                TestsVars.NAME_2.getString(),
                TestsVars.PASSWORD.getString());
        badAccountDto = new AccountDto(
                TestsVars.BANK_NAME.getString(),
                TestsVars.TOO_LONG_NAME.getString(),
                TestsVars.PASSWORD.getString());
        updateAccountDto = new AccountDto(
                TestsVars.BANK_NAME.getString(),
                TestsVars.NAME_3.getString(),
                TestsVars.PASSWORD.getString());
    }

    @Test
    @Order(1)
    void testAddEntity() throws Exception {
        mockMvc.perform(post("/account/create")
                        .content(objectMapper.writeValueAsString(accountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.message").value(Messages.ADDED_MESSAGE.getMessage()));
    }

    @Test
    @Order(2)
    void testUpdateEntity() throws Exception {
        mockMvc.perform(put("/account/{id}", TestsVars.ID.getTestId()+1)
                        .content(objectMapper.writeValueAsString(updateAccountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.message").value(Messages.UPDATED_MESSAGE.getMessage()));
    }

    @Test
    @Order(5)
    void testDeleteEntity() throws Exception {
        mockMvc.perform(delete("/account/{id}", TestsVars.ID.getTestId()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void willThrowWhenEntityAlreadyExist() throws Exception {
        mockMvc.perform(post("/account/create")
                        .content(objectMapper.writeValueAsString(existingAccountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Messages.ADDED_EXCEPTION_MESSAGE.getMessage()));
    }

    @Test
    @Order(4)
    void willThrowWhenEntityNotFound() throws Exception {
        mockMvc.perform(put("/account/{id}", TestsVars.ID.getTestId()+5)
                        .content(objectMapper.writeValueAsString(accountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Messages.NOT_FOUND_MESSAGE.getMessage()));
    }

    @Test
    void willThrowWhenEntitySavingError() throws Exception {
        mockMvc.perform(post("/account/create")
                        .content(objectMapper.writeValueAsString(badAccountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void willThrowWhenEntityUpdatingError() throws Exception {
        mockMvc.perform(put("/account/{id}", TestsVars.ID.getTestId())
                        .content(objectMapper.writeValueAsString(badAccountDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
