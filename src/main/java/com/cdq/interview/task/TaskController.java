package com.cdq.interview.task;

import com.cdq.interview.task.model.CreateTaskCommand;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.CreateTaskRequest;
import com.cdq.interview.task.model.api.ListTasksResponse;
import com.cdq.interview.task.model.api.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Profile("api")
@RestController
@AllArgsConstructor
@RequestMapping("api")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public Mono<ResponseEntity<ListTasksResponse>> listTasks(@RequestParam(required = false) List<String> taskId,
                                                             @RequestParam(required = false) Integer limit,
                                                             @RequestParam(required = false) Integer offset) {
        log.info("List tasks with id {}, and limit {} with offset {}", taskId, limit, offset);

        List<String> taskIds = taskId;
        Integer queryOffset = offset;

        if (taskIds == null) {
            taskIds = List.of();
        }

        if (queryOffset == null) {
            queryOffset = 0;
        }

        var tasksQuery = new TasksQuery(taskIds, limit, queryOffset);

        return taskService.listTasks(tasksQuery)
                .collectList()
                .map(ListTasksResponse::new)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/tasks/create-task-command")
    public Mono<ResponseEntity<Task>> createTask(@RequestBody @Validated CreateTaskRequest request) {
        log.info("Create new task {}", request);

        var taskCommand = new CreateTaskCommand(request.input(), request.pattern());

        return taskService.createNewTask(taskCommand)
                .map(response -> ResponseEntity.accepted().body(response));
    }

}
