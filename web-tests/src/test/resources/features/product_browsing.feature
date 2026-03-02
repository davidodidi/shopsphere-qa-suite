@smoke @regression
Feature: Product Browsing
  As a ShopSphere customer
  I want to browse and sort products
  So that I can find what I want to buy

  Background:
    Given I am logged in as "standard_user" with password "secret_sauce"
    And I am on the products page

  @smoke
  Scenario: Products page displays items
    Then the products page should be loaded
    And I should see at least 1 product

  @regression
  Scenario: Sort products by price low to high
    When I sort products by "Price (low to high)"
    Then products should be sorted by price in ascending order

  @regression
  Scenario: Sort products by name A to Z
    When I sort products by "Name (A to Z)"
    Then products should be sorted alphabetically in ascending order

  @regression
  Scenario: View product detail page
    When I click on the first product
    Then I should be on the product detail page
    And the product name should not be empty
    And the product price should not be empty

  @regression
  Scenario: Add product to cart from listing
    When I add "Sauce Labs Backpack" to the cart
    Then the cart count should be 1
