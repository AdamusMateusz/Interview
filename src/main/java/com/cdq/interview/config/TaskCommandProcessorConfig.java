package com.cdq.interview.config;

import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.execution.receiver.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class TaskCommandProcessorConfig {

    @Value("${cdq.processing.delay}")
    private Duration delay;

    @Bean
    public TaskCommandProcessor taskCommandProcessor(TasksDao tasksDao) {

        TaskCommandProcessor commandProcessor = new TaskCommandProcessorImpl(
                tasksDao
        );

        TaskCommandProcessor thirdPercentageIncrement = new PercentageCountingCommandProcessor(
                66,
                99,
                5,
                delay,
                commandProcessor
        );

        TaskCommandProcessor retryingProcessor = new RetryingCommandProcessor(
          new AtomicInteger(0),
          20,
          thirdPercentageIncrement
        );

        TaskCommandProcessor secondPercentageIncrement = new PercentageCountingCommandProcessor(
                33,
                66,
                5,
                delay,
                retryingProcessor
        );

        TaskCommandProcessor rejectingProcessor = new RejectingCommandProcessor(
                new AtomicInteger(0),
                100,
                secondPercentageIncrement
        );

        TaskCommandProcessor firstPercentageIncrement = new PercentageCountingCommandProcessor(
                0,
                33,
                5,
                delay,
                rejectingProcessor
                );

        return new LoggingTaskCommandProcessor(firstPercentageIncrement);
    }

}
