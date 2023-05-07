package com.cdq.interview.config;

import com.cdq.interview.config.model.TopologyCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Sender;

import java.util.concurrent.atomic.AtomicBoolean;

import static reactor.rabbitmq.ResourcesSpecification.*;

@Slf4j
@Component
@AllArgsConstructor
public class RabbitMQTopologyGenerator {

    private final Sender sender;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AtomicBoolean isFirstRun = new AtomicBoolean(true);

    @EventListener
    public void generate(ContextRefreshedEvent event) {
        if (isFirstRun.getAndSet(false)) {

            sender.declare(exchange("interview.adamus"))
                    .then(sender.declare(queue("interview.adamus.tasks").durable(true)))
                    .then(sender.bind(binding("interview.adamus", "task", "interview.adamus.tasks")))
                    .then(Mono.fromRunnable(() -> applicationEventPublisher.publishEvent(new TopologyCreatedEvent(this))))
                    .subscribe(r -> System.out.println("Exchange and queue declared and bound"));
        }
    }
}
