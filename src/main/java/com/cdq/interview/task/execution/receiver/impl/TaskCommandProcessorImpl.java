package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.pattern.PatternMatcher;
import com.cdq.interview.pattern.model.FindPatternCommand;
import com.cdq.interview.pattern.model.MatchingResult;
import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskResult;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TaskCommandProcessorImpl implements TaskCommandProcessor {

    private final TasksDao tasksDao;
    private final IMap<String, Task> tasksCache;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        return Mono.just(command)
                .map(c -> new FindPatternCommand(c.input(), c.pattern()))
                .map(PatternMatcher::fromCommand)
                .map(PatternMatcher::findFirstBestMatch)
                .flatMap(result ->
                        Mono.just(buildUpdateTaskStatusCommand(command, result))
                                .flatMap(tasksDao::updateTaskStatus)
                                .then(Mono.fromCallable(() -> toCacheTask(command, result)))
                                .flatMap(task -> Mono.fromCompletionStage(tasksCache.putAsync(task.id(), task)))
                ).then();
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

    private Task toCacheTask(ExecuteTaskCommand command, MatchingResult result) {
        return new Task(
                command.id(),
                command.input(),
                command.pattern(),
                new TaskStatus(
                        TaskStatusCode.ENDED,
                        100
                ),
                new TaskResult(
                        result.isMatchFound(),
                        result.matchDescription().position(),
                        result.matchDescription().typos()
                )
        );
    }
}
