package com.cqd.interview.task.model.api;

public record TaskResult(
        Boolean isResultFound,
        Integer position,
        Integer typos) {
}
