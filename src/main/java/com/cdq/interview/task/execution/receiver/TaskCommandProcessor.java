package com.cdq.interview.task.execution.receiver;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import reactor.core.publisher.Mono;

public interface TaskCommandProcessor {
    Mono<Void> process(ExecuteTaskCommand command);
}
