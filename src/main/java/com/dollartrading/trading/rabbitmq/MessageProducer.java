package com.dollartrading.trading.rabbitmq;

import com.dollartrading.trading.dto.OperationStatusDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(Source.class)
@Component
public class MessageProducer {

    private final Source mySource;

    @Autowired
    public MessageProducer(Source mySource) {
        this.mySource = mySource;
    }

    public void publishMessage(OperationStatusDto osDto){
        mySource.output().send(MessageBuilder.withPayload(osDto).build());
    }

    public Source getMySource() {
        return mySource;
    }
}
