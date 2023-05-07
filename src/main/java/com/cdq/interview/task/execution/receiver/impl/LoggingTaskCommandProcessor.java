package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class LoggingTaskCommandProcessor implements TaskCommandProcessor {

    private final TaskCommandProcessor taskCommandProcessor;

    public Mono<Void> process(ExecuteTaskCommand command) {
        log.info("Processing command {}", command);
        return taskCommandProcessor.process(command);
    }
}
