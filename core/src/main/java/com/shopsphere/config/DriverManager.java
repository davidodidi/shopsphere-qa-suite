package com.shopsphere.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            initDriver();
        }
        return driverThread.get();
    }

    public static void initDriver() {
        String browser = System.getProperty("browser", config.getBrowser()).toLowerCase();
        String gridUrl = System.getProperty("grid.url", "");
        boolean headless = config.isHeadless();
        WebDriver driver;

        log.info("Initialising browser: {} | headless: {} | grid: {}", browser, headless, !gridUrl.isEmpty());

        try {
            if (!gridUrl.isEmpty()) {
                driver = createRemoteDriver(browser, headless, gridUrl);
            } else {
                driver = createLocalDriver(browser, headless);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise WebDriver", e);
        }

        // maximize() hangs in headless Docker/WSL2 for up to 180s then crashes
        // the Chrome process. Window size is already set via --window-size flag.
        if (!headless) {
            driver.manage().window().maximize();
        }

        // Implicit wait intentionally removed — it conflicts with WebDriverWait
        // explicit waits in WaitUtils and causes unpredictable timing behaviour.
        // All synchronisation is handled exclusively by WaitUtils.

        driverThread.set(driver);
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        if ("firefox".equals(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions opts = new FirefoxOptions();
            if (headless) opts.addArguments("--headless");
            return new FirefoxDriver(opts);
        } else if ("edge".equals(browser)) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions opts = new EdgeOptions();
            if (headless) opts.addArguments("--headless");
            return new EdgeDriver(opts);
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions opts = new ChromeOptions();
            if (headless) {
                opts.addArguments(
                    "--headless=new",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu",
                    "--window-size=1920,1080"
                );
            } else {
                opts.addArguments("--window-size=1920,1080");
            }
            return new ChromeDriver(opts);
        }
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless, String gridUrl)
            throws MalformedURLException {
        if ("firefox".equals(browser)) {
            FirefoxOptions opts = new FirefoxOptions();
            if (headless) opts.addArguments("--headless");
            return new RemoteWebDriver(new URL(gridUrl), opts);
        } else if ("edge".equals(browser)) {
            EdgeOptions opts = new EdgeOptions();
            if (headless) opts.addArguments("--headless");
            return new RemoteWebDriver(new URL(gridUrl), opts);
        } else {
            ChromeOptions opts = new ChromeOptions();
            if (headless) {
                opts.addArguments(
                    "--headless=new",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu",
                    "--window-size=1920,1080"
                );
            }
            return new RemoteWebDriver(new URL(gridUrl), opts);
        }
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            log.info("WebDriver closed and removed from ThreadLocal");
        }
    }
}
