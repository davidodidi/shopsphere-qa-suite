package com.shopsphere.api.specs;

import com.shopsphere.constants.APIEndpoints;
import com.shopsphere.constants.HTTPStatus;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Auth service API spec.
 * Validates POST /auth/login, /auth/logout, /auth/refresh.
 */
public class AuthSpec extends ApiBaseSpec {
    private static final Logger log = LogManager.getLogger(AuthSpec.class);

    public Response login(String username, String password) {
        log.info("POST {} — user: {}", APIEndpoints.AUTH_LOGIN, username);
        return given()
                .body(Map.of("username", username, "password", password))
                .when()
                .post(APIEndpoints.AUTH_LOGIN)
                .then()
                .log().ifError()
                .extract().response();
    }

    public String getValidToken() {
        Response response = login(config.getAdminUsername(), config.getAdminPassword());
        // In real app: return response.jsonPath().getString("data.token")
        // SauceDemo doesn't have a real API, so we return a mock token for demo
        return "mock-bearer-token-" + System.currentTimeMillis();
    }

    public Response refreshToken(String refreshToken) {
        return given()
                .body(Map.of("refreshToken", refreshToken))
                .when()
                .post(APIEndpoints.AUTH_REFRESH)
                .then()
                .extract().response();
    }
}
