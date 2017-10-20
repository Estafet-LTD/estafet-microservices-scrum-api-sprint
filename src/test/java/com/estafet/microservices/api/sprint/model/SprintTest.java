package com.estafet.microservices.api.sprint.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SprintTest {

	@Test
	public void testStartCalendarOverWeekend() {
		Sprint sprint = new Sprint().start(Sprint.toCalendar("2017-10-20 00:00:00"), 1, 3);
		assertEquals("2017-10-20 00:00:00", sprint.getStartDate());
		assertEquals("2017-10-24 00:00:00", sprint.getEndDate());
	}
	
	@Test
	public void testStartCalendarMidWeek() {
		Sprint sprint = new Sprint().start(Sprint.toCalendar("2017-10-16 00:00:00"), 1, 5);
		assertEquals("2017-10-16 00:00:00", sprint.getStartDate());
		assertEquals("2017-10-20 00:00:00", sprint.getEndDate());
	}

	@Test
	public void testGetSprintDaysMidWeek() {
		Sprint sprint = new Sprint().start(Sprint.toCalendar("2017-10-16 00:00:00"), 1, 5);
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
		Sprint sprint = new Sprint().start(Sprint.toCalendar("2017-10-20 00:00:00"), 1, 3);
		List<String> days = sprint.getSprintDays();
		assertEquals(3, days.size());
		assertEquals("2017-10-20 00:00:00", days.get(0));
		assertEquals("2017-10-23 00:00:00", days.get(1));
		assertEquals("2017-10-24 00:00:00", days.get(2));
	}

}
