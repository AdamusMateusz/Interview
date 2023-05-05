package com.cqd.interview.task;

import com.cqd.interview.task.model.CreateTaskRequest;
import com.cqd.interview.task.model.ListTasksResponse;
import com.cqd.interview.task.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api")
public class TaskApi {

    @GetMapping("/tasks")
    public Mono<ResponseEntity<ListTasksResponse>> listTasks(@RequestParam(required = false) List<String> taskId,
                                                             @RequestParam(required = false) Integer limit,
                                                             @RequestParam(required = false) Integer offset) {
        log.info("List tasks with id {}, and limit {} with offset {}", taskId, limit, offset);
        return Mono.empty();
    }

    @PostMapping("/tasks/create-task-command")
    public Mono<ResponseEntity<Task>> createTask(@RequestBody @Validated CreateTaskRequest request){
        log.info("Create new task {}", request);
        return Mono.empty();
    }

}
