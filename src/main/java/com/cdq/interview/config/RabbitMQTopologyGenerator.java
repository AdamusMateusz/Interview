package com.cdq.interview.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Sender;

import java.util.concurrent.atomic.AtomicBoolean;

import static reactor.rabbitmq.ResourcesSpecification.*;

@Slf4j
@Component
@AllArgsConstructor
public class RabbitMQTopologyGenerator {

    private final Sender sender;
    private final AtomicBoolean isFirstRun = new AtomicBoolean(true);

    @SneakyThrows
    @EventListener
    public void test(ContextRefreshedEvent event) {
        if (isFirstRun.getAndSet(false)) {

            sender.declare(exchange("interview.adamus"))
                    .then(sender.declare(queue("interview.adamus.tasks")))
                    .then(sender.bind(binding("interview.adamus", "task", "interview.adamus.tasks")))
                    .subscribe(r -> System.out.println("Exchange and queue declared and bound"));
        }
    }
}
