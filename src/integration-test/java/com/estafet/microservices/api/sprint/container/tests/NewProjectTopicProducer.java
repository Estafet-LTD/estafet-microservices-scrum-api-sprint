package com.estafet.microservices.api.sprint.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class NewProjectTopicProducer extends TopicProducer {

	public NewProjectTopicProducer() {
		super("new.project.topic");
	}
	
	public static void send(String message) {
		new NewProjectTopicProducer().sendMessage(message);
	}

}
