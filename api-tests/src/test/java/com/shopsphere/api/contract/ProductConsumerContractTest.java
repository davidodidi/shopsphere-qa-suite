package com.shopsphere.api.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Consumer-Driven Contract Tests using Pact.
 *
 * These tests define the CONTRACT between the ShopSphere Frontend (consumer)
 * and the Product Service (provider).
 *
 * How it works:
 *   1. Consumer defines expected request/response (this file)
 *   2. Pact generates a pact file (JSON contract)
 *   3. Provider verifies against the contract (see ProductProviderContractTest)
 *
 * This is essential for SOA/microservices environments where services evolve independently.
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ShopSphereProductService")
@DisplayName("Product Service Consumer Contract Tests")
class ProductConsumerContractTest {

    // ─── Pact Definitions ──────────────────────────────────────────────────

    @Pact(provider = "ShopSphereProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getAllProductsPact(PactDslWithProvider builder) {
        return builder
                .given("products exist in the system")
                .uponReceiving("a GET request for all products")
                    .path("/v1/products")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .booleanType("success", true)
                            .array("data")
                                .object()
                                    .stringType("id")
                                    .stringType("name")
                                    .decimalType("price")
                                    .stringType("category")
                                .closeObject()
                            .closeArray()
                    )
                .toPact();
    }

    @Pact(provider = "ShopSphereProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getProductByIdPact(PactDslWithProvider builder) {
        return builder
                .given("product with id 'prod-001' exists")
                .uponReceiving("a GET request for product prod-001")
                    .path("/v1/products/prod-001")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .stringType("id", "prod-001")
                            .stringType("name")
                            .stringType("description")
                            .decimalType("price")
                            .integerType("stockQuantity")
                    )
                .toPact();
    }

    @Pact(provider = "ShopSphereProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getProductNotFoundPact(PactDslWithProvider builder) {
        return builder
                .given("product with id 'nonexistent' does NOT exist")
                .uponReceiving("a GET request for a non-existent product")
                    .path("/v1/products/nonexistent")
                    .method("GET")
                .willRespondWith()
                    .status(404)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .booleanType("success", false)
                            .stringType("message")
                    )
                .toPact();
    }

    @Pact(provider = "ShopSphereProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact createProductPact(PactDslWithProvider builder) {
        return builder
                .given("admin user is authenticated")
                .uponReceiving("a POST request to create a product")
                    .path("/v1/products")
                    .method("POST")
                    .headers(java.util.Map.of(
                            "Content-Type", "application/json",
                            "Authorization", "Bearer valid-token"
                    ))
                    .body(new PactDslJsonBody()
                            .stringType("name", "New Product")
                            .decimalType("price", 29.99)
                            .stringType("category", "Electronics")
                    )
                .willRespondWith()
                    .status(201)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(new PactDslJsonBody()
                            .stringType("id")
                            .stringType("name", "New Product")
                            .decimalType("price", 29.99)
                    )
                .toPact();
    }

    // ─── Contract Verification Tests ───────────────────────────────────────

    @Test
    @PactTestFor(pactMethod = "getAllProductsPact")
    @DisplayName("GET /products returns list of products matching contract")
    void getAllProductsMatchesContract(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();

        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/v1/products")
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(response.jsonPath().getBoolean("success"),
                "Response success should be true");
        assertNotNull(response.jsonPath().getList("data"),
                "Response data should not be null");
    }

    @Test
    @PactTestFor(pactMethod = "getProductByIdPact")
    @DisplayName("GET /products/{id} returns single product matching contract")
    void getProductByIdMatchesContract(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();

        Response response = given()
                .when()
                .get("/v1/products/prod-001")
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals("prod-001", response.jsonPath().getString("id"),
                "Product ID should match");
        assertNotNull(response.jsonPath().getString("name"),
                "Product name should not be null");
        assertTrue(response.jsonPath().getDouble("price") > 0,
                "Product price should be positive");
    }

    @Test
    @PactTestFor(pactMethod = "getProductNotFoundPact")
    @DisplayName("GET /products/{nonexistent} returns 404 matching contract")
    void getProductNotFoundMatchesContract(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();

        given()
                .when()
                .get("/v1/products/nonexistent")
                .then()
                .statusCode(404);
    }

    @Test
    @PactTestFor(pactMethod = "createProductPact")
    @DisplayName("POST /products creates product matching contract")
    void createProductMatchesContract(MockServer mockServer) {
        RestAssured.baseURI = mockServer.getUrl();

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer valid-token")
                .body("{\"name\":\"New Product\",\"price\":29.99,\"category\":\"Electronics\"}")
                .when()
                .post("/v1/products")
                .then()
                .statusCode(201);
    }
}
