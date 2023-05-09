package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CachingTasksDao implements TasksDao {

    private final TasksDao tasksDao;
    private final IMap<String, Task> tasksCache;

    public CachingTasksDao(TasksDao tasksDao, HazelcastInstance hazelcastInstance) {
        this.tasksDao = tasksDao;
        tasksCache = hazelcastInstance.getMap("tasks");
    }

    @Override
    public Flux<Task> findTasks(TasksQuery tasksQuery) {

        if (tasksQuery.tasksIds().isEmpty()) {
            return tasksDao.findTasks(tasksQuery)
                    .flatMap(this::fillWithCacheChanges);
        }

        Flux<Task> taskFlux = Flux.fromIterable(tasksQuery.tasksIds())
                .flatMap(id -> Mono.fromCompletionStage(tasksCache.getAsync(id))
                        .flatMapMany(Flux::just)
                        .switchIfEmpty(tasksDao.findTasks(new TasksQuery(List.of(id), null, null)))
                );

        if (tasksQuery.limit() != null) {
            taskFlux = taskFlux.take(tasksQuery.limit());
        }

        if (tasksQuery.offset() != null) {
            taskFlux = taskFlux.skip(tasksQuery.offset());
        }

        return taskFlux;
    }

    @Override
    public Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand) {
        return tasksDao.createNewTask(createNewTaskCommand)
                .then(Mono.just(createNewTaskCommand))
                .map(command -> buildTaskFromCreateCommand(createNewTaskCommand))
                .flatMap(task -> Mono.fromCompletionStage(tasksCache.putAsync(task.id(), task)))
                .then();
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        return tasksDao.updateTaskStatus(command);
    }

    private Mono<Task> fillWithCacheChanges(Task task) {
        return Mono.just(task)
                .filter(t -> TaskStatusCode.STARTED.equals(task.taskStatus().statusCode()))
                .flatMap(t -> Mono.fromCompletionStage(tasksCache.getAsync(t.id())))
                .defaultIfEmpty(task);
    }

    private Task buildTaskFromCreateCommand(CreateNewTaskCommand createNewTaskCommand) {
        return new Task(
                createNewTaskCommand.id(),
                createNewTaskCommand.input(),
                createNewTaskCommand.pattern(),
                new TaskStatus(
                        TaskStatusCode.CREATED,
                        0
                ),
                null
        );
    }
}
