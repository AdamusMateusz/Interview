package com.cqd.interview.task.dao.model;

import com.cqd.interview.task.model.api.TaskStatusCode;

public record UpdateTaskStatusCommand(
        String id,
        TaskStatusCode statusCode,
        Boolean isMatchFound,
        Integer position,
        Integer typos
) {
}
