package com.shopsphere.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton ConfigReader - loads environment-specific properties from classpath.
 * Usage: -Denv=staging (dev | staging | prod)
 */
public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        String env = System.getProperty("env", "dev");
        String resourcePath = "config/" + env + ".properties";
        log.info("Loading config for environment: {}", env);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Config file not found on classpath: " + resourcePath);
            }
            properties.load(is);
        } catch (IOException e) {
            log.error("Failed to load config file: {}", resourcePath);
            throw new RuntimeException("Failed to load config: " + resourcePath, e);
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
