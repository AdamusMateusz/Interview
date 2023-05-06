package com.cdq.interview.task.dao.model;

import com.cdq.interview.task.model.api.TaskStatusCode;

public record CreateNewTaskCommand(
        String id,
        String input,
        String pattern,
        TaskStatusCode taskStatusCode
) {
}
