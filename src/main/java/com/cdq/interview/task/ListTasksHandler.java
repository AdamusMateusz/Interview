package com.cdq.interview.task;

import com.cdq.interview.task.dao.LoggingTasksDao;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
@AllArgsConstructor
public class ListTasksHandler {

    private final LoggingTasksDao tasksRepository;

    public Mono<Collection<Task>> handle(TasksQuery tasksQuery) {
        return tasksRepository.findTasks(tasksQuery);
    }
}
