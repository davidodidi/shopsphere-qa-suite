@e2e @regression @mobile
Feature: End-to-End Checkout Flow
  As a ShopSphere customer
  I want to complete a purchase
  So that I can receive my ordered items
  Background:
    Given I am logged in as "standard_user" with password "secret_sauce"
  @e2e @smoke @mobile
  Scenario: Complete successful checkout flow
    Given I add "Sauce Labs Backpack" to the cart from the products page
    When I navigate to the cart
    And I proceed to checkout
    And I fill in checkout information with first name "David" last name "Odidi" and postal code "L6Y 5B7"
    And I continue to order review
    And I finish the order
    Then I should see the order confirmation message
  @e2e @mobile
  Scenario: Checkout fails without filling in required fields
    Given I add "Sauce Labs Backpack" to the cart from the products page
    When I navigate to the cart
    And I proceed to checkout
    And I click continue without filling in checkout information
    Then I should see an error message about required fields
  @e2e @mobile
  Scenario: Multiple items can be purchased together
    Given I add "Sauce Labs Backpack" to the cart from the products page
    And I add "Sauce Labs Bike Light" to the cart from the products page
    When I navigate to the cart
    Then the cart should contain 2 items
    When I proceed to checkout
    And I fill in checkout information with first name "David" last name "Odidi" and postal code "L6Y 5B7"
    And I continue to order review
    And I finish the order
    Then I should see the order confirmation message
