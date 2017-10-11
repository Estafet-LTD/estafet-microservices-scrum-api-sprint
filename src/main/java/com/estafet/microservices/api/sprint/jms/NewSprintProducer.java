package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.model.Sprint;

@Component
public class NewSprintProducer {

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(Sprint sprint) {
		jmsTemplate.convertAndSend("new.sprint.topic", sprint.toJSON());
	}
}
