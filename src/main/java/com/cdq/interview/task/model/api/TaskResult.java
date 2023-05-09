package com.cdq.interview.task.model.api;

import java.io.Serializable;

public record TaskResult(
        Boolean isMatchFound,
        Integer position,
        Integer typos) implements Serializable {
}