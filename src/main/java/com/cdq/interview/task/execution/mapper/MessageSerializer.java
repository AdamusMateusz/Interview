package com.cdq.interview.task.execution.mapper;

import com.cdq.interview.task.execution.model.ExecuteTaskCommand;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MessageSerializer {

    public byte[] fromCommandObject(ExecuteTaskCommand executeTaskCommand) {
        return new StringBuilder()
                .append(executeTaskCommand.id())
                .append("❤")
                .append(executeTaskCommand.input())
                .append("❤")
                .append(executeTaskCommand.pattern())
                .toString()
                .getBytes(StandardCharsets.UTF_8);
    }

    public ExecuteTaskCommand toCommandObject(byte[] executeTaskCommand) {
        String rawMessage = new String(executeTaskCommand, StandardCharsets.UTF_8);
        String[] items = rawMessage.split("❤");

        return new ExecuteTaskCommand(
                items[0],
                items[1],
                items[2]
        );
    }


}
