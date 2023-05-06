package com.cdq.interview.task;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.cdq.interview.task.model.CreateTaskCommand;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
@AllArgsConstructor
public class TaskService {

    private final ListTasksHandler listTasksHandler;
    private final CreateNewTaskHandler createNewTaskHandler;

    Mono<Collection<Task>> listTasks(TasksQuery tasksQuery) {
        return listTasksHandler.handle(tasksQuery);
    }

    Mono<Task> createNewTask(CreateTaskCommand createTaskCommand) {
        var taskId = NanoIdUtils.randomNanoId();

        return createNewTaskHandler.handle(taskId, createTaskCommand)
                .thenReturn(buildResult(taskId, createTaskCommand));
    }

    private Task buildResult(String taskId, CreateTaskCommand createTaskCommand) {
        return new Task(
                taskId,
                createTaskCommand.input(),
                createTaskCommand.pattern(),
                new TaskStatus(
                        TaskStatusCode.CREATED,
                        0
                ),
                null
        );
    }
}
