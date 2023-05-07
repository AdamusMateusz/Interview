package com.cdq.interview.config;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;
import reactor.util.retry.RetrySpec;

import java.time.Duration;


@Configuration
public class RabbitMQConfig {


    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();

        return connectionFactory;
    }

    @Bean
    public SenderOptions senderOptions(@Qualifier("rabbitConnectionFactory") ConnectionFactory connectionFactory) {
        return new SenderOptions()
                .connectionMonoConfigurator(cm -> cm.retryWhen(RetrySpec.backoff(10, Duration.ofSeconds(5))))
                .connectionFactory(connectionFactory)
                .connectionSupplier(cf -> cf.newConnection(
                                new Address[]{new Address("interview-rabbitmq")},
                                "interview-reactive-connection-sender"
                        )
                ).resourceManagementScheduler(Schedulers.boundedElastic());

    }

    @Bean
    public ReceiverOptions receiverOptions(@Qualifier("rabbitConnectionFactory") ConnectionFactory connectionFactory) {
        return new ReceiverOptions()
                .connectionMonoConfigurator(cm -> cm.retryWhen(RetrySpec.backoff(10, Duration.ofSeconds(5))))
                .connectionFactory(connectionFactory)
                .connectionSupplier(cf -> cf.newConnection(
                                new Address[]{new Address("interview-rabbitmq")},
                                "interview-reactive-connection-listener"
                        )
                ).connectionSubscriptionScheduler(Schedulers.boundedElastic());
    }


    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }

}
