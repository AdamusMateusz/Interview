package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.execution.mapper.RetryMessageException;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@AllArgsConstructor
public class RetryingCommandProcessor implements TaskCommandProcessor {

    private final AtomicInteger processedCounter;
    private final int retryEveryNthElement;
    private final TaskCommandProcessor taskCommandProcessor;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        int commandIndex = processedCounter.incrementAndGet();

        if (commandIndex == retryEveryNthElement){
            processedCounter.set(0);
            log.info("Retrying command {}", command);
            throw new RetryMessageException();
        }

        return taskCommandProcessor.process(command);
    }
}
