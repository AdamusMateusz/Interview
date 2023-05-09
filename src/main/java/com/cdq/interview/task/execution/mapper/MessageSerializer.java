package com.cdq.interview.task.execution.mapper;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageSerializer {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public byte[] fromCommandObject(ExecuteTaskCommand executeTaskCommand) {
        return objectMapper.writeValueAsBytes(executeTaskCommand);
    }

    @SneakyThrows
    public ExecuteTaskCommand toCommandObject(byte[] executeTaskCommand) {
        return objectMapper.readValue(executeTaskCommand, ExecuteTaskCommand.class);
    }


}
