package com.estafet.microservices.api.sprint.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "STORY")
public class Story implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3122234334138115967L;

	@Id
	@Column(name = "STORY_ID")
	private Integer id;

	@Column(name = "STATUS", nullable = false)
	private String status;

	@Transient
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
