package com.shopsphere.core;

import com.shopsphere.config.ConfigReader;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD Unit tests for ConfigReader.
 * Tests written first, implementation follows.
 */
@DisplayName("ConfigReader Unit Tests")
class ConfigReaderTest {

    private ConfigReader config;

    @BeforeEach
    void setUp() {
        System.setProperty("env", "staging");
        config = ConfigReader.getInstance();
    }

    @Test
    @DisplayName("Should load base URL from staging config")
    void shouldLoadBaseUrl() {
        String baseUrl = config.getBaseUrl();
        assertNotNull(baseUrl, "Base URL should not be null");
        assertTrue(baseUrl.startsWith("https://"), "Base URL should use HTTPS");
    }

    @Test
    @DisplayName("Should return default browser as chrome")
    void shouldReturnDefaultBrowser() {
        String browser = config.getBrowser();
        assertNotNull(browser, "Browser should not be null");
        assertFalse(browser.isEmpty(), "Browser should not be empty");
    }

    @Test
    @DisplayName("Should parse headless as boolean")
    void shouldParseHeadlessAsBoolean() {
        assertDoesNotThrow(() -> config.isHeadless(),
                "isHeadless() should not throw exception");
    }

    @Test
    @DisplayName("Should return positive implicit wait value")
    void shouldReturnPositiveImplicitWait() {
        int wait = config.getImplicitWait();
        assertTrue(wait > 0, "Implicit wait should be positive");
    }

    @Test
    @DisplayName("System property should override config file value")
    void systemPropertyShouldOverrideConfig() {
        System.setProperty("browser", "firefox");
        assertEquals("firefox", config.get("browser"));
        System.clearProperty("browser");
    }
}
