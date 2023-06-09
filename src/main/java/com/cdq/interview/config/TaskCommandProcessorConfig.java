package com.cdq.interview.config;

import com.cdq.interview.task.dao.impl.TasksDao;
import com.cdq.interview.task.execution.receiver.TaskCommandProcessor;
import com.cdq.interview.task.execution.receiver.impl.*;
import com.cdq.interview.task.model.api.Task;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("worker")
@Configuration
public class TaskCommandProcessorConfig {

    @Value("${cdq.processing.delay}")
    private Duration delay;

    @Bean
    public TaskCommandProcessor taskCommandProcessor(TasksDao tasksDao, HazelcastInstance hazelcastInstance) {

        IMap<String, Task> tasksCache = hazelcastInstance.getMap("tasks");

        TaskCommandProcessor commandProcessor = new TaskCommandProcessorImpl(
                tasksDao,
                tasksCache
        );

        TaskCommandProcessor thirdPercentageIncrement = new PercentageCountingCommandProcessor(
                66,
                99,
                5,
                delay,
                commandProcessor,
                tasksCache
        );

        TaskCommandProcessor retryingProcessor = new RetryingCommandProcessor(
                new AtomicInteger(0),
                20,
                thirdPercentageIncrement,
                tasksDao,
                tasksCache
        );

        TaskCommandProcessor secondPercentageIncrement = new PercentageCountingCommandProcessor(
                33,
                66,
                5,
                delay,
                retryingProcessor,
                tasksCache
        );

        TaskCommandProcessor rejectingProcessor = new RejectingCommandProcessor(
                new AtomicInteger(0),
                100,
                secondPercentageIncrement,
                tasksDao,
                tasksCache
        );

        TaskCommandProcessor firstPercentageIncrement = new PercentageCountingCommandProcessor(
                0,
                33,
                5,
                delay,
                rejectingProcessor,
                tasksCache
        );

        return new LoggingTaskCommandProcessor(firstPercentageIncrement);
    }

}
