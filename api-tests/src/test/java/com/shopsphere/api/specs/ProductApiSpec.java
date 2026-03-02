package com.shopsphere.api.specs;

import com.shopsphere.constants.APIEndpoints;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Product Service API spec.
 * Tests GET/POST/PUT/PATCH/DELETE on /products.
 * Validates HTTP protocol compliance: status codes, headers, response times.
 */
public class ProductApiSpec extends ApiBaseSpec {
    private static final Logger log = LogManager.getLogger(ProductApiSpec.class);

    public Response getAllProducts() {
        log.info("GET {}", APIEndpoints.PRODUCTS);
        return given().when().get(APIEndpoints.PRODUCTS).then().extract().response();
    }

    public Response getProductById(String productId) {
        log.info("GET {}/{}", APIEndpoints.PRODUCTS, productId);
        return given().when().get(APIEndpoints.PRODUCT_BY_ID.replace("{id}", productId))
                .then().extract().response();
    }

    public Response searchProducts(String query) {
        return given()
                .queryParam("q", query)
                .when()
                .get(APIEndpoints.PRODUCT_SEARCH)
                .then().extract().response();
    }

    public Response createProduct(Map<String, Object> productPayload, String token) {
        log.info("POST {} — creating product", APIEndpoints.PRODUCTS);
        return givenAuth(token)
                .body(productPayload)
                .when()
                .post(APIEndpoints.PRODUCTS)
                .then().extract().response();
    }

    public Response updateProduct(String productId, Map<String, Object> payload, String token) {
        return givenAuth(token)
                .body(payload)
                .when()
                .put(APIEndpoints.PRODUCT_BY_ID.replace("{id}", productId))
                .then().extract().response();
    }

    public Response patchProduct(String productId, Map<String, Object> partialPayload, String token) {
        return givenAuth(token)
                .body(partialPayload)
                .when()
                .patch(APIEndpoints.PRODUCT_BY_ID.replace("{id}", productId))
                .then().extract().response();
    }

    public Response deleteProduct(String productId, String token) {
        log.info("DELETE {}/{}", APIEndpoints.PRODUCTS, productId);
        return givenAuth(token)
                .when()
                .delete(APIEndpoints.PRODUCT_BY_ID.replace("{id}", productId))
                .then().extract().response();
    }

    public Response headProducts() {
        return given().when().head(APIEndpoints.PRODUCTS).then().extract().response();
    }

    public Response optionsProducts() {
        return given().when().options(APIEndpoints.PRODUCTS).then().extract().response();
    }
}
