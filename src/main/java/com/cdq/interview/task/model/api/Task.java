package com.cdq.interview.task.model.api;

import java.io.Serializable;

public record Task(
        String id,
        String input,
        String pattern,
        TaskStatus taskStatus,
        TaskResult taskResult) implements Serializable {
}
