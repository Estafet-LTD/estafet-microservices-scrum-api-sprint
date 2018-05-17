package com.estafet.microservices.api.sprint.container.tests;

public class NewProjectTopicProducer extends TopicProducer {

	public NewProjectTopicProducer() {
		super("new.project.topic");
	}
	
	public static void send(String message) {
		new NewProjectTopicProducer().sendMessage(message);
	}

}
