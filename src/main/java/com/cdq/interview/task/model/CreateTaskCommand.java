package com.cdq.interview.task.model;

public record CreateTaskCommand(
        String input,
        String pattern) {
}
