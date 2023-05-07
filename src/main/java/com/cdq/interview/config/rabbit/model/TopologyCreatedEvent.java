package com.cdq.interview.config.rabbit.model;

import org.springframework.context.ApplicationEvent;

public class TopologyCreatedEvent extends ApplicationEvent {
    public TopologyCreatedEvent(Object source) {
        super(source);
    }
}
