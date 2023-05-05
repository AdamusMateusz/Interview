package com.cqd.interview.task.model;

import java.util.Collection;

public record ListTasksResponse(Collection<Task> tasksList) {
}
