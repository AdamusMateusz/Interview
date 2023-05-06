package com.cdq.interview.task.dao.model;

import com.cdq.interview.task.model.api.TaskStatusCode;

public record UpdateTaskStatusCommand(
        String id,
        TaskStatusCode statusCode,
        Boolean isMatchFound,
        Integer position,
        Integer typos
) {
}
