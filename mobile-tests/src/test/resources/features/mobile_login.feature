@smoke @sanity @regression @mobile
Feature: User Authentication
  As a ShopSphere customer
  I want to log into my account
  So that I can access my personalised shopping experience
  Background:
    Given I am on the ShopSphere login page
  @smoke @mobile
  Scenario: Successful login with valid credentials
    When I enter username "standard_user" and password "secret_sauce"
    And I click the login button
    Then I should be redirected to the products page
    And the page title should be "Products"
  @sanity @mobile
  Scenario: Login fails with invalid username
    When I enter username "invalid_user" and password "secret_sauce"
    And I click the login button
    Then I should see the error message "Epic sadface: Username and password do not match any user in this service"
  @sanity @mobile
  Scenario: Login fails with invalid password
    When I enter username "standard_user" and password "wrong_password"
    And I click the login button
    Then I should see an error message containing "do not match"
  @regression @mobile
  Scenario: Locked out user cannot login
    When I enter username "locked_out_user" and password "secret_sauce"
    And I click the login button
    Then I should see the error message "Epic sadface: Sorry, this user has been locked out."
  @regression @mobile
  Scenario Outline: Login validation with multiple user types
    When I enter username "<username>" and password "<password>"
    And I click the login button
    Then I should be on page "<expected_page>"
    Examples:
      | username      | password     | expected_page |
      | standard_user | secret_sauce | products      |
      | problem_user  | secret_sauce | products      |
