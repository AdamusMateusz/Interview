package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.dao.util.FindTaskRowMapper;
import com.cdq.interview.task.dao.util.ListTasksStatementFactory;
import com.cdq.interview.task.dao.util.TasksDaoSQLCode;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlResult;
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
        return connectionFactory.create()
                .map(c -> c.createStatement(TasksDaoSQLCode.INSERT_TASK))
                .map(statement ->
                        statement.bind("$1", createNewTaskCommand.id())
                                .bind("$2", createNewTaskCommand.input())
                                .bind("$3", createNewTaskCommand.pattern())
                                .bind("$4", createNewTaskCommand.taskStatusCode().toString())
                )
                .flatMapMany(PostgresqlStatement::execute)
                .flatMap(PostgresqlResult::getRowsUpdated)
                .doOnNext(rows -> log.info("Inserted rows count: {}", rows))
                .then();
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        return connectionFactory.create()
                .map(c -> c.createStatement(TasksDaoSQLCode.UPDATE_TASK))
                .map(statement ->
                        statement.bind("$1", command.statusCode().toString())
                                .bind("$2", command.isMatchFound())
                                .bind("$3", command.position())
                                .bind("$4", command.typos())
                                .bind("$5", command.id())
                )
                .flatMapMany(PostgresqlStatement::execute)
                .flatMap(PostgresqlResult::getRowsUpdated)
                .doOnNext(rows -> log.info("Updated rows count: {}", rows))
                .then();
    }
}
