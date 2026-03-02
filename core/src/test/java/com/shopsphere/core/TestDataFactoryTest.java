package com.shopsphere.core;

import com.shopsphere.models.User;
import com.shopsphere.models.Product;
import com.shopsphere.utils.TestDataFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD Unit tests for TestDataFactory.
 * Verifies data integrity before use in integration tests.
 */
@DisplayName("TestDataFactory Unit Tests")
class TestDataFactoryTest {

    @Test
    @DisplayName("Generated user should have required fields")
    void generatedUserShouldHaveRequiredFields() {
        User user = TestDataFactory.createRandomUser();
        assertAll("User required fields",
                () -> assertNotNull(user.getFirstName(), "First name"),
                () -> assertNotNull(user.getLastName(),  "Last name"),
                () -> assertNotNull(user.getEmail(),     "Email"),
                () -> assertNotNull(user.getPassword(),  "Password"),
                () -> assertNotNull(user.getAddress(),   "Address")
        );
    }

    @Test
    @DisplayName("Generated user email should be valid format")
    void generatedUserEmailShouldBeValid() {
        User user = TestDataFactory.createRandomUser();
        assertTrue(user.getEmail().contains("@"), "Email should contain @");
        assertTrue(user.getEmail().contains("."),  "Email should contain .");
    }

    @Test
    @DisplayName("Generated product should have positive price")
    void generatedProductShouldHavePositivePrice() {
        Product product = TestDataFactory.createRandomProduct();
        assertNotNull(product.getPrice(), "Price should not be null");
        assertTrue(product.getPrice().doubleValue() > 0, "Price should be positive");
    }

    @RepeatedTest(5)
    @DisplayName("Each generated user should have unique email")
    void eachUserShouldHaveUniqueEmail(RepetitionInfo info) {
        User u1 = TestDataFactory.createRandomUser();
        User u2 = TestDataFactory.createRandomUser();
        assertNotEquals(u1.getEmail(), u2.getEmail(),
                "Emails should be unique, iteration: " + info.getCurrentRepetition());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Generated users should always have address with country Canada")
    void userAddressShouldHaveCanadaCountry(int ignored) {
        User user = TestDataFactory.createRandomUser();
        assertEquals("Canada", user.getAddress().getCountry());
    }
}
