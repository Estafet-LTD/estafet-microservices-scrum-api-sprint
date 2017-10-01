package com.estafet.microservices.api.sprint.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.sprint.dao.SprintDAO;
import com.estafet.microservices.api.sprint.message.StartSprint;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.model.Story;

@Service
public class SprintService {

	@Autowired
	private SprintDAO sprintDAO;
	
	@Transactional
	public void addStory(Story story) {
		Sprint sprint = sprintDAO.getSprint(story.getSprintId());
		sprint.addStory(story);
		sprintDAO.update(sprint);
	}
	
	@Transactional
	public void updateStory(Story story) {
		Sprint sprint = sprintDAO.getSprint(story.getSprintId());
		sprint.addStory(story);
		sprintDAO.update(sprint);
	}

	@Transactional
	public Sprint startSprint(StartSprint message) {
		if (!hasActiveSprint(message.getProjectId())) {
			Sprint sprint = new Sprint().start(message.getNoDays());
			List<Sprint> projectSprints = getProjectSprints(message.getProjectId());
			sprint.setNumber(projectSprints.size() + 1);
			sprint.setProjectId(message.getProjectId());
			sprintDAO.create(sprint);
			return sprint;
		} else {
			throw new RuntimeException("Cannot start a new sprint when one is active");
		}
	}

	private boolean hasActiveSprint(int projectId) {
		for (Sprint sprint : getProjectSprints(projectId)) {
			if (sprint.getStatus().equals("Active")) {
				return true;
			}
		}
		return false;
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
