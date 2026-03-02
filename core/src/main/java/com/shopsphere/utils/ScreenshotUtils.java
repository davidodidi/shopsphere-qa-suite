package com.shopsphere.utils;

import com.shopsphere.config.DriverManager;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {
    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    private ScreenshotUtils() {}

    public static byte[] captureScreenshot() {
        WebDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void attachToAllure(String name) {
        try {
            byte[] screenshot = captureScreenshot();
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
            log.info("Screenshot '{}' attached to Allure report", name);
        } catch (Exception e) {
            log.warn("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    public static String saveToFile(String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, captureScreenshot());
            log.info("Screenshot saved: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
            return "";
        }
    }
}
