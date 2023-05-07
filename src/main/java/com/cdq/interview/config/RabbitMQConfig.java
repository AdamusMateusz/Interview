package com.cdq.interview.config;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;
import reactor.util.retry.RetrySpec;

import java.time.Duration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public SenderOptions senderOptions() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();


        return new SenderOptions()
                .connectionMonoConfigurator(cm -> cm.retryWhen(RetrySpec.backoff(10, Duration.ofSeconds(5))))
                .connectionFactory(connectionFactory)
                .connectionSupplier(cf -> cf.newConnection(
                                new Address[]{new Address("interview-rabbitmq")},
                                "interview-reactive-connection"
                        )
                ).resourceManagementScheduler(Schedulers.boundedElastic());

    }

    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }

}
