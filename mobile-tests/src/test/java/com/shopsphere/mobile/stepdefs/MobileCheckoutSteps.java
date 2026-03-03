package com.shopsphere.mobile.stepdefs;
import com.shopsphere.config.AppiumDriverManager;
import com.shopsphere.screens.*;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Mobile Checkout Step Definitions - mirrors web CheckoutSteps.
 * Same Gherkin language, different Appium-based implementation.
 * Demonstrates cross-platform parity between web and mobile test suites.
 */
public class MobileCheckoutSteps {
    private ProductsScreen productsScreen;
    private CartScreen cartScreen;
    private CheckoutScreen checkoutScreen;

    // Tracks items added this scenario. After each add, that button becomes REMOVE
    // and shifts back — so the next ADD TO CART is always at index 0.
    private int itemsAddedToCart = 0;

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedInMobile(String username, String password) {
        // Reset counter on each new scenario (Background runs this before each scenario)
        itemsAddedToCart = 0;
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
        if (productsScreen == null) {
            productsScreen = new ProductsScreen(AppiumDriverManager.getDriver());
        }
        // Always tap index 0 — after each add the button becomes REMOVE
        // and the next product's ADD TO CART becomes the new index 0
        productsScreen.addProductToCartByIndex(0);
        itemsAddedToCart++;
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

    @When("I click continue without filling in checkout information")
    public void iClickContinueWithoutFilling() {
        checkoutScreen.tapContinueWithoutFilling();
    }

    @Then("I should see an error message about required fields")
    public void iShouldSeeErrorAboutRequiredFields() {
        Assert.assertTrue(checkoutScreen.isErrorDisplayed(), "Checkout error message should be displayed");
        Assert.assertFalse(checkoutScreen.getErrorMessage().isEmpty(), "Error message should not be empty");
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
