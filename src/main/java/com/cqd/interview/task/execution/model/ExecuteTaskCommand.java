package com.cqd.interview.task.execution.model;

public record ExecuteTaskCommand(
        String id,
        String input,
        String pattern
) {
}
