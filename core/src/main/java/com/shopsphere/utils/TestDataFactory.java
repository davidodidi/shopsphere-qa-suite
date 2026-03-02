package com.shopsphere.utils;

import com.github.javafaker.Faker;
import com.shopsphere.models.User;
import com.shopsphere.models.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Generates realistic test data using JavaFaker.
 * Used for both TDD unit tests and BDD integration tests.
 */
public class TestDataFactory {
    private static final Faker faker = new Faker(new Locale("en-CA"));

    private TestDataFactory() {}

    public static User createRandomUser() {
        return User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .username(faker.name().username())
                .password("Test@" + faker.number().digits(6))
                .phone(faker.phoneNumber().cellPhone())
                .address(User.Address.builder()
                        .street(faker.address().streetAddress())
                        .city(faker.address().city())
                        .province(faker.address().state())
                        .postalCode(faker.address().zipCode())
                        .country("Canada")
                        .build())
                .role("CUSTOMER")
                .active(true)
                .build();
    }

    public static Product createRandomProduct() {
        return Product.builder()
                .name(faker.commerce().productName())
                .description(faker.lorem().sentence(10))
                .price(new BigDecimal(faker.commerce().price()).setScale(2, RoundingMode.HALF_UP))
                .category(faker.commerce().department())
                .stockQuantity(faker.number().numberBetween(1, 500))
                .rating(faker.number().randomDouble(1, 1, 5))
                .reviewCount(faker.number().numberBetween(0, 1000))
                .build();
    }

    public static String randomEmail()    { return faker.internet().emailAddress(); }
    public static String randomPassword() { return "Test@" + faker.number().digits(6); }
    public static String randomName()     { return faker.name().fullName(); }
    public static String randomPhone()    { return faker.phoneNumber().cellPhone(); }
}
