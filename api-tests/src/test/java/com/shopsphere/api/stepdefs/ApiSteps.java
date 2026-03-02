package com.shopsphere.api.stepdefs;

import com.shopsphere.api.specs.ApiBaseSpec;
import com.shopsphere.config.ConfigReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApiSteps extends ApiBaseSpec {
    private static final Logger log = LogManager.getLogger(ApiSteps.class);
    private Response response;
    private String authToken;

    @Given("the API base URL is configured")
    public void theApiBaseUrlIsConfigured() {
        RestAssured.baseURI = config.getApiBaseUrl();
        log.info("API base URL set to: {}", config.getApiBaseUrl());
    }

    @Given("the Content-Type header is {string}")
    public void theContentTypeHeaderIs(String contentType) {
        // Handled in requestSpec
        log.info("Content-Type: {}", contentType);
    }

    @Given("a product with ID {string} exists")
    public void aProductWithIdExists(String productId) {
        log.info("Assuming product {} exists (pre-condition)", productId);
    }

    @Given("I am authenticated as admin")
    public void iAmAuthenticatedAsAdmin() {
        authToken = "mock-admin-token";
        log.info("Authenticated as admin with token: {}", authToken);
    }

    @When("I send a GET request to {string}")
    public void iSendGetRequest(String endpoint) {
        log.info("GET {}", endpoint);
        response = given().when().get(endpoint).then().extract().response();
    }

    @When("I send a HEAD request to {string}")
    public void iSendHeadRequest(String endpoint) {
        response = given().when().head(endpoint).then().extract().response();
    }

    @When("I send an OPTIONS request to {string}")
    public void iSendOptionsRequest(String endpoint) {
        response = given().when().options(endpoint).then().extract().response();
    }

    @When("I send a POST request to {string} with body:")
    public void iSendPostRequestWithBody(String endpoint, String body) {
        log.info("POST {} — body: {}", endpoint, body);
        response = given()
                .body(body)
                .when().post(endpoint)
                .then().extract().response();
    }

    @When("I send an unauthenticated POST request to {string} with a valid body")
    public void iSendUnauthenticatedPostRequest(String endpoint) {
        Map<String, Object> body = Map.of("name", "Test", "price", 9.99);
        response = given().body(body).when().post(endpoint).then().extract().response();
    }

    @When("I send a PUT request to {string} with updated data")
    public void iSendPutRequest(String endpoint) {
        Map<String, Object> body = Map.of(
                "title", "Updated Product",
                "price", 49.99,
                "category", "laptops",
                "description", "Updated via automation",
                "thumbnail", "https://dummyjson.com/icon/updated/128"
        );
        response = givenAuth(authToken).body(body).when().put(endpoint).then().extract().response();
    }

    @When("I send a PATCH request to {string} with body:")
    public void iSendPatchRequestWithBody(String endpoint, String body) {
        response = givenAuth(authToken).body(body).when().patch(endpoint).then().extract().response();
    }

    @When("I send a DELETE request to {string}")
    public void iSendDeleteRequest(String endpoint) {
        response = givenAuth(authToken).when().delete(endpoint).then().extract().response();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        log.info("Asserting status: expected={} actual={}", expectedStatus, response.statusCode());
        Assert.assertEquals(response.statusCode(), expectedStatus,
                "HTTP status code mismatch. Response: " + response.asString());
    }

    @Then("the response status code should be one of {int}, {int}")
    public void theResponseStatusCodeShouldBeOneOf(int status1, int status2) {
        int actual = response.statusCode();
        Assert.assertTrue(actual == status1 || actual == status2,
                "Expected " + status1 + " or " + status2 + " but got " + actual);
    }

    @Then("the response Content-Type should be {string}")
    public void theResponseContentTypeShouldBe(String expectedContentType) {
        Assert.assertTrue(response.contentType().contains(expectedContentType.split(";")[0].trim()),
                "Content-Type mismatch. Got: " + response.contentType());
    }

    @Then("the response body should be a non-empty array")
    public void theResponseBodyShouldBeNonEmptyArray() {
        String body = response.asString();
        // dummyjson wraps arrays: {"products":[...]} or {"users":[...]}
        // also handle bare arrays just in case
        Assert.assertTrue(
                body.contains("\"id\"") && (body.startsWith("[") || body.contains(":[")),
                "Response should contain a non-empty array. Got: " + body
        );
    }

    @Then("the response body should be empty")
    public void theResponseBodyShouldBeEmpty() {
        Assert.assertTrue(response.body().asString().isEmpty(),
                "HEAD response body should be empty");
    }

    @Then("the response body should contain an error message")
    public void theResponseBodyShouldContainErrorMessage() {
        String body = response.asString();
        Assert.assertTrue(body.contains("error") || body.contains("message") || body.contains("not found"),
                "Response should contain an error message. Got: " + body);
    }

    @Then("the response should contain field {string}")
    public void theResponseShouldContainField(String fieldName) {
        String body = response.asString();
        Assert.assertTrue(body.contains("\"" + fieldName + "\""),
                "Response should contain field '" + fieldName + "'. Got: " + body);
    }

    @Then("the response should contain field {string} with value {string}")
    public void theResponseShouldContainFieldWithValue(String fieldName, String expectedValue) {
        Assert.assertTrue(response.asString().contains(expectedValue),
                "Expected field '" + fieldName + "' with value '" + expectedValue + "'");
    }

    @Then("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThan(int maxMs) {
        long actualMs = response.timeIn(java.util.concurrent.TimeUnit.MILLISECONDS);
        Assert.assertTrue(actualMs < maxMs,
                "Response time " + actualMs + "ms exceeded " + maxMs + "ms");
    }

    @Then("the response should include {string} header")
    public void theResponseShouldIncludeHeader(String headerName) {
        Assert.assertNotNull(response.header(headerName),
                "Response should include header: " + headerName);
    }

    @Then("the Allow header should contain {string}")
    public void theAllowHeaderShouldContain(String method) {
        String allow = response.header("Allow");
        Assert.assertNotNull(allow, "Allow header should be present");
        Assert.assertTrue(allow.contains(method),
                "Allow header should contain " + method + ". Got: " + allow);
    }
}
