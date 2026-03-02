package com.shopsphere.config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * AppiumDriverManager - Thread-safe Appium driver management.
 * Supports Android, iOS, and AWS Device Farm.
 */
public class AppiumDriverManager {
    private static final Logger log = LogManager.getLogger(AppiumDriverManager.class);
    private static final ThreadLocal<AppiumDriver> driverThread = new ThreadLocal<>();
    private static final String APPIUM_SERVER = System.getProperty("appium.server", "http://localhost:4723");

    private AppiumDriverManager() {}

    public static AppiumDriver getDriver() {
        if (driverThread.get() == null) initDriver();
        return driverThread.get();
    }

    public static void initDriver() {
        String platform = System.getProperty("platform", "android").toLowerCase();
        log.info("Initialising Appium driver for platform: {}", platform);

        try {
            AppiumDriver driver = platform.equals("ios")
                    ? createIOSDriver()
                    : createAndroidDriver();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverThread.set(driver);
            log.info("Appium driver initialised successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise Appium driver: " + e.getMessage(), e);
        }
    }

    private static AndroidDriver createAndroidDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",    "Android");
        caps.setCapability("deviceName",      System.getProperty("device.name", "Android Emulator"));
        caps.setCapability("platformVersion", System.getProperty("platform.version", "12.0"));
        caps.setCapability("automationName",  "UIAutomator2");
        caps.setCapability("app",             System.getProperty("app.path", "src/test/resources/apps/SauceLabs.apk"));
        caps.setCapability("appPackage",      System.getProperty("app.package", "com.swaglabsmobileapp"));
        caps.setCapability("appActivity",     System.getProperty("app.activity", ".MainActivity"));
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("autoGrantPermissions", true);

        // AWS Device Farm support
        String awsArn = System.getProperty("aws.device.arn");
        if (awsArn != null) {
            caps.setCapability("deviceArn", awsArn);
        }

        return new AndroidDriver(new URL(APPIUM_SERVER + "/wd/hub"), caps);
    }

    private static IOSDriver createIOSDriver() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",    "iOS");
        caps.setCapability("deviceName",      System.getProperty("device.name", "iPhone 14"));
        caps.setCapability("platformVersion", System.getProperty("platform.version", "16.0"));
        caps.setCapability("automationName",  "XCUITest");
        caps.setCapability("app",             System.getProperty("app.path", "src/test/resources/apps/SauceLabs.app"));
        caps.setCapability("bundleId",        System.getProperty("bundle.id", "com.swaglabsmobileapp"));
        caps.setCapability("newCommandTimeout", 300);

        return new IOSDriver(new URL(APPIUM_SERVER + "/wd/hub"), caps);
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            log.info("Appium driver closed");
        }
    }
}
