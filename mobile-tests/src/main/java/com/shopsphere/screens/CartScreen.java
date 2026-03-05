package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartScreen extends BaseScreen {

    @AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc='test-Cart Content']//android.view.ViewGroup[@content-desc='test-Item']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeScrollView[@name='test-Cart Content']//XCUIElementTypeOther[@name='test-Item']")
    private List<WebElement> cartItems;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "test-CHECKOUT")
    private WebElement checkoutButton;

    public CartScreen(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public int getItemCount() { return cartItems.size(); }

    @Step("Scrolling down and tapping checkout button")
    public CheckoutScreen proceedToCheckout() {
        scrollDown();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']")));
            driver.findElement(
                By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']")).click();
            log.info("Tapped CHECKOUT button successfully");
        } catch (Exception e) {
            log.warn("CHECKOUT via xpath failed, trying accessibility id: {}", e.getMessage());
            tap(checkoutButton);
        }
        return new CheckoutScreen(driver);
    }

    @Override
    public boolean isLoaded() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.ScrollView[@content-desc='test-Cart Content']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
