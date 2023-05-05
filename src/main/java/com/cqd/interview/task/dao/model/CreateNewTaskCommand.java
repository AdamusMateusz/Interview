package com.cqd.interview.task.dao.model;

import com.cqd.interview.task.model.api.TaskStatusCode;

public record CreateNewTaskCommand(
        String id,
        String input,
        String pattern,
        TaskStatusCode taskStatusCode
) {
}
