package com.shopsphere.api.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
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
 * Defines the CONTRACT between ShopSphere Frontend (consumer)
 * and FakeStore Product Service (provider).
 *
 * Fields match FakeStoreAPI exactly:
 *   id, title, price, category, description, image
 */
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "FakeStoreProductService")
@DisplayName("FakeStore Product Service Consumer Contract Tests")
class ProductConsumerContractTest {

    // ─── Pact: GET /products ───────────────────────────────────────────────

    @Pact(provider = "FakeStoreProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getAllProductsPact(PactDslWithProvider builder) {
        return builder
                .given("products exist")
                .uponReceiving("GET all products")
                    .path("/products")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(PactDslJsonArray.arrayMinLike(1)
                            .integerType("id", 1)
                            .stringType("title", "Fjallraven Backpack")
                            .decimalType("price", 109.95)
                            .stringType("category", "men's clothing")
                            .stringType("description", "A backpack")
                            .stringType("image", "https://fakestoreapi.com/img/1.jpg")
                            .closeObject()
                    )
                .toPact();
    }

    // ─── Pact: GET /products/{id} ──────────────────────────────────────────

    @Pact(provider = "FakeStoreProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getProductByIdPact(PactDslWithProvider builder) {
        return builder
                .given("product with id 1 exists")
                .uponReceiving("GET product by id 1")
                    .path("/products/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(java.util.Map.of("Content-Type", "application/json"))
                    .body(new au.com.dius.pact.consumer.dsl.PactDslJsonBody()
                            .integerType("id", 1)
                            .stringType("title", "Fjallraven Backpack")
                            .decimalType("price", 109.95)
                            .stringType("category", "men's clothing")
                            .stringType("description", "A backpack")
                            .stringType("image", "https://fakestoreapi.com/img/1.jpg")
                    )
                .toPact();
    }

    // ─── Pact: GET /products/999 (not found) ──────────────────────────────

    @Pact(provider = "FakeStoreProductService", consumer = "ShopSphereFrontend")
    public RequestResponsePact getProductNotFoundPact(PactDslWithProvider builder) {
        return builder
                .given("product with id 999 does not exist")
                .uponReceiving("GET product by non-existent id")
                    .path("/products/999")
                    .method("GET")
                .willRespondWith()
                    .status(404)
                .toPact();
    }

    // ─── Tests ────────────────────────────────────────────────────────────

    @Test
    @PactTestFor(pactMethod = "getAllProductsPact")
    @DisplayName("GET /products returns array with product fields")
    void testGetAllProducts(MockServer mockServer) {
        Response response = given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response.jsonPath().getList("$"));
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }

    @Test
    @PactTestFor(pactMethod = "getProductByIdPact")
    @DisplayName("GET /products/1 returns single product with correct fields")
    void testGetProductById(MockServer mockServer) {
        given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("price", notNullValue())
                .body("category", notNullValue());
    }

    @Test
    @PactTestFor(pactMethod = "getProductNotFoundPact")
    @DisplayName("GET /products/999 returns 404 when product not found")
    void testGetProductNotFound(MockServer mockServer) {
        given()
                .baseUri(mockServer.getUrl())
                .when()
                .get("/products/999")
                .then()
                .statusCode(404);
    }
}
