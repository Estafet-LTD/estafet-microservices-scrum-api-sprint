package com.estafet.microservices.api.sprint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "STORY")
public class Story {

	@Id
	@Column(name = "STORY_ID")
	private Integer id;

	@Column(name = "STATUS", nullable = false)
	private String status;

	private Integer sprintId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "SPRINT_ID", nullable = false, referencedColumnName = "SPRINT_ID")
	private Sprint storySprint;

	public Story update(Story newStory) {
		status = newStory.getStatus() != null ? newStory.getStatus() : status;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public Sprint getStorySprint() {
		return storySprint;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	void setStorySprint(Sprint storySprint) {
		this.storySprint = storySprint;
	}

}
