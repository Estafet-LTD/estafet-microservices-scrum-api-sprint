package com.estafet.microservices.api.sprint.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sprint {

	private Integer id;

	private String startDate;

	private String endDate;

	private Integer number;

	private List<Story> stories = new ArrayList<Story>();

	public Sprint start(int days) {
		startDate = calendarString(newCalendar());
		Calendar cal = newCalendar();
		int workingDaysToAdd = 5;
		for (int i = 0; i < workingDaysToAdd; i++)
			do {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			} while (!isWorkingDay(cal));
		endDate = calendarString(cal);
		return this;
	}

	private String calendarString(Calendar calendar) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	private static boolean isWorkingDay(Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
			return false;
		return true;
	}

	private Calendar newCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public Integer getId() {
		return id;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getNumber() {
		return number;
	}

	public List<Story> getStories() {
		return stories;
	}

}
