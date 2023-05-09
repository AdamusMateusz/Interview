package com.cdq.interview.task.execution.receiver.impl;

import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import com.hazelcast.map.IMap;
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
    private final TasksDao tasksDao;
    private final IMap<String, Task> tasksCache;

    @Override
    public Mono<Void> process(ExecuteTaskCommand command) {
        int commandIndex = processedCounter.incrementAndGet();

        if (commandIndex == rejectEveryNthElement) {
            processedCounter.set(0);

            log.info("Rejecting command {}", command);
            return tasksDao.updateTaskStatus(buildUpdateTaskStatusCommand(command))
                    .then(Mono.fromCallable(() -> toCacheTask(command)))
                    .flatMap(task -> Mono.fromCompletionStage(tasksCache.putAsync(task.id(), task)))
                    .then(Mono.error(new RuntimeException("Simulated exception. This is expected behaviour")));
        }

        return taskCommandProcessor.process(command);
    }

    private UpdateTaskStatusCommand buildUpdateTaskStatusCommand(ExecuteTaskCommand executeTaskCommand) {
        return new UpdateTaskStatusCommand(
                executeTaskCommand.id(),
                TaskStatusCode.REJECTED,
                null,
                null,
                null
        );
    }

    private Task toCacheTask(ExecuteTaskCommand command) {
        return new Task(
                command.id(),
                command.input(),
                command.pattern(),
                new TaskStatus(
                        TaskStatusCode.REJECTED,
                        null
                ),
                null
        );
    }

}
