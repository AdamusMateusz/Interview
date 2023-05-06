package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.dao.util.FindTaskRowMapper;
import com.cdq.interview.task.dao.util.ListTasksStatementFactory;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlStatement;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class R2DBCTasksDao implements TasksDao {

    private final PostgresqlConnectionFactory connectionFactory;

    public R2DBCTasksDao(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        this.connectionFactory = (PostgresqlConnectionFactory) connectionFactory;
    }

    @Override
    public Flux<Task> findTasks(TasksQuery tasksQuery) {
        return connectionFactory.create()
                .map(c -> ListTasksStatementFactory.buildStatement(tasksQuery, c))
                .flatMapMany(PostgresqlStatement::execute)
                .flatMap(FindTaskRowMapper::map);
    }

    @Override
    public Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand) {
        return null;
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        return null;
    }
}
