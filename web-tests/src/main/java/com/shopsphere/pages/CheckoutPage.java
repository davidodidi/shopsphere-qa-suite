package com.shopsphere.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

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
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalCodeField, postalCode);
        return this;
    }

    @Step("Clicking continue")
    public CheckoutPage clickContinue() {
        click(continueButton);
        return this;
    }

    @Step("Finishing order")
    public CheckoutPage finishOrder() {
        click(finishButton);
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
}
