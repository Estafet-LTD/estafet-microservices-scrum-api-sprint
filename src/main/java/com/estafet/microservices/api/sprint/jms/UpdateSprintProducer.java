package com.estafet.microservices.api.sprint.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.model.Sprint;

@Component
public class UpdateSprintProducer {

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(Sprint sprint) {
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.convertAndSend("update.sprint.topic", sprint.toJSON());
	}
}
