package com.shopsphere.web.stepdefs;

import com.shopsphere.pages.*;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class CheckoutSteps {
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @Given("I add {string} to the cart from the products page")
    public void iAddProductFromProductsPage(String productName) {
        productsPage = new ProductsPage();
        productsPage.addProductToCart(productName);
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        checkoutPage = cartPage.proceedToCheckout();
    }

    @When("I fill in checkout information with first name {string} last name {string} and postal code {string}")
    public void iFillCheckoutInfo(String firstName, String lastName, String postalCode) {
        checkoutPage.fillCheckoutInfo(firstName, lastName, postalCode);
    }

    @When("I continue to order review")
    public void iContinueToOrderReview() {
        checkoutPage.clickContinue();
    }

    @When("I finish the order")
    public void iFinishTheOrder() {
        checkoutPage.finishOrder();
    }

    @Then("I should see the order confirmation message")
    public void iShouldSeeOrderConfirmation() {
        Assert.assertTrue(checkoutPage.isOrderComplete(),
                "Order confirmation should be displayed. Got: " + checkoutPage.getOrderConfirmation());
    }

    @When("I click continue without filling in checkout information")
    public void iClickContinueWithoutInfo() {
        checkoutPage.clickContinue();
    }

    @Then("I should see an error message about required fields")
    public void iShouldSeeRequiredFieldsError() {
        Assert.assertFalse(checkoutPage.getErrorMessage().isEmpty(),
                "Error message should be shown for empty fields");
    }
}
