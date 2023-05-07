package com.cdq.interview.task.execution.sender;

import com.cdq.interview.task.execution.mapper.MessageSerializer;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Slf4j
@Component
public class TasksExecutionService {

    private FluxSink<ExecuteTaskCommand> sink;

    public TasksExecutionService(Sender sender, MessageSerializer messageSerializer) {
        Flux<OutboundMessage> infiniteCommandsFlux =
                Flux.<ExecuteTaskCommand>create(fluxSink -> this.sink = fluxSink)
                        .map(messageSerializer::fromCommandObject)
                        .map(bytes ->
                                new OutboundMessage(
                                        "interview.adamus",
                                        "task",
                                        bytes
                                )
                        );

        sender.send(infiniteCommandsFlux).subscribe();
    }

    public Mono<Void> queue(ExecuteTaskCommand executeTaskCommand) {
        log.info("Queueing command {}", executeTaskCommand);

        return Mono.just(executeTaskCommand)
                .doOnNext(sink::next)
                .then();
    }

    @PreDestroy
    public void preDestroy() {
        sink.complete();
    }

}
