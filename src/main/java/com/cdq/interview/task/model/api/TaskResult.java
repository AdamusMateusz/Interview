package com.cdq.interview.task.model.api;

public record TaskResult(
        Boolean isMatchFound,
        Integer position,
        Integer typos) {
}
