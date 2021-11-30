package com.dollartrading.trading.controllers;

import com.dollartrading.trading.dto.BidDto;
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

class BidControllerTest extends BaseControllerTest{

    private BidDto bidDto;
    private BidDto badBidDto;

    @BeforeEach
    void setUp() {
        bidDto = new BidDto(
                TestsVars.NAME_2.getString(),
                TestsVars.USDRUB.getString(),
                TestsVars.CORRECT_BID_VALUE.getValue(),
                true
        );
        badBidDto = new BidDto(
                TestsVars.NAME.getString(),
                TestsVars.USDRUB.getString(),
                TestsVars.CORRECT_BID_VALUE.getValue(),
                true
        );
    }

    @Test
    @Order(1)
    void testAddBid() throws Exception {
        mockMvc.perform(post("/bid/create")
                .content(objectMapper.writeValueAsString(bidDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.message").value(Messages.ADDED_MESSAGE.getMessage()));
    }

    @Test
    @Order(2)
    void testUpdateBid() throws Exception {
        mockMvc.perform(put("/bid/{id}", TestsVars.ID.getTestId())
                        .content(objectMapper.writeValueAsString(bidDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.message").value(Messages.UPDATED_MESSAGE.getMessage()));
    }

    @Test
    @Order(5)
    void testDeleteBid() throws Exception {
        mockMvc.perform(delete("/bid/{id}", TestsVars.ID.getTestId()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void willThrowWhenEntityNotFound() throws Exception {
        mockMvc.perform(put("/bid/{id}", TestsVars.ID.getTestId()+5)
                        .content(objectMapper.writeValueAsString(bidDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Messages.NOT_FOUND_MESSAGE.getMessage()));
    }

    @Test
    void willThrowWhenEntitySavingError() throws Exception {
        mockMvc.perform(post("/bid/create")
                        .content(objectMapper.writeValueAsString(badBidDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void willThrowWhenEntityUpdatingError() throws Exception {
        mockMvc.perform(put("/bid/{id}", TestsVars.ID.getTestId())
                        .content(objectMapper.writeValueAsString(badBidDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
