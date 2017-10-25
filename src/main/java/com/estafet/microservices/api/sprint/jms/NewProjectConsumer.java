package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.model.Project;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class NewProjectConsumer {

	@Autowired
	private Tracer tracer;

	@Autowired
	private SprintService sprintService;

	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		ActiveSpan span = tracer.buildSpan("newProjectConsumer").startActive().log(message);
		try {
			sprintService.newProject(Project.fromJSON(message));
		} finally {
			span.close();
		}
	}

}
