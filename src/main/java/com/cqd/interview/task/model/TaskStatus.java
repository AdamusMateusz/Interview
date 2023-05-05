package com.cqd.interview.task.model;

public record TaskStatus(
        TaskStatusCode statusCode,
        Integer completionInPercent) {
}
