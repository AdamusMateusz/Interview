package com.cdq.interview.task.dao.util;

import com.cdq.interview.task.dao.model.UpdateTaskStatusCommand;
import io.r2dbc.postgresql.api.PostgresqlStatement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateStatusStatementFactory {

    public PostgresqlStatement bind(UpdateTaskStatusCommand command, PostgresqlStatement statement) {
        PostgresqlStatement biding = statement.bind("$1", command.statusCode().toString());

        if (command.isMatchFound() == null) {
            biding = biding.bindNull("$2", Boolean.class);
        } else {
            biding = biding.bind("$2", command.isMatchFound());
        }

        if (command.isMatchFound() == null) {
            biding = biding.bindNull("$3", Integer.class);
        } else {
            biding = biding.bind("$3", command.position());
        }

        if (command.isMatchFound() == null) {
            biding = biding.bindNull("$4", Integer.class);
        } else {
            biding = biding.bind("$4", command.typos());
        }

        return biding.bind("$5", command.id());
    }

}
