package com.cdq.interview.task.model.api;

import java.io.Serializable;

public record TaskStatus(
        TaskStatusCode statusCode,
        Integer completionInPercent) implements Serializable {
}
