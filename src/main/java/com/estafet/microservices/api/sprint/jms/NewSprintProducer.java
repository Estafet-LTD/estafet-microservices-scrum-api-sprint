package com.estafet.microservices.api.sprint.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.support.JmsGatewaySupport;

import com.estafet.microservices.api.sprint.model.Sprint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class NewSprintProducer extends JmsGatewaySupport {

	public void sendMessage(final Sprint sprint) {
		getJmsTemplate().send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				try {
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = ow.writeValueAsString(sprint);
					TextMessage message = session.createTextMessage(json);
					return message;
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}
