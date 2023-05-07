package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@AllArgsConstructor
public class PercentageCountingCommandProcessor implements TaskCommandProcessor {

    private final int startingPercentage;
    private final int endingPercentage;
    private final int step;
    private final Duration delay;
    private final TaskCommandProcessor taskCommandProcessor;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        return Flux.range(startingPercentage, endingPercentage + 1)
                .buffer(step)
                .delayElements(delay)
                //TODO incement completition percetage
                .then(taskCommandProcessor.process(command));

    }
}
