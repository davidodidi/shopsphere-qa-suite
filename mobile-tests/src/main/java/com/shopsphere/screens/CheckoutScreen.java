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

public class CheckoutScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-First Name")
    @iOSXCUITFindBy(accessibility = "test-First Name")
    private WebElement firstNameField;

    @AndroidFindBy(accessibility = "test-Last Name")
    @iOSXCUITFindBy(accessibility = "test-Last Name")
    private WebElement lastNameField;

    @AndroidFindBy(accessibility = "test-Zip/Postal Code")
    @iOSXCUITFindBy(accessibility = "test-Zip/Postal Code")
    private WebElement postalCodeField;

    @AndroidFindBy(accessibility = "test-CONTINUE")
    @iOSXCUITFindBy(accessibility = "test-CONTINUE")
    private WebElement continueButton;

    @AndroidFindBy(xpath = "//*[@content-desc='test-CHECKOUT: COMPLETE!']")
    @iOSXCUITFindBy(accessibility = "test-CHECKOUT: COMPLETE!")
    private WebElement orderCompleteHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='test-Error message']")
    private WebElement errorMessage;

    public CheckoutScreen(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    @Step("Filling mobile checkout info")
    public CheckoutScreen fillInfo(String firstName, String lastName, String postalCode) {
        typeText(firstNameField, firstName);
        typeText(lastNameField, lastName);
        typeText(postalCodeField, postalCode);
        return this;
    }

    @Step("Tapping continue")
    public CheckoutScreen tapContinue() { tap(continueButton); return this; }

    @Step("Tapping continue without filling in info")
    public CheckoutScreen tapContinueWithoutFilling() { tap(continueButton); return this; }

    @Step("Scrolling down and tapping finish")
    public CheckoutScreen tapFinish() {
        scrollDown();
        scrollDown();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.view.ViewGroup[@content-desc='test-FINISH']")));
            driver.findElement(
                By.xpath("//android.view.ViewGroup[@content-desc='test-FINISH']")).click();
            log.info("Tapped FINISH button successfully");
        } catch (Exception e) {
            log.error("Could not tap FINISH button: {}", e.getMessage());
        }
        return this;
    }

    public boolean isOrderComplete() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@content-desc='test-CHECKOUT: COMPLETE!']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorDisplayed() { return isDisplayed(errorMessage); }
    public String getErrorMessage()    { return getText(errorMessage); }

    @Override
    public boolean isLoaded() { return isDisplayed(firstNameField); }
}
