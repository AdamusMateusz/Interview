package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@AllArgsConstructor
public class RejectingCommandProcessor implements TaskCommandProcessor {

    private final AtomicInteger processedCounter;
    private final int rejectEveryNthElement;
    private final TaskCommandProcessor taskCommandProcessor;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        int commandIndex = processedCounter.incrementAndGet();

        if (commandIndex == rejectEveryNthElement) {
            processedCounter.set(0);

            log.info("Rejecting command {}", command);
            throw new RuntimeException();
        }

        return taskCommandProcessor.process(command);
    }
}
