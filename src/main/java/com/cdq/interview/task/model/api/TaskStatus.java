package com.cdq.interview.task.model.api;

public record TaskStatus(
        TaskStatusCode statusCode,
        Integer completionInPercent) {
}
