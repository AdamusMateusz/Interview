package com.cqd.interview.task.model.api;

import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotNull String input,
        @NotNull String pattern) {
}
