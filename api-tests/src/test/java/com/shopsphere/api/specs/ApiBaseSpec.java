package com.shopsphere.api.specs;

import com.shopsphere.config.ConfigReader;
import com.shopsphere.constants.HTTPStatus;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * API Base Specification — centralised RestAssured configuration.
 * Demonstrates SOA/HTTP protocol testing practices.
 *
 * HTTP Methods covered:
 *   GET    — Read resources
 *   POST   — Create resources
 *   PUT    — Full update
 *   PATCH  — Partial update
 *   DELETE — Remove resources
 *   HEAD   — Check headers without body
 *   OPTIONS— CORS/allowed methods check
 */
public class ApiBaseSpec {
    protected static final Logger log = LogManager.getLogger(ApiBaseSpec.class);
    protected static final ConfigReader config = ConfigReader.getInstance();
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec200;

    static {
        RestAssured.baseURI = config.getApiBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(config.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .addHeader("Referer", config.getApiBaseUrl())
                .addFilter(new AllureRestAssured())
                .addFilter(new io.restassured.filter.log.RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new io.restassured.filter.log.ResponseLoggingFilter(LogDetail.ALL))
                .build();

        responseSpec200 = new ResponseSpecBuilder()
                .expectStatusCode(HTTPStatus.OK)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(org.hamcrest.Matchers.lessThan(3000L), TimeUnit.MILLISECONDS)
                .build();
    }

    protected RequestSpecification givenAuth(String token) {
        return RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token);
    }

    protected RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }
}
