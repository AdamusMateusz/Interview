package com.cqd.interview.task;

import com.cqd.interview.task.dao.LoggingTasksDao;
import com.cqd.interview.task.model.TasksQuery;
import com.cqd.interview.task.model.api.Task;
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
