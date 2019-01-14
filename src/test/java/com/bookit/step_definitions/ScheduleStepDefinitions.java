package com.bookit.step_definitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ScheduleStepDefinitions {
	
	@When("Go to my schedule")
	public void go_to_my_schedule() {
	    System.out.println("Go to my schedule");
	}

	@Then("I should be able to see the reservations for my team")
	public void i_should_be_able_to_see_the_reservations_for_my_team() {
	    System.out.println("I am able to see the reservations");
	}

}
