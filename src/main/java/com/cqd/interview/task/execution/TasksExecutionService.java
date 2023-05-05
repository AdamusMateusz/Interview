package com.cqd.interview.task.execution;

import com.cqd.interview.pattern.PatternMatcher;
import com.cqd.interview.pattern.model.FindPatternCommand;
import com.cqd.interview.pattern.model.MatchingResult;
import com.cqd.interview.task.dao.LoggingTasksDao;
import com.cqd.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cqd.interview.task.execution.model.ExecuteTaskCommand;
import com.cqd.interview.task.model.api.TaskStatusCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TasksExecutionService {

    private final LoggingTasksDao tasksRepository;

    public Mono<Void> queue(ExecuteTaskCommand executeTaskCommand) {
        return Mono.just(executeTaskCommand)
                .map(command -> new FindPatternCommand(command.input(), command.pattern()))
                .map(PatternMatcher::fromCommand)
                .map(PatternMatcher::findFirstBestMatch)
                .map(result -> buildUpdateTaskStatusCommand(executeTaskCommand, result))
                .map(tasksRepository::updateTaskStatus)
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
