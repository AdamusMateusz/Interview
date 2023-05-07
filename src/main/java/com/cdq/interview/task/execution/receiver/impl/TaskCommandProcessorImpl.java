package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.pattern.PatternMatcher;
import com.cdq.interview.pattern.model.FindPatternCommand;
import com.cdq.interview.pattern.model.MatchingResult;
import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.model.api.TaskStatusCode;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TaskCommandProcessorImpl implements TaskCommandProcessor {

    private final TasksDao tasksDao;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        return Mono.just(command)
                .map(c -> new FindPatternCommand(c.input(), c.pattern()))
                .map(PatternMatcher::fromCommand)
                .map(PatternMatcher::findFirstBestMatch)
                .map(result -> buildUpdateTaskStatusCommand(command, result))
                .flatMap(tasksDao::updateTaskStatus)
                .then();
    }

    private UpdateTaskStatusCommand buildUpdateTaskStatusCommand(ExecuteTaskCommand executeTaskCommand, MatchingResult result) {
        return new UpdateTaskStatusCommand(
                executeTaskCommand.id(),
                TaskStatusCode.ENDED,
                result.isMatchFound(),
                result.matchDescription().position(),
                result.matchDescription().typos()
        );
    }
}
