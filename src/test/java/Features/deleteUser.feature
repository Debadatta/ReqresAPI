Feature: DELETE: Delete User
  Description: DELETE: The purpose of this feature is to delete user

  @smoke
  Scenario Outline: DELETE: Delete User
    Given I want to set URL as "<URL>" for test case "<TestName>"
    When I set header content type as "<ContentType>"
    When I hit the API with requestbody "<RequestBody>" and request method is "<RequestMethod>"
    Then I try to verify the status code is "<StatusCode>"

    Examples: 
      | TestName  | URL          | ContentType      | RequestBody | RequestMethod | StatusCode |
      | Delete User | /api/users/2 | application/json |           | DELETE        |        204 |