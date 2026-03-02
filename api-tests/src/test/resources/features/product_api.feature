@api @regression
Feature: Product API - HTTP Protocol Validation
  As a QA Engineer
  I want to validate the Product REST API
  So that the SOA backend services meet contract requirements

  Background:
    Given the API base URL is configured
    And the Content-Type header is "application/json"

  @smoke @api
  Scenario: GET all products returns HTTP 200 with JSON body
    When I send a GET request to "/products"
    Then the response status code should be 200
    And the response Content-Type should be "application/json"
    And the response body should be a non-empty array
    And the response time should be less than 3000 milliseconds

  @api
  Scenario: GET product by valid ID returns product details
    Given a product with ID "1" exists
    When I send a GET request to "/products/1"
    Then the response status code should be 200
    And the response should contain field "id"
    And the response should contain field "title"
    And the response should contain field "price"

  @api
  Scenario: GET product by invalid ID returns HTTP 404
    When I send a GET request to "/products/0"
    Then the response status code should be 404

  @api
  Scenario: POST create product returns HTTP 201
    Given I am authenticated as admin
    When I send a POST request to "/products/add" with body:
      """
      {
        "title": "Test Product - Automation",
        "price": 29.99,
        "category": "laptops",
        "description": "Automation test product",
        "thumbnail": "https://dummyjson.com/icon/test/128"
      }
      """
    Then the response status code should be 201
    And the response should contain field "id"
    And the response should contain field "title"

  @api
  Scenario: PUT update product returns HTTP 200
    Given I am authenticated as admin
    And a product with ID "1" exists
    When I send a PUT request to "/products/1" with updated data
    Then the response status code should be 200

  @api
  Scenario: PATCH partial update product price
    Given I am authenticated as admin
    When I send a PATCH request to "/products/1" with body:
      """
      { "price": 39.99 }
      """
    Then the response status code should be 200
    And the response should contain field "price" with value "39.99"

  @api
  Scenario: DELETE product returns HTTP 200 or 204
    Given I am authenticated as admin
    When I send a DELETE request to "/products/1"
    Then the response status code should be one of 200, 204

  @api
  Scenario: HEAD request returns headers without body
    When I send a HEAD request to "/products"
    Then the response status code should be 200
    And the response body should be empty

  @api
  Scenario: GET products by category returns filtered results
    When I send a GET request to "/products/category/laptops"
    Then the response status code should be 200
    And the response Content-Type should be "application/json"
    And the response body should be a non-empty array
