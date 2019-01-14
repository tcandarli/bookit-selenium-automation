Feature: View schedule 

@schedule1 
Scenario: My team schedule 
	Given the user is on the login page 
	And the user logs in as a teacher 
	When Go to my schedule 
	Then I should be able to see the reservations for my team 
	
@schedule2 
Scenario: My team schedule 
	Given the user is on the login page 
	And the user logs in as a teacher 
	When Go to my schedule 
	Then I should be able to see the reservations for my team