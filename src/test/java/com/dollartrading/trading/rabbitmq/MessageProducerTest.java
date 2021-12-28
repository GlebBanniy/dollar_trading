package com.dollartrading.trading.rabbitmq;

import com.dollartrading.trading.dto.BidDto;
import com.dollartrading.trading.dto.OperationStatusDto;
import com.dollartrading.trading.exceptions.EntityAddingException;
import com.dollartrading.trading.service.BidService;
import com.dollartrading.trading.service.Messages;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.binder.MessageCollectorAutoConfiguration;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static com.dollartrading.trading.service.TestsVars.CORRECT_BID_VALUE;
import static com.dollartrading.trading.service.TestsVars.NAME_2;
import static com.dollartrading.trading.service.TestsVars.TRUE_VALUE;
import static com.dollartrading.trading.service.TestsVars.USDRUB;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@PropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@ImportAutoConfiguration({TestSupportBinderAutoConfiguration.class, MessageCollectorAutoConfiguration.class})
class MessageProducerTest {

    private OperationStatusDto osDto;
    private BidDto bidDto;

    @Autowired
    private BidService bidService;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private MessageProducer messageProducer;

    @BeforeEach
    void setUp() {
        bidDto = new BidDto(
                NAME_2.getString(),
                USDRUB.getString(),
                CORRECT_BID_VALUE.getValue(),
                TRUE_VALUE.isbValue());
        osDto = OperationStatusDto.builder()
                .id(2L)
                .message(Messages.ADDED_MESSAGE.getMessage())
                .build();
    }

    @Test
    void tryPublishMessage() throws EntityAddingException{
        bidService.handleMessage(bidDto);
        Gson g = new Gson();
        OperationStatusDto result = g.fromJson(Objects.requireNonNull(messageCollector.forChannel(
                        messageProducer.getMySource().output()).poll()).getPayload().toString(),
                OperationStatusDto.class
        );
        assertEquals(osDto, result);
    }
}
