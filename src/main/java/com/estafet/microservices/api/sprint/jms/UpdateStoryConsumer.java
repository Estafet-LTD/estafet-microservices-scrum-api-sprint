package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.model.Story;
import com.estafet.microservices.api.sprint.service.SprintService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@Component
public class UpdateStoryConsumer {

	@Autowired
	private Tracer tracer;
	
	@Autowired
	private SprintService sprintService;

	@Transactional
	@JmsListener(destination = "update.story.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		ActiveSpan span = tracer.activeSpan().log(message);
		try {
			sprintService.updateStory(Story.fromJSON(message));
		} finally {
			span.close();
		}
	}

}
