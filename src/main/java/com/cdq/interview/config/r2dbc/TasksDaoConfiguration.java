package com.cdq.interview.config.r2dbc;

import com.cdq.interview.task.dao.impl.CachingTasksDao;
import com.cdq.interview.task.dao.impl.LoggingTasksDao;
import com.cdq.interview.task.dao.impl.R2DBCTasksDao;
import com.cdq.interview.task.dao.impl.TasksDao;
import com.hazelcast.core.HazelcastInstance;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TasksDaoConfiguration {

    @Bean
    public TasksDao tasksDao(@Qualifier("connectionFactory") ConnectionFactory connectionFactory, HazelcastInstance hazelcastInstance) {
        R2DBCTasksDao r2DBCTasksDao = new R2DBCTasksDao(connectionFactory);

        CachingTasksDao cachingTasksDao = new CachingTasksDao(r2DBCTasksDao, hazelcastInstance);

        return new LoggingTasksDao(cachingTasksDao);
    }

}
