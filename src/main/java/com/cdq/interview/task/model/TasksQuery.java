package com.cdq.interview.task.model;

import java.util.List;

public record TasksQuery(
        List<String> tasksId,
        Integer limit,
        Integer offset) {
}