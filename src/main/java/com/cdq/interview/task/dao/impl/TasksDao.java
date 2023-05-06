package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.model.api.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface TasksDao {
    Flux<Task> findTasks(TasksQuery tasksQuery);

    Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand);

    Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command);
}
