package com.estafet.microservices.api.sprint.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SprintTest {

	@Test
	public void testNewSprintOverWeekend() {
		Sprint sprint = new Sprint("2017-10-20 00:00:00", 1, 3);
		assertEquals("2017-10-20 00:00:00", sprint.getStartDate());
		assertEquals("2017-10-24 00:00:00", sprint.getEndDate());
	}
	
	@Test
	public void testNewSprintMidWeek() {
		Sprint sprint = new Sprint("2017-10-16 00:00:00", 1, 5);
		assertEquals("2017-10-16 00:00:00", sprint.getStartDate());
		assertEquals("2017-10-20 00:00:00", sprint.getEndDate());
	}
	
	@Test
	public void testChainSprintReferences() {
		Sprint sprint1 = new Sprint("2017-10-20 00:00:00", 1, 3);
		Sprint sprint2 = sprint1.addSprint();
		Sprint sprint3 = sprint2.addSprint();
		assertSame(sprint2, sprint1.getNext());
		assertNull(sprint1.getPrevious());
		assertSame(sprint3, sprint2.getNext());
		assertSame(sprint2, sprint3.getPrevious());
		assertNull(sprint3.getNext());
	}
	
	@Test
	public void testChainSprintReferencesAddToRoot() {
		Sprint sprint1 = new Sprint("2017-10-20 00:00:00", 1, 3);
		Sprint sprint2 = sprint1.addSprint();
		Sprint sprint3 = sprint1.addSprint();
		assertSame(sprint2, sprint1.getNext());
		assertNull(sprint1.getPrevious());
		assertSame(sprint3, sprint2.getNext());
		assertSame(sprint2, sprint3.getPrevious());
		assertNull(sprint3.getNext());
	}
	
	@Test
	public void testChainSprintDates() {
		Sprint sprint1 = new Sprint("2017-10-20 00:00:00", 1, 3);
		Sprint sprint2 = sprint1.addSprint();
		Sprint sprint3 = sprint2.addSprint();
		assertEquals("2017-10-20 00:00:00", sprint1.getStartDate());
		assertEquals("2017-10-24 00:00:00", sprint1.getEndDate());
		assertEquals("2017-10-25 00:00:00", sprint2.getStartDate());
		assertEquals("2017-10-27 00:00:00", sprint2.getEndDate());
		assertEquals("2017-10-30 00:00:00", sprint3.getStartDate());
		assertEquals("2017-11-01 00:00:00", sprint3.getEndDate());
	}

	@Test
	public void testGetSprintDaysMidWeek() {
		Sprint sprint = new Sprint("2017-10-16 00:00:00", 1, 5);
		List<String> days = sprint.getSprintDays();
		assertEquals(5, days.size());
		assertEquals("2017-10-16 00:00:00", days.get(0));
		assertEquals("2017-10-17 00:00:00", days.get(1));
		assertEquals("2017-10-18 00:00:00", days.get(2));
		assertEquals("2017-10-19 00:00:00", days.get(3));
		assertEquals("2017-10-20 00:00:00", days.get(4));
	}
	
	@Test
	public void testGetSprintDaysOverWeekend() {
		Sprint sprint = new Sprint("2017-10-20 00:00:00", 1, 3);
		List<String> days = sprint.getSprintDays();
		assertEquals(3, days.size());
		assertEquals("2017-10-20 00:00:00", days.get(0));
		assertEquals("2017-10-23 00:00:00", days.get(1));
		assertEquals("2017-10-24 00:00:00", days.get(2));
	}

}
