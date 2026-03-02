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
import java.time.Duration;

/**
 * Thread-safe DriverManager using ThreadLocal.
 * Supports local Chrome/Firefox/Edge and Selenium Grid (Docker).
 */
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

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driverThread.set(driver);
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new FirefoxDriver(opts);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                yield new EdgeDriver(opts);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                opts.addArguments("--window-size=1920,1080");
                yield new ChromeDriver(opts);
            }
        };
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless, String gridUrl)
            throws MalformedURLException {
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("--headless");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
            default -> {
                ChromeOptions opts = new ChromeOptions();
                if (headless) opts.addArguments("--headless=new", "--no-sandbox");
                yield new RemoteWebDriver(new URL(gridUrl), opts);
            }
        };
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            log.info("WebDriver closed and removed from ThreadLocal");
        }
    }
}
