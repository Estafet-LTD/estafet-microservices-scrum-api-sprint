package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.Tracer;

@Component
public class CompletedSprintConsumer {

	@Autowired
	private Tracer tracer;

	@Autowired
	private SprintService sprintService;

	@JmsListener(destination = "update.sprint.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		Sprint sprint = Sprint.fromJSON(message);
		try {
			if (sprint.getStatus().equals("Completed")) {
				sprintService.completedSprint(sprint.getId());
			}
		} finally {
			tracer.activeSpan().close();
		}
	}

}
