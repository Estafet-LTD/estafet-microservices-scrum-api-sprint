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
		get("/sprint/1000/days").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body(hasItems(	"2017-10-02 00:00:00",
							"2017-10-03 00:00:00",
							"2017-10-04 00:00:00",
							"2017-10-05 00:00:00",
							"2017-10-06 00:00:00"));
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testGetSprintDay() {
		get("/sprint/1000/day").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body(is("2017-10-02 00:00:00"));
	}

	@Test
	@DatabaseSetup("ITSprintTest-data.xml")
	public void testCalculateSprints() {
		given().contentType(ContentType.JSON)
			.body("{\r\n" + 
					"	\"projectId\": 22,\r\n" + 
					"	\"noDays\": 3,\r\n" + 
					"	\"noSprints\": 3\r\n" + 
					"}")
		.when()
			.post("/calculate-sprints")
		.then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("startDate", hasSize(3))
			.body("endDate", hasSize(3))
			.body("number", hasItems(1, 2, 3));

	}

}
