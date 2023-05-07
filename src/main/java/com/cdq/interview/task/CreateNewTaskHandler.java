package com.cdq.interview.task;

import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.execution.sender.TasksExecutionService;
import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.cdq.interview.task.model.CreateTaskCommand;
import com.cdq.interview.task.model.api.TaskStatusCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CreateNewTaskHandler {

    private final TasksExecutionService tasksExecutionService;
    private final TasksDao tasksDao;

    Mono<Void> handle(String taskId, CreateTaskCommand createTaskCommand) {
        var daoCommand = buildDaoCommand(taskId, createTaskCommand);
        var executionCommand = buildExecutionCommand(taskId, createTaskCommand);

        return tasksDao.createNewTask(daoCommand)
                .then(tasksExecutionService.queue(executionCommand));
    }

    private CreateNewTaskCommand buildDaoCommand(String taskId, CreateTaskCommand createTaskCommand) {
        return new CreateNewTaskCommand(
                taskId,
                createTaskCommand.input(),
                createTaskCommand.pattern(),
                TaskStatusCode.CREATED
        );
    }

    private ExecuteTaskCommand buildExecutionCommand(String taskId, CreateTaskCommand createTaskCommand) {
        return new ExecuteTaskCommand(
                taskId,
                createTaskCommand.input(),
                createTaskCommand.pattern()
        );
    }
}
