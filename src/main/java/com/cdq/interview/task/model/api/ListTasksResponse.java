package com.cdq.interview.task.model.api;

import java.util.Collection;

public record ListTasksResponse(Collection<Task> tasksList) {
}
