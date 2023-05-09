package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
public class PercentageCountingCommandProcessor implements TaskCommandProcessor {

    private final int startingPercentage;
    private final int endingPercentage;
    private final int step;
    private final Duration delay;
    private final TaskCommandProcessor taskCommandProcessor;
    private final IMap<String, Task> tasksCache;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        return Flux.range(startingPercentage + 1, endingPercentage - startingPercentage)
                .buffer(step)
                .delayElements(delay)
                .map(integers -> integers.get(integers.size() - 1))
                .map(percentage -> toTask(command, percentage))
                .flatMap(task -> Mono.fromCompletionStage(tasksCache.putAsync(task.id(), task)))
                .then(taskCommandProcessor.process(command));
    }

    private Task toTask(ExecuteTaskCommand command, Integer percentage) {
        return new Task(
                command.id(),
                command.input(),
                command.pattern(),
                new TaskStatus(
                        TaskStatusCode.STARTED,
                        percentage
                ),
                null
        );
    }
}
