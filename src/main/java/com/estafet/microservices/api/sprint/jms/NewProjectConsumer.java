package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.event.MessageEventHandler;
import com.estafet.microservices.api.sprint.model.Project;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.Tracer;

@Component
public class NewProjectConsumer {

	public final static String TOPIC = "new.project.topic";
	
	@Autowired
	private Tracer tracer;

	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private MessageEventHandler messageEventHandler;

	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
		try {
			if (messageEventHandler.isValid(TOPIC, reference)) {
				sprintService.newProject(Project.fromJSON(message));	
			}
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}

}
