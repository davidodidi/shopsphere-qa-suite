package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CheckoutPage.class);

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement orderConfirmationHeader;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public CheckoutPage() { super(); }

    @Step("Filling checkout info: {firstName} {lastName} {postalCode}")
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        log.info("DEBUG fillCheckoutInfo called with: '{}' '{}' '{}'", firstName, lastName, postalCode);
        log.info("DEBUG current URL before fill: {}", driver.getCurrentUrl());

        WaitUtils.waitForClickable(firstNameField);
        log.info("DEBUG firstNameField is clickable");

        reactSetValue(firstNameField, firstName);
        reactSetValue(lastNameField, lastName);
        reactSetValue(postalCodeField, postalCode);

        // Read values back from DOM to confirm they were set
        String v1 = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", firstNameField);
        String v2 = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", lastNameField);
        String v3 = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", postalCodeField);
        log.info("DEBUG field values after reactSetValue — first:'{}' last:'{}' zip:'{}'", v1, v2, v3);

        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
        log.info("DEBUG clickContinue — URL before click: {}", driver.getCurrentUrl());

        // Re-read values immediately before clicking to catch any React re-render wiping them
        String v1 = (String) ((JavascriptExecutor) driver).executeScript("return document.getElementById('first-name').value;");
        String v2 = (String) ((JavascriptExecutor) driver).executeScript("return document.getElementById('last-name').value;");
        String v3 = (String) ((JavascriptExecutor) driver).executeScript("return document.getElementById('postal-code').value;");
        log.info("DEBUG field values immediately before click — first:'{}' last:'{}' zip:'{}'", v1, v2, v3);

        WaitUtils.waitForClickable(continueButton).click();
        log.info("DEBUG click fired — waiting for checkout-step-two");
        WaitUtils.waitForUrlToContain("checkout-step-two");
        return this;
    }

    @Step("Clicking continue expecting validation error")
    public CheckoutPage clickContinueExpectingError() {
        WaitUtils.waitForClickable(continueButton).click();
        return this;
    }

    @Step("Finishing order")
    public CheckoutPage finishOrder() {
        scrollToElement(finishButton);
        jsClick(finishButton);
        WaitUtils.waitForUrlToContain("checkout-complete");
        return this;
    }

    public String getOrderConfirmation() { return getText(orderConfirmationHeader); }
    public String getErrorMessage()      { return getText(errorMessage); }

    public boolean isOrderComplete() {
        try {
            return orderConfirmationHeader.isDisplayed()
                    && getOrderConfirmation().toLowerCase().contains("thank you");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("checkout");
    }

    private void reactSetValue(WebElement field, String value) {
        WaitUtils.waitForVisibility(field);
        ((JavascriptExecutor) driver).executeScript(
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "    window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new InputEvent('input', {" +
            "    bubbles: true," +
            "    inputType: 'insertText'," +
            "    data: arguments[1]" +
            "}));",
            field, value
        );
    }
}
