package com.RabbitMQ;

import javax.annotation.PostConstruct;

import com.rabbitmq.client.ConnectionFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.BindingSpecification;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;
import reactor.test.StepVerifier;
import lombok.extern.slf4j.Slf4j;
import com.rabbitmq.client.AMQP;

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
        
    

        ReceiverOptions receiverOptions =  new ReceiverOptions()
        .connectionFactory(connectionFactory)                       
        .connectionSubscriptionScheduler(Schedulers.elastic());

        
    }

    @Test
    public void AMQP() {
        SenderOptions senderOptions = new SenderOptions()
        .connectionFactory(connectionFactory)                         
        .resourceManagementScheduler(Schedulers.elastic());  
        
        Sender sender = RabbitFlux.createSender(senderOptions);
        
        Mono<AMQP.Exchange.DeclareOk> exchange = sender.declareExchange(
            ExchangeSpecification.exchange("my.queue")
        );

        Mono<AMQP.Queue.DeclareOk> queue =  sender.declareQueue(QueueSpecification.queue("my.queue"));
        
        Mono<AMQP.Queue.BindOk> binding = sender.bind(
            BindingSpecification.binding().exchange("my.exchange").
            queue("my.queue").routingKey("a,b")
        );    
    }

    public void test3() {
    Sender sender = RabbitFlux.createSender(new SenderOptions()
        .connectionMono(
            Mono.fromCallable(() -> connectionFactory.newConnection("sender")).cache())   
    );
    
    


    Receiver receiver = RabbitFlux.createReceiver(new ReceiverOptions()
        .connectionMono(
            Mono.fromCallable(() -> connectionFactory.newConnection("receiver")).cache()) 
    );

        
    }
    

}