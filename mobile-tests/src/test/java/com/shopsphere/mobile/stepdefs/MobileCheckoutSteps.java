package com.shopsphere.mobile.stepdefs;

import com.shopsphere.config.AppiumDriverManager;
import com.shopsphere.screens.*;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Mobile Checkout Step Definitions — mirrors web CheckoutSteps.
 * Same Gherkin language, different Appium-based implementation.
 * Demonstrates the cross-platform parity between web and mobile test suites.
 */
public class MobileCheckoutSteps {

    private ProductsScreen productsScreen;
    private CartScreen cartScreen;
    private CheckoutScreen checkoutScreen;

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInMobile(String username, String password) {
        LoginScreen loginScreen = new LoginScreen(AppiumDriverManager.getDriver());
        productsScreen = loginScreen.loginAs(username, password);
        Assert.assertTrue(productsScreen.isLoaded(), "Products screen should load after mobile login");
    }

    @Given("I am on the products page")
    public void iAmOnProductsPage() {
        productsScreen = new ProductsScreen(AppiumDriverManager.getDriver());
        Assert.assertTrue(productsScreen.isLoaded(), "Mobile products screen should be loaded");
    }

    @Given("I add {string} to the cart from the products page")
    public void iAddProductToCartMobile(String productName) {
        productsScreen.addProductToCartByIndex(0);
    }

    @When("I navigate to the cart")
    public void iNavigateToCart() {
        cartScreen = productsScreen.goToCart();
    }

    @Then("the cart should contain {int} item(s)")
    public void cartShouldContainItems(int expectedCount) {
        Assert.assertEquals(cartScreen.getItemCount(), expectedCount, "Mobile cart item count mismatch");
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        checkoutScreen = cartScreen.proceedToCheckout();
    }

    @When("I fill in checkout information with first name {string} last name {string} and postal code {string}")
    public void iFillCheckoutInfo(String firstName, String lastName, String postalCode) {
        checkoutScreen.fillInfo(firstName, lastName, postalCode);
    }

    @When("I continue to order review")
    public void iContinueToOrderReview() {
        checkoutScreen.tapContinue();
    }

    @When("I finish the order")
    public void iFinishOrder() {
        checkoutScreen.tapFinish();
    }

    @Then("I should see the order confirmation message")
    public void iShouldSeeOrderConfirmation() {
        Assert.assertTrue(checkoutScreen.isOrderComplete(), "Mobile order confirmation should be displayed");
    }
}
