package com.cdq.interview.task.execution.receiver;

import com.cdq.interview.config.model.TopologyCreatedEvent;
import com.cdq.interview.task.execution.mapper.MessageSerializer;
import com.cdq.interview.task.execution.mapper.RetryMessageException;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.sender.TasksExecutionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.Receiver;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@AllArgsConstructor
public class TasksQueueListener {

    private final Receiver receiver;
    private final MessageSerializer messageSerializer;
    private final TaskCommandProcessor taskCommandProcessor;
    private final TasksExecutionService tasksExecutionService;
    private final AtomicBoolean isFirstRun = new AtomicBoolean(true);

    @EventListener
    public void connect(TopologyCreatedEvent topologyCreatedEvent) {
        if (isFirstRun.getAndSet(false)) {

            ConsumeOptions consumeOptions = new ConsumeOptions().qos(1);

            receiver.consumeManualAck("interview.adamus.tasks", consumeOptions)
                    .flatMap(message ->
                            executeCommand(message.getBody())
                                    .then(Mono.fromRunnable(message::ack))
                    ).subscribe();
        }
    }

    private Mono<Void> executeCommand(byte[] commandBody) {
        ExecuteTaskCommand command = messageSerializer.toCommandObject(commandBody);

        return taskCommandProcessor.process(command)
                .doOnError(throwable -> log.error("Error occurred: ", throwable))
                .onErrorResume(RetryMessageException.class, throwable -> tasksExecutionService.queue(command))
                .onErrorResume(Throwable.class, throwable -> Mono.empty());
    }

}
