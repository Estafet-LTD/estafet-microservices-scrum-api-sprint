package com.estafet.microservices.api.sprint.jms;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.model.Story;
import com.estafet.microservices.api.sprint.service.SprintService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "newStoryConsumer")
public class NewStoryConsumer {

	@Autowired
	private SprintService sprintService;
	
	@Transactional
	public void onMessage(String message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Story story = mapper.readValue(message, Story.class);
			sprintService.addStory(story);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
