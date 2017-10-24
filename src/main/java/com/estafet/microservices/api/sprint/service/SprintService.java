package com.estafet.microservices.api.sprint.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.dao.SprintDAO;
import com.estafet.microservices.api.sprint.model.Project;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.model.Story;

@Service
public class SprintService {

	@Autowired
	private SprintDAO sprintDAO;

	@Transactional
	public void addStory(Story story) {
		if (story.getSprintId() != null) {
			Sprint sprint = sprintDAO.getSprint(story.getSprintId());
			sprint.addStory(story);
			sprintDAO.update(sprint);
		}
	}

	@Transactional
	public void updateStory(Story story) {
		if (story.getSprintId() != null) {
			Sprint sprint = sprintDAO.getSprint(story.getSprintId());
			sprint.addStory(story);
			sprintDAO.update(sprint);
		}
	}
	
	@Transactional
	public void newProject(Project project) {
		Sprint sprint = new Sprint(project.getId(), project.getSprintLengthDays());
		for (int i=1; i < project.getNoSprints(); i++) {
			sprint.addSprint();
		}
		sprintDAO.create(sprint);
	}
	
	@Transactional
	public void completedSprint(int sprintId) {
		Sprint sprint = sprintDAO.getSprint(sprintId);
		if (sprint.getNext() != null) {
			sprintDAO.update(sprint.getNext().start());	
		}
	}

	@Transactional(readOnly = true)
	public List<Sprint> getProjectSprints(int projectId) {
		return sprintDAO.getProjectSprints(projectId);
	}

	@Transactional(readOnly = true)
	public Sprint getSprint(int sprintId) {
		return sprintDAO.getSprint(sprintId);
	}

	@Transactional(readOnly = true)
	public List<String> getSprintDays(int sprintId) {
		return sprintDAO.getSprint(sprintId).getSprintDays();
	}

	@Transactional(readOnly = true)
	public String getSprintDay(int sprintId) {
		return sprintDAO.getSprint(sprintId).getSprintDay();
	}

}
