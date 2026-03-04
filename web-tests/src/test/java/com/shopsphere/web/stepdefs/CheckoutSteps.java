package com.shopsphere.web.stepdefs;

import com.shopsphere.pages.*;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class CheckoutSteps {

    private final ScenarioContext ctx;

    public CheckoutSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("I add {string} to the cart from the products page")
    public void iAddProductFromProductsPage(String productName) {
        ctx.productsPage = new ProductsPage();
        ctx.productsPage.addProductToCart(productName);
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        ctx.checkoutPage = ctx.cartPage.proceedToCheckout();
    }

    @When("I fill in checkout information with first name {string} last name {string} and postal code {string}")
    public void iFillCheckoutInfo(String firstName, String lastName, String postalCode) {
        ctx.checkoutPage.fillCheckoutInfo(firstName, lastName, postalCode);
    }

    @When("I continue to order review")
    public void iContinueToOrderReview() {
        ctx.checkoutPage.clickContinue();
    }

    @When("I finish the order")
    public void iFinishTheOrder() {
        ctx.checkoutPage.finishOrder();
    }

    @Then("I should see the order confirmation message")
    public void iShouldSeeOrderConfirmation() {
        Assert.assertTrue(ctx.checkoutPage.isOrderComplete(),
                "Order confirmation should be displayed. Got: " + ctx.checkoutPage.getOrderConfirmation());
    }

    @When("I click continue without filling in checkout information")
    public void iClickContinueWithoutInfo() {
        // Use the error-expecting variant so the form stays on step-one
        // and SauceDemo's own validation error message is shown.
        ctx.checkoutPage.clickContinueExpectingError();
    }

    @Then("I should see an error message about required fields")
    public void iShouldSeeRequiredFieldsError() {
        Assert.assertFalse(ctx.checkoutPage.getErrorMessage().isEmpty(),
                "Error message should be shown for empty fields");
    }
}
