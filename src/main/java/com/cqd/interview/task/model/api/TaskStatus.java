package com.cqd.interview.task.model.api;

public record TaskStatus(
        TaskStatusCode statusCode,
        Integer completionInPercent) {
}
