package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class LoggingTasksDao implements TasksDao {

    private final TasksDao tasksDao;

    @Override
    public Flux<Task> findTasks(TasksQuery tasksQuery) {
        log.info("Finding tasks {}", tasksQuery);
        return tasksDao.findTasks(tasksQuery);
    }

    @Override
    public Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand) {
        return tasksDao.createNewTask(createNewTaskCommand)
                .then(
                        Mono.fromRunnable(() -> log.info("Task created {}", createNewTaskCommand))
                );
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        return tasksDao.updateTaskStatus(command)
                .then(
                        Mono.fromRunnable(() -> log.info("Task updated {}", command))
                );
    }
}
