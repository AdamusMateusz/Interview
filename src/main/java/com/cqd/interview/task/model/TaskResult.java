package com.cqd.interview.task.model;

public record TaskResult(
        Boolean isResultFound,
        Integer position,
        Integer typos) {
}
