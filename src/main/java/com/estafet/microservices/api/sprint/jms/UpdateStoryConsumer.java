package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.model.Story;
import com.estafet.microservices.api.sprint.service.SprintService;

@Component
public class UpdateStoryConsumer {

	@Autowired
	private SprintService sprintService;

	@Transactional
	@JmsListener(destination = "update.story.topic", containerFactory = "myFactory")
	public void onMessage(Story story) {
		sprintService.updateStory(story);
	}

}
