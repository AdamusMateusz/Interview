package com.cdq.interview.task.dao.util;

import com.cdq.interview.task.model.TasksQuery;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlStatement;

public class ListTasksStatementFactory {
    public static PostgresqlStatement buildStatement(TasksQuery tasksQuery, PostgresqlConnection c) {
        String sql = ListTasksDaoSQLFactory.fromQuery(tasksQuery);
        PostgresqlStatement statement = c.createStatement(sql);

        if (!tasksQuery.tasksIds().isEmpty()) {
            for (int i = 0; i < tasksQuery.tasksIds().size(); i++) {
                statement = statement.bind("$" + (i + 3), tasksQuery.tasksIds().get(i));
            }
        }

        if (tasksQuery.limit() != null) {
            statement = statement.bind("$1", tasksQuery.limit());
        }

        if (tasksQuery.offset() != null) {
            statement = statement.bind("$2", tasksQuery.offset());
        }

        return statement;
    }
}
