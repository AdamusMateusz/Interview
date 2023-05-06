package com.cdq.interview.task.model.api;

public record TaskResult(
        Boolean isResultFound,
        Integer position,
        Integer typos) {
}
