package com.shopsphere.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton ConfigReader - loads environment-specific properties.
 * Usage: -Denv=staging (dev | staging | prod)
 */
public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        String env = System.getProperty("env", "staging");
        String filePath = "core/src/main/resources/config/" + env + ".properties";
        log.info("Loading config for environment: {}", env);
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            log.error("Failed to load config file: {}", filePath);
            throw new RuntimeException("Config file not found: " + filePath, e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }

    public String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String getBaseUrl()       { return get("base.url"); }
    public String getApiBaseUrl()    { return get("api.base.url"); }
    public String getBrowser()       { return get("browser"); }
    public boolean isHeadless()      { return Boolean.parseBoolean(get("headless")); }
    public int getImplicitWait()     { return Integer.parseInt(get("implicit.wait")); }
    public int getExplicitWait()     { return Integer.parseInt(get("explicit.wait")); }
    public String getAdminUsername() { return get("admin.username"); }
    public String getAdminPassword() { return get("admin.password"); }
}
