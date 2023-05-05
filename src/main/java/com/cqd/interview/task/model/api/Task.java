package com.cqd.interview.task.model.api;

public record Task(
        String id,
        String input,
        String pattern,
        TaskStatus taskStatus,
        TaskResult taskResult) {
}
