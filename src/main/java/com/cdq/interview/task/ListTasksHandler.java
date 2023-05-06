package com.cdq.interview.task;

import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class ListTasksHandler {

    private final TasksDao tasksDao;

    public Flux<Task> handle(TasksQuery tasksQuery) {
        return tasksDao.findTasks(tasksQuery);
    }
}
