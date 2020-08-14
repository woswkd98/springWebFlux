package com.RabbitMQ;

import javax.annotation.PostConstruct;

import com.rabbitmq.client.ConnectionFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;
import reactor.test.StepVerifier;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class RabbitmqTest {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public void pubSubTest() {
     
        SenderOptions senderOptions = new SenderOptions()
        .connectionFactory(connectionFactory)                         
        .resourceManagementScheduler(Schedulers.elastic());  
        Sender sender = RabbitFlux.createSender(senderOptions);
        Flux<OutboundMessage> outboundFlux  =
        Flux.range(1, 10)
        .map(i -> new OutboundMessage(
            "amq.direct",
            "routing.key", ("Message " + i).getBytes()
        ));
        outboundFlux.subscribe();


        ReceiverOptions receiverOptions =  new ReceiverOptions()
        .connectionFactory(connectionFactory)                       
        .connectionSubscriptionScheduler(Schedulers.elastic());

        Receiver receiver = RabbitFlux.createReceiver(receiverOptions);
    
        receiver.consumeNoAck("reactive.queue");
    }


}