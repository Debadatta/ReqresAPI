Feature: GET: Show User
  Description: GET: purpose of this feature is to view single user
  
  
	@smoke
  Scenario Outline: GET: view a Single User
    Given I want to set URL as "<URL>" for test case "<TestName>"
    When I set header content type as "<ContentType>"
    When I hit the API with requestbody "<RequestBody>" and request method is "<RequestMethod>"
    Then I try to verify the status code is "<StatusCode>"
    And I try to verify the response of "email" is "janet.weaver@reqres.in"
    And I try to verify the response of "first_name" is "Janet"
    And I try to verify the response of "last_name" is "Weaver"

    Examples: 
      | TestName         | URL          | ContentType      | RequestBody | RequestMethod | StatusCode |
      | View Single User | /api/users/2 | application/json |             | GET           |        200 |