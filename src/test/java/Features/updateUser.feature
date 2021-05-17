Feature: PUT: Update User
  Description: PUT: The purpose of this feature is to update user

  @smoke
  Scenario Outline: PUT: Update User
    Given I want to set URL as "<URL>" for test case "<TestName>"
    When I set header content type as "<ContentType>"
    When I hit the API with requestbody "<RequestBody>" and request method is "<RequestMethod>"
    Then I try to verify the status code is "<StatusCode>"
    And I try to verify the response value "name" is "morpheus"
    And I try to verify the response value "job" is "zion resident"

    Examples: 
      | TestName    | URL          | ContentType      | RequestBody         				 | RequestMethod | StatusCode |
      | Update User | /api/users/2 | application/json | testdata/updateUserData.json | PUT           |        201 |