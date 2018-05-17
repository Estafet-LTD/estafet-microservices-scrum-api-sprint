package com.estafet.microservices.api.sprint.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITSprintTest {

	@Before
	public void before() {
		RestAssured.baseURI = System.getenv("SPRINT_API_SERVICE_URI");
	}

	@After
	public void after() {
	}

	@Test
	public void testGetAPI() {
		get("/api").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(1))
			.body("startDate", is("2017-10-01 00:00:00"))
			.body("endDate", is("2017-10-06 00:00:00"))
			.body("number", is(1))
			.body("status", is("Completed"));
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testGetSprint() {
		get("/sprint/1000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(1000))
			.body("startDate", is("2017-10-01 00:00:00"))
			.body("endDate", is("2017-10-06 00:00:00"))
			.body("number", is(5))
			.body("status", is("Active"))
			.body("projectId", is(1));
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testGetProjectSprints() {
		get("/project/1/sprints").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", hasItems(1000, 1001))
			.body("startDate", hasItems("2017-10-01 00:00:00", "2016-10-01 00:00:00"))
			.body("endDate", hasItems("2017-10-06 00:00:00", "2016-10-06 00:00:00"))
			.body("number", hasItems(5, 5))
			.body("status", hasItems("Active", "Completed"));
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testGetSprintDays() {
		fail("Not yet implemented");
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testGetSprintDay() {
		fail("Not yet implemented");
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testCalculateSprints() {
		fail("Not yet implemented");
	}

}
