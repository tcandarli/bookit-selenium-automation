package com.bookit.tests;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static io.restassured.RestAssured.*;

public class API1 {

	String baseUrl = "http://18.206.61.190:1000/ords/hr";
	
	/*
	 * When I send a GET request to URL/employees 
	 * Then response STATUS code should be 200
	 */

	@Test
	public void simpleStatusCode() {

		when().get("http://18.206.61.190:1000/ords/hr/employees")
		.then().statusCode(200);
	}
	
	/* When I send a GET request to URL/countries
	 * Then I should see the JSON response
	 */
	
	@Test
	public void printResponse() {
		
		when().get("http://18.206.61.190:1000/ords/hr/countries")
		.body().prettyPrint();
	}
	
	 /* When I send a GET request to URL/countries
	   * And Accept type is "application/json"
	   * Then I should see the JSON response
	   */
	
	@Test
	public void getWithHeaders() {
		given().accept(ContentType.JSON)
		.when().get("http://18.206.61.190:1000/ords/hr/countries")
		.then().statusCode(200);
	}
	
	 /* When I send a GET request to URL/employees/1234
	   * Then response STATUS CODE should be 404
	   * 
	   */
	
	public void negativeGet() {
		
		when().get("http://18.206.61.190:1000/ords/hr/employees/1234")
		.then().statusCode(404);
	}
	
	/* When I send a GET request to URL/employees/1234
	   * Then response STATUS CODE should be 404
	   * And response body error message should  includes "Not Found"
	   */
	
	@Test
	public void negativeGetWithBody() {
		
		Response response = when().get("http://18.206.61.190:1000/ords/hr/employees/1234");
		
		// Check status code
		assertEquals(response.statusCode(), 404);
		
		// Check body includes not found
		assertTrue(response.asString().contains("Not Found"));
		
		// Print the response
		response.prettyPrint();
		
	}
	
	/* When I send a GET request to URL/employees/110 
	 * And accept type is JSON
	 * Then status code is 200
	 * And response content should be JSON
	 */
	
	
	@Test
	public void verifyContentType() {
		
		given().accept(ContentType.JSON)
		.when().get("http://18.206.61.190:1000/ords/hr/employees/110")
		.then().statusCode(200).and().contentType(ContentType.JSON);
		
	}
	
	/* Given Accept type is JSON
	 * When I send a GET request to URL/employees/110 
	 * Then status code is 200
	 * And response content should be JSON
	 * And first name should be "John"
	 */
	
	@Test
	public void verifyFirstName() {
		
		given().accept(ContentType.JSON)
		.when().get(baseUrl + "/employees/110")
		.then().assertThat().statusCode(200)
		.and().assertThat().contentType(ContentType.JSON)
		.and().assertThat().body("first_name", Matchers.equalTo("John"));
	}
	
	
	
	
	/* Given Accept Type is JSON
	 * When I send a GET request to url/employees/150
	 * Then status code is 200
	 * And response content should be JSON
	 * and first name should be "Tucker"
	 * and JOB_ID should be "SA_REP"
	 */
	
	String baseurl = "http://18.206.61.190:1000/ords/hr";
	
	@Test
	public void verifyLastName() {
		
		given().accept(ContentType.JSON)
		.when().get(baseurl+"/employees/150")
		.then().statusCode(200)
		.and().contentType(ContentType.JSON)
		.and().body("last_name",Matchers.equalTo("Tucker"))
		.and().body("job_id", Matchers.equalTo("SA_REP"));
	}
	
	/*
	 * When I send a GET request to url/regions
	 * Then status code is 200
	 * And response content should be JSON
	 * and 4 regions should be return
	 * and one region should be "Europe"
	 */
	
	@Test
	public void verifyRegionCount() {
		
		given().accept(ContentType.JSON)
		.when().get(baseurl+"/regions")
		.then().assertThat().statusCode(200)
		.and().assertThat().contentType(ContentType.JSON)
		.and().assertThat().body("items.region_id", Matchers.hasSize(9))
		.and().assertThat().body("items.region_name", Matchers.hasItem("Europe"))
		.and().body("items.region_name", Matchers.hasItems("Americas", "Middle East and Africa"));
	}
	
}
