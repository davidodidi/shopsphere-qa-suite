@api @regression
Feature: User Service API Validation
  As a QA Engineer
  I want to validate User REST API endpoints
  So that user management SOA service is functioning correctly

  Background:
    Given the API base URL is configured
    And the Content-Type header is "application/json"

  @smoke @api
  Scenario: GET all users returns HTTP 200
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response Content-Type should be "application/json"
    And the response body should be a non-empty array

  @api
  Scenario: GET user by valid ID returns user details
    When I send a GET request to "/users/1"
    Then the response status code should be 200
    And the response should contain field "id"
    And the response should contain field "email"
    And the response should contain field "username"

  @api
  Scenario: Login with valid credentials returns token
    When I send a POST request to "/auth/login" with body:
      """
      {
        "username": "emilys",
        "password": "emilyspass"
      }
      """
    Then the response status code should be 200
    And the response should contain field "token"

  @api
  Scenario: GET limited users list respects limit parameter
    When I send a GET request to "/users?limit=3"
    Then the response status code should be 200
    And the response body should be a non-empty array

  @api
  Scenario: GET users in ascending sort order
    When I send a GET request to "/users?sortBy=id&order=asc"
    Then the response status code should be 200
    And the response body should be a non-empty array

  @api
  Scenario: GET user by invalid ID returns HTTP 404
    When I send a GET request to "/users/0"
    Then the response status code should be 404
