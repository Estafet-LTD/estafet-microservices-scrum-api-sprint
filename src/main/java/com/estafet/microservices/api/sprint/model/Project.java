package com.estafet.microservices.api.sprint.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

	private Integer id;

	private Integer noSprints;

	private Integer sprintLengthDays;

	public Integer getId() {
		return id;
	}

	public Integer getNoSprints() {
		return noSprints;
	}

	public Integer getSprintLengthDays() {
		return sprintLengthDays;
	}
	
	public static Project fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, Project.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
