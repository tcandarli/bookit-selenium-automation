package com.bookit.step_definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.bookit.pages.SelfPage;
import com.bookit.pages.SignInPage;
import com.bookit.pages.TeamPage;
import com.bookit.utilities.BrowserUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import com.bookit.utilities.Driver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class MyInfoStepDefs {

	@Given("user logs in using {string} {string}")
	public void user_logs_in_using(String email, String password) {
		Driver.getDriver().get(ConfigurationReader.getProperty("url"));
		Driver.getDriver().manage().window().maximize();
		SignInPage signInPage = new SignInPage();
		signInPage.email.sendKeys(email);
		signInPage.password.sendKeys(password);
		signInPage.signInButton.click();
		BrowserUtils.waitFor(5);

	}

	@When("user is on the myself page")
	public void user_is_on_the_myself_page() {

		SelfPage selfPage = new SelfPage();
		selfPage.goToSelf();
		BrowserUtils.waitFor(3);

	}

	@Then("user info should match with the database records for {string}")
	public void user_info_should_match_with_the_database_records_for(String email) {

		// writing our query
		String query = "select firstname, lastname, role from users where email = '" + email + "'";

		// get value from database and assign to map
		Map<String, Object> result = DBUtils.getRowMap(query);

		// assigning database values to variables
		String expectedFirstName = (String) result.get("firstname");
		String expectedLastName = (String) result.get("lastname");
		String expectedRole = (String) result.get("role");

		String expectedFullName = expectedFirstName + " " + expectedLastName;

		System.out.println(expectedFirstName);
		System.out.println(expectedLastName);
		System.out.println(expectedRole);

		// ------------------------------------------------------

		SelfPage selfPage = new SelfPage();

		// wait until got the user information table
		BrowserUtils.waitFor(4);

		// getting values from UI and assigning to variables
		String actualFullName = selfPage.name.getText();
		String actualRole = selfPage.role.getText();

		System.out.println(actualFullName);
		System.out.println(actualRole);

		// compare UI and DATABASE values

		Assert.assertEquals(actualFullName, expectedFullName);
		Assert.assertEquals(actualRole, expectedRole);

	}

	
	@When("user is on the my team page")
	public void user_is_on_the_my_team_page() {
		
		SelfPage selfPage = new SelfPage();
		selfPage.goToTeam();
		BrowserUtils.waitFor(4);
		
	   
	}
	

	@Then("team info should match with the database records {string}")
	public void team_info_should_match_with_the_database_records(String email) {

		String query = "SELECT firstname, role FROM users\r\n" + 
				"WHERE team_id = (SELECT team_id FROM users WHERE email = '"+email+"')";

		// assign query result to list map
		List<Map<String, Object>> result = DBUtils.getQueryResultMap(query);

		// -----------------------------
		// import teampage and create teamPage object to reach names and roles from UI
		TeamPage teamPage = new TeamPage();

		// empty list for actual names
		List<String> actualNames = new ArrayList<>();

		for (WebElement el : teamPage.teamMemberNames) {
			// get each WebElement name and add to actualNames list
			actualNames.add(el.getText());
		}
		// empty list for actual roles
		List<String> actualRoles = new ArrayList<>();

		for (WebElement el : teamPage.teamMemberRoles) {
			// get each webelement roles and add to actualRoles list
			actualRoles.add(el.getText());
		}
		
		// before one by one comparison, check the number of results from DB and UI
		Assert.assertEquals(result.size(), actualNames.size());
		
		for(Map<String, Object> row : result) {
			
			String firstName = (String) row.get("firstname");
			String role = (String) row.get("role");
			Assert.assertTrue(actualNames.contains(firstName));
			Assert.assertTrue(actualRoles.contains(role));
			
		}
		
		
	}

}
