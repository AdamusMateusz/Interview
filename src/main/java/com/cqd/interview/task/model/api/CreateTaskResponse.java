package com.cqd.interview.task.model.api;

import jakarta.validation.constraints.NotNull;

public record CreateTaskResponse(
        @NotNull String input,
        @NotNull String pattern,
        @NotNull String id) {
}
