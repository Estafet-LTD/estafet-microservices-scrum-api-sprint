package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.event.MessageEventHandler;
import com.estafet.microservices.api.sprint.model.Story;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.Tracer;

@Component
public class UpdateStoryConsumer {

	public final static String TOPIC = "update.story.topic";
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private MessageEventHandler messageEventHandler;

	@Transactional
	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
		try {
			if (messageEventHandler.isValid(TOPIC, reference)) {
				sprintService.updateStory(Story.fromJSON(message));
			}
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}

}
