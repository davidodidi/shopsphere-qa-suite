@uat
Feature: User Acceptance Testing - Core Business Flows
  As a business stakeholder
  I want to verify that core user journeys work end-to-end
  So that customers can successfully use the platform

  @uat
  Scenario: New customer can complete full shopping journey
    Given I am on the ShopSphere login page
    When I enter username "standard_user" and password "secret_sauce"
    And I click the login button
    Then I should be on the products page
    When I add "Sauce Labs Backpack" to the cart
    And I navigate to the cart
    Then the cart should contain 1 item
    When I proceed to checkout
    And I fill in checkout information with first name "David" last name "Odidi" and postal code "L6Y 5B7"
    And I continue to order review
    And I finish the order
    Then I should see the order confirmation message

  @uat
  Scenario: Customer can browse multiple products and view details
    Given I am logged in as "standard_user" with password "secret_sauce"
    And I am on the products page
    When I click on the first product
    Then I should be on the product detail page
    And the product name should not be empty
    When I go back to the products list
    Then I should be on the products page
