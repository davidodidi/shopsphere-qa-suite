package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
        log.info("DEBUG fillCheckoutInfo called: '{} {} {}'", firstName, lastName, postalCode);

        typeIntoField(firstNameField, firstName);
        typeIntoField(lastNameField, lastName);
        typeIntoField(postalCodeField, postalCode);

        // Read back values via JavaScript to confirm what is actually in the DOM
        // and what React's internal fiber state thinks the value is.
        String fn = jsGetValue("first-name");
        String ln = jsGetValue("last-name");
        String zip = jsGetValue("postal-code");
        log.info("DEBUG DOM values after typeIntoField — first:'{}' last:'{}' zip:'{}'", fn, ln, zip);

        // Also check React's internal state directly from the fiber node
        String fnReact = jsGetReactValue("first-name");
        String lnReact = jsGetReactValue("last-name");
        String zipReact = jsGetReactValue("postal-code");
        log.info("DEBUG React fiber values after typeIntoField — first:'{}' last:'{}' zip:'{}'", fnReact, lnReact, zipReact);

        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
        // Log field values one more time immediately before the click
        String fn = jsGetValue("first-name");
        String ln = jsGetValue("last-name");
        String zip = jsGetValue("postal-code");
        log.info("DEBUG values immediately before continue click — first:'{}' last:'{}' zip:'{}'", fn, ln, zip);

        WaitUtils.waitForClickable(continueButton).click();
        log.info("DEBUG continue button clicked — waiting for checkout-step-two");
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

    private void typeIntoField(WebElement field, String value) {
        WaitUtils.waitForClickable(field);
        new Actions(driver)
            .click(field)
            .sendKeys(value)
            .perform();
    }

    /** Reads the DOM .value property of an input by its id. */
    private String jsGetValue(String id) {
        try {
            Object result = ((JavascriptExecutor) driver)
                .executeScript("var el = document.getElementById(arguments[0]); return el ? el.value : 'ELEMENT_NOT_FOUND';", id);
            return result != null ? result.toString() : "null";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Reads React's internal fiber state for an input by its id.
     * React 17 stores the current value in the fiber node under
     * __reactFiber or __reactInternalInstance keys.
     */
    private String jsGetReactValue(String id) {
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(
                "var el = document.getElementById(arguments[0]);" +
                "if (!el) return 'ELEMENT_NOT_FOUND';" +
                "var fiberKey = Object.keys(el).find(k => k.startsWith('__reactFiber') || k.startsWith('__reactInternalInstance'));" +
                "if (!fiberKey) return 'NO_FIBER_KEY';" +
                "var fiber = el[fiberKey];" +
                "if (!fiber) return 'NO_FIBER';" +
                "var memoized = fiber.memoizedProps;" +
                "if (!memoized) return 'NO_MEMOIZED_PROPS';" +
                "return memoized.value !== undefined ? String(memoized.value) : 'NO_VALUE_PROP';",
                id
            );
            return result != null ? result.toString() : "null";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
