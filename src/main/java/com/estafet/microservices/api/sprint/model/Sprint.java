package com.estafet.microservices.api.sprint.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "SPRINT")
public class Sprint {

	@Id
	@SequenceGenerator(name = "SPRINT_ID_SEQ", sequenceName = "SPRINT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPRINT_ID_SEQ")
	@Column(name = "SPRINT_ID")
	private Integer id;

	@Column(name = "START_DATE", nullable = false)
	private String startDate;

	@Column(name = "END_DATE", nullable = false)
	private String endDate;

	@Column(name = "SPRINT_NUMBER", nullable = false)
	private Integer number;

	@Column(name = "SPRINT_STATUS", nullable = false)
	private String status = "Not Started";

	@Column(name = "PROJECT_ID", nullable = false)
	private Integer projectId;

	@Column(name = "NO_DAYS", nullable = false)
	private Integer noDays;

	@JsonIgnore
	@OneToMany(mappedBy = "storySprint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Story> stories = new HashSet<Story>();

	public Sprint update(Sprint newSprint) {
		status = newSprint.getStatus() != null ? newSprint.getStatus() : status;
		return this;
	}

	public Sprint start(int projectId, int days) {
		return start(projectId, days, new ArrayList<Sprint>());
	}
	
	public Sprint start(int projectId, int days, List<Sprint> projectSprints) {
		if ("Not Started".equals(status)) {
			this.startDate = projectSprints.isEmpty() ? toCalendarString(newCalendar())
					: getLastSprint(projectSprints).endDate;
			Calendar cal = toCalendar(startDate);
			for (int i = 0; i < days; i++)
				do {
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} while (!isWorkingDay(cal));
			this.endDate = toCalendarString(cal);
			this.status = "Active";
			this.noDays = days;
			this.number = projectSprints.size() + 1;
			this.projectId = projectId;
			return this;
		}
		throw new RuntimeException("Canot start a sprint that has already started.");

	}

	private Sprint getLastSprint(List<Sprint> projectSprints) {
		Collections.sort(projectSprints, new Comparator<Sprint>() {
			@Override
			public int compare(Sprint o1, Sprint o2) {
				return toCalendar(o2.getEndDate()).compareTo(toCalendar(o1.getEndDate()));
			}
		});
		return projectSprints.get(0);
	}

	public void addStory(Story story) {
		story.setStorySprint(this);
		if (!stories.add(story)) {
			stories.remove(story);
			stories.add(story);
		}
		updateStatus();
	}

	private void updateStatus() {
		if (status.equals("Active") && !stories.isEmpty()) {
			for (Story story : stories) {
				if (!story.getStatus().equals("Completed")) {
					return;
				}
			}
			status = "Completed";
		}
	}

	@JsonIgnore
	public String getSprintDay() {
		String today = toCalendarString(newCalendar());
		for (String day : getSprintDays()) {
			if (day.equals(today)) {
				return today;
			}
		}
		return getSprintDays().get(0);
	}

	@JsonIgnore
	public List<String> getSprintDays() {
		List<String> workingDays = new ArrayList<String>(noDays);
		for (int i = 0; i < noDays; i++) {
			Calendar workDay = getWorkingDay(toCalendar(startDate));
			workDay.add(Calendar.DAY_OF_MONTH, i);
			workDay = getWorkingDay(workDay);
			workingDays.add(toCalendarString(workDay));
		}
		return workingDays;
	}

	private Calendar getWorkingDay(Calendar day) {
		if (isWorkingDay(day)) {
			return day;
		} else {
			day.add(Calendar.DAY_OF_MONTH, 1);
			return getWorkingDay(day);
		}
	}

	private Calendar toCalendar(String calendarString) {
		try {
			Calendar cal = newCalendar();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(calendarString));
			return cal;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private String toCalendarString(Calendar calendar) {
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

	public Sprint setId(Integer id) {
		this.id = id;
		return this;
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

	public String getStatus() {
		return status;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getNoDays() {
		return noDays;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Sprint getAPI() {
		Sprint sprint = new Sprint().start(1, 5);
		sprint.id = 1;
		return sprint;
	}
}
