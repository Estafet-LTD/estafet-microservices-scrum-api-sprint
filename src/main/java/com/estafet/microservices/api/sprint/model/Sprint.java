package com.estafet.microservices.api.sprint.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	@OneToOne(mappedBy = "previous", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Sprint next;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "PREVIOUS_SPRINT_ID", referencedColumnName = "SPRINT_ID")
	private Sprint previous;

	@JsonIgnore
	@OneToMany(mappedBy = "storySprint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Story> stories = new HashSet<Story>();

	public Sprint update(Sprint newSprint) {
		status = newSprint.getStatus() != null ? newSprint.getStatus() : status;
		return this;
	}

	public Sprint() {
	}


	public Sprint(Integer projectId, Integer noDays) {
		this(null, toCalendarString(newCalendar()), projectId, noDays);
	}	
	
	public Sprint(String startDate, Integer projectId, Integer noDays) {
		this(null, startDate, projectId, noDays);
	}
	
	public Sprint(Sprint previous, String startDate, Integer projectId, Integer noDays) {
		this.projectId = projectId;
		this.noDays = noDays;
		this.startDate = previous == null ? startDate : getNextWorkingDay(increment(previous.endDate));
		this.endDate = calculateEndDate();
		this.number = previous == null ? 1 : previous.number + 1;
		this.previous = previous;
		if (previous != null) {
			this.previous.next = this;	
		}
	}

	public Sprint addSprint() {
		return new Sprint(getLastSprint(), startDate, projectId, noDays);
	}

	private Sprint getLastSprint() {
		if (next != null) {
			return next.getLastSprint();
		} else {
			return this;
		}
	}

	private String calculateEndDate() {
		String day = getNextWorkingDay(startDate);
		int i = 1;
		while (i < noDays) {
			day = getNextWorkingDay(increment(day));
			i++;
		}
		return day;
	}

	public Sprint start() {
		if ("Not Started".equals(status)) {
			if (previous != null && previous.getStatus().equals("Active")) {
				throw new RuntimeException("Cannot start a new sprint when one is active");
			}
			this.status = "Active";
			return this;
		} else {
			throw new RuntimeException("Canot start a sprint that has already started.");	
		}
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
	public Sprint getNext() {
		return next;
	}

	@JsonIgnore
	public Sprint getPrevious() {
		return previous;
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
		String day = getNextWorkingDay(startDate);
		int i = 0;
		while (i < noDays) {
			workingDays.add(day);
			day = getNextWorkingDay(increment(day));
			i++;
		}
		return workingDays;
	}

	private String increment(String day) {
		Calendar cal = toCalendar(day);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return toCalendarString(cal);
	}

	private String getNextWorkingDay(String day) {
		return toCalendarString(getWorkingDay(toCalendar(day)));
	}

	private Calendar getWorkingDay(Calendar day) {
		if (isWorkingDay(day)) {
			return day;
		} else {
			day.add(Calendar.DAY_OF_MONTH, 1);
			return getWorkingDay(day);
		}
	}

	static Calendar toCalendar(String calendarString) {
		try {
			Calendar cal = newCalendar();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(calendarString));
			return cal;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	static String toCalendarString(Calendar calendar) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	private static boolean isWorkingDay(Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
			return false;
		return true;
	}

	static Calendar newCalendar() {
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
	
	public static Sprint fromJSON(String message) {
		try {
			return new ObjectMapper().readValue(message, Sprint.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Sprint getAPI() {
		Sprint sprint = new Sprint(1, 5);
		sprint.id = 1;
		return sprint;
	}
}
