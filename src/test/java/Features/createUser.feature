Feature: POST: Create User
  Description: POST: The purpose of this feature is to create user

  @smoke
  Scenario Outline: POST: Create User
    Given I want to set URL as "<URL>" for test case "<TestName>"
    When I set header content type as "<ContentType>"
    When I hit the API with requestbody "<RequestBody>" and request method is "<RequestMethod>"
    Then I try to verify the status code is "<StatusCode>"
    And I try to verify the content type contains "<ContentType>"
    And I try to verify the response value "name" is "morpheus"
    And I try to verify the response value "job" is "leader"
    #And I try to verify the response value "id" is "55"

    Examples: 
      | TestName    | URL        | ContentType      | RequestBody        | RequestMethod | StatusCode |
      | Create User | /api/users | application/json | Testdata/test.json | POST          |        201 |
      
      
      
  @smoke @failScenario
  Scenario Outline: POST: Create User
    Given I want to set URL as "<URL>" for test case "<TestName>"
    When I set header content type as "<ContentType>"
    When I hit the API with requestbody "<RequestBody>" and request method is "<RequestMethod>"
    Then I try to verify the status code is "<StatusCode>"
    And I try to verify the content type contains "<ContentType>"
    And I try to verify the response value "name" is "morpheus"
    And I try to verify the response value "job" is "manager"

    Examples: 
      | TestName    | URL        | ContentType      | RequestBody        | RequestMethod | StatusCode |
      | Create User | /api/users | application/json | Testdata/test.json | POST          |        201 |