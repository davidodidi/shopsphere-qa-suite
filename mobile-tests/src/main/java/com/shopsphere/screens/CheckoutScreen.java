package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

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

    @AndroidFindBy(accessibility = "test-FINISH")
    @iOSXCUITFindBy(accessibility = "test-FINISH")
    private WebElement finishButton;

    @AndroidFindBy(xpath = "//*[@content-desc='test-CHECKOUT: COMPLETE!']")
    @iOSXCUITFindBy(accessibility = "test-CHECKOUT: COMPLETE!")
    private WebElement orderCompleteHeader;

    public CheckoutScreen(AppiumDriver driver) { super(driver); }

    @Step("Filling mobile checkout info")
    public CheckoutScreen fillInfo(String firstName, String lastName, String postalCode) {
        typeText(firstNameField, firstName);
        typeText(lastNameField, lastName);
        typeText(postalCodeField, postalCode);
        return this;
    }

    @Step("Tapping continue")
    public CheckoutScreen tapContinue() { tap(continueButton); return this; }

    @Step("Tapping finish")
    public CheckoutScreen tapFinish() { tap(finishButton); return this; }

    public boolean isOrderComplete() {
        try { return orderCompleteHeader.isDisplayed(); }
        catch (Exception e) { return false; }
    }

    @Override
    public boolean isLoaded() { return isDisplayed(firstNameField); }
}
