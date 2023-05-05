package com.cqd.interview.task.dao;

import com.cqd.interview.task.dao.model.CreateNewTaskCommand;
import com.cqd.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cqd.interview.task.model.TasksQuery;
import com.cqd.interview.task.model.api.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Slf4j
@Component
public class LoggingTasksDao implements TasksDao {

    @Override
    public Mono<Collection<Task>> findTasks(TasksQuery tasksQuery) {
        log.info("Finding tasks {}", tasksQuery);
        return Mono.empty();
    }

    @Override
    public Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand) {
        log.info("Task created {}", createNewTaskCommand);
        return Mono.empty();
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        log.info("Task updated {}", command);
        return Mono.empty();
    }
}
