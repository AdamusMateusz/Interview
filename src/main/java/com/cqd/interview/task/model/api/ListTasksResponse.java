package com.cqd.interview.task.model.api;

import java.util.Collection;

public record ListTasksResponse(Collection<Task> tasksList) {
}
