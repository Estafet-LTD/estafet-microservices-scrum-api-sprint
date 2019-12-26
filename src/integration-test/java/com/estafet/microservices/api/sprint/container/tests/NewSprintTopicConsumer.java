package com.estafet.microservices.api.sprint.container.tests;

import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.scrum.lib.commons.jms.TopicConsumer;

public class NewSprintTopicConsumer extends TopicConsumer {

	public NewSprintTopicConsumer() {
		super("new.sprint.topic");
	}

	public Sprint consume() {
		return super.consume(Sprint.class);
	}

}
