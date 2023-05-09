package com.cdq.interview.task.dao.impl;

import com.cdq.interview.task.dao.model.CreateNewTaskCommand;
import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import com.cdq.interview.task.dao.util.FindTaskRowMapper;
import com.cdq.interview.task.dao.util.ListTasksStatementFactory;
import com.cdq.interview.task.dao.util.TasksDaoSQLCode;
import com.cdq.interview.task.dao.util.UpdateStatusStatementFactory;
import com.cdq.interview.task.model.TasksQuery;
import com.cdq.interview.task.model.api.Task;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.postgresql.api.PostgresqlStatement;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
public class R2DBCTasksDao implements TasksDao {

    private final PostgresqlConnectionFactory connectionFactory;

    public R2DBCTasksDao(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        this.connectionFactory = (PostgresqlConnectionFactory) connectionFactory;
    }

    @Override
    public Flux<Task> findTasks(TasksQuery tasksQuery) {
        return Flux.usingWhen(
                connectionFactory.create(),
                connection -> Flux.just(connection)
                        .map(c -> ListTasksStatementFactory.buildStatement(tasksQuery, c))
                        .flatMap(PostgresqlStatement::execute)
                        .flatMap(FindTaskRowMapper::map),
                PostgresqlConnection::close

        );
    }

    @Override
    public Mono<Void> createNewTask(CreateNewTaskCommand createNewTaskCommand) {
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.just(connection)
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
                        .then(),
                PostgresqlConnection::close
        );
    }

    @Override
    public Mono<Void> updateTaskStatus(UpdateTaskStatusCommand command) {
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.just(connection)
                        .map(c -> c.createStatement(TasksDaoSQLCode.UPDATE_TASK))
                        .map(statement -> UpdateStatusStatementFactory.bind(command, statement))
                        .flatMapMany(PostgresqlStatement::execute)
                        .flatMap(PostgresqlResult::getRowsUpdated)
                        .doOnNext(rows -> log.info("Updated rows count: {}", rows))
                        .then(),
                PostgresqlConnection::close
        );
    }
}
