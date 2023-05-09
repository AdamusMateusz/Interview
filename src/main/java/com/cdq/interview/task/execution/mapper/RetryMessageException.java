package com.cdq.interview.task.execution.mapper;

public class RetryMessageException extends RuntimeException{

    public RetryMessageException(String message) {
        super(message);
    }
}
