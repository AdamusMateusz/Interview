package com.cqd.interview.task.dao;

import com.cqd.interview.task.dao.model.CreateNewTaskCommand;
import com.cqd.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cqd.interview.task.model.TasksQuery;
import com.cqd.interview.task.model.api.Task;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TasksDao {
    Mono<Collection<Task>> findTasks(TasksQuery tasksQuery);

    Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand);

    Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command);
}
