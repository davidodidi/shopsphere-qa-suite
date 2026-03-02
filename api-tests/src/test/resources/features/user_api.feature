@api @regression
Feature: User Service API Validation
  As a QA Engineer
  I want to validate User REST API endpoints
  So that user management SOA service is functioning correctly

  @smoke @api
  Scenario: Health check endpoint returns HTTP 200
    When I send a GET request to "/health"
    Then the response status code should be 200

  @api
  Scenario: Register new user via POST
    When I send a POST request to "/auth/register" with body:
      """
      {
        "firstName": "Test",
        "lastName": "Automation",
        "email": "automation.test@shopsphere.com",
        "password": "SecurePass@123",
        "username": "test_automation_user"
      }
      """
    Then the response status code should be one of 201, 409

  @api
  Scenario: Login with valid credentials returns token
    When I send a POST request to "/auth/login" with body:
      """
      {
        "username": "standard_user",
        "password": "secret_sauce"
      }
      """
    Then the response status code should be 200
    And the response should contain field "token"

  @api
  Scenario: Login with invalid credentials returns 401
    When I send a POST request to "/auth/login" with body:
      """
      {
        "username": "bad_user",
        "password": "wrong_pass"
      }
      """
    Then the response status code should be 401

  @api
  Scenario: GET user profile requires authentication
    When I send a GET request to "/users/1"
    Then the response status code should be 401

  @api
  Scenario: Authenticated GET user profile succeeds
    Given I am authenticated as admin
    When I send a GET request to "/users/1"
    Then the response status code should be 200
    And the response should contain field "id"
    And the response should contain field "email"
