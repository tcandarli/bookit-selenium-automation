@login2 
Feature: Login 

Background: 
	Given the user is on the login page 
	
Scenario: Login as teacher 

	When the user logs in as a teacher 
	Then the user should be logged in 
	
Scenario: Login as a team lead 
	When the user logs in as a team lead 
	Then the user should be logged in 
	