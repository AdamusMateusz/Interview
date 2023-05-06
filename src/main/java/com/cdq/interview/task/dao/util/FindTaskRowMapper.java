package com.cdq.interview.task.dao.util;

import com.cdq.interview.task.model.api.Task;
import com.cdq.interview.task.model.api.TaskResult;
import com.cdq.interview.task.model.api.TaskStatus;
import com.cdq.interview.task.model.api.TaskStatusCode;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.spi.Row;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;

@UtilityClass
public class FindTaskRowMapper {

    public Flux<Task> map(PostgresqlResult result) {
        return result.map(
                (row, rowMetadata) ->
                        new Task(
                                row.get("task_id", String.class),
                                row.get("input", String.class),
                                row.get("pattern", String.class),
                                mapTaskStatus(row),
                                mapTaskResult(row)
                        )
        );
    }

    private static TaskStatus mapTaskStatus(Row row) {
        TaskStatusCode taskStatusCode = TaskStatusCode.valueOf(row.get("task_status_code", String.class));

        return new TaskStatus(
                taskStatusCode,
                resolvePercentage(taskStatusCode)
        );
    }

    private static TaskResult mapTaskResult(Row row) {
        return new TaskResult(
            row.get("is_match_found", Boolean.class),
            row.get("position", Integer.class),
            row.get("typos", Integer.class)
        );
    }

    private static Integer resolvePercentage(TaskStatusCode taskStatusCode) {
        return switch (taskStatusCode) {
            case CREATED -> 0;
            case ENDED -> 100;
            default -> null;
        };
    }

}
