package com.cqd.interview.task.model;

public record Task(
        String id,
        String input,
        String pattern,
        TaskStatus taskStatus,
        TaskResult taskResult) {
}
