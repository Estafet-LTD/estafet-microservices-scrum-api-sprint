package com.estafet.microservices.api.sprint.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.estafet.microservices.api.sprint.jms.NewSprintProducer;
import com.estafet.microservices.api.sprint.jms.UpdateSprintProducer;
import com.estafet.microservices.api.sprint.model.Sprint;

@Repository
public class SprintDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UpdateSprintProducer updateSprintProducer;
	
	@Autowired
	private NewSprintProducer newSprintProducer;

	public Sprint getSprint(int sprintId) {
		return entityManager.find(Sprint.class, new Integer(sprintId));
	}

	public void delete(int sprintId) {
		Sprint sprint = getSprint(sprintId);
		entityManager.remove(sprint);
	}

	public Sprint create(Sprint sprint) {
		entityManager.persist(sprint);
		newSprintProducer.sendMessage(sprint);
		return sprint;
	}

	public Sprint update(Sprint sprint) {
		entityManager.merge(sprint);
		updateSprintProducer.sendMessage(sprint);
		return sprint;
	}

	public List<Sprint> getProjectSprints(Integer projectId) {
		return entityManager.createQuery("select s from Sprint s where s.projectId = " + projectId, Sprint.class)
				.getResultList();
	}
	
	public int deleteAll() {
		return entityManager.createQuery("DELETE FROM Sprint").executeUpdate();
	}

}
