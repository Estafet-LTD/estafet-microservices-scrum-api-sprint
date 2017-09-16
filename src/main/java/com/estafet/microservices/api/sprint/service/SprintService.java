package com.estafet.microservices.api.sprint.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.sprint.message.StartSprint;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SprintService {

	public Sprint startSprint(StartSprint message) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", message.getProjectId());
		Sprint sprint = template.postForObject("http://localhost:8080/project-repository/project/{id}/sprint",
				new Sprint().start(message.getNoDays()), Sprint.class, params);
		return getSprint(sprint.getId());
	}

	public void deleteSprint(int sprintId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", sprintId);
		template.delete("http://localhost:8080/project-repository/sprint/{id}", params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Story> getSprintStories(int sprintId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", sprintId);
		return template.getForObject("http://localhost:8080/story-repository/stories?sprintId={id}",
				new ArrayList().getClass(), params);
	}

	public Sprint getSprint(int sprintId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", sprintId);
		return template.getForObject("http://localhost:8080/project-repository/sprint/{id}", Sprint.class, params);
	}

	@SuppressWarnings("rawtypes")
	public List<Sprint> getProjectSprints(int projectId) {
		RestTemplate template = new RestTemplate();
		List objects = template.getForObject("http://localhost:8080/project-repository/project/{id}/sprints",
				List.class, projectId);
		List<Sprint> sprints = new ArrayList<Sprint>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Sprint sprint = mapper.convertValue(object, new TypeReference<Sprint>() {
			});
			sprints.add(sprint);
		}
		return sprints;
	}

}
