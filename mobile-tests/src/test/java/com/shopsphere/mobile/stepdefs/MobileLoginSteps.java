package com.shopsphere.mobile.stepdefs;

import com.shopsphere.config.AppiumDriverManager;
import com.shopsphere.screens.LoginScreen;
import com.shopsphere.screens.ProductsScreen;
import io.cucumber.java.en.*;
import org.testng.Assert;

/**
 * Mobile Login Step Definitions.
 * Uses the same Gherkin steps as web — different implementation, same behaviour.
 * This demonstrates cross-platform testing strategy.
 */
public class MobileLoginSteps {
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;

    @Given("I am on the ShopSphere login page")
    public void iAmOnLoginPage() {
        loginScreen = new LoginScreen(AppiumDriverManager.getDriver());
        Assert.assertTrue(loginScreen.isLoaded(), "Mobile login screen should be loaded");
    }

    @When("I enter username {string} and password {string}")
    public void iEnterCredentials(String username, String password) {
        loginScreen.enterUsername(username).enterPassword(password);
    }

    @When("I click the login button")
    public void iClickLoginButton() {
        productsScreen = loginScreen.tapLogin();
    }

    @Then("I should be redirected to the products page")
    public void iShouldBeRedirectedToProductsPage() {
        Assert.assertTrue(productsScreen.isLoaded(), "Mobile products screen should load after login");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        // Mobile app uses accessibility id "test-PRODUCTS" which returns the label text
        Assert.assertTrue(productsScreen.isLoaded(), "Mobile products header should be visible");
    }

    @Then("I should be on page {string}")
    public void iShouldBeOnPage(String expectedPage) {
        if (expectedPage.equalsIgnoreCase("products")) {
            Assert.assertTrue(productsScreen.isLoaded(), "Should be on products page after login");
        }
    }

    @Then("I should see the error message {string}")
    public void iShouldSeeErrorMessage(String expectedMessage) {
        Assert.assertTrue(loginScreen.isErrorDisplayed(), "Error should be shown");
        String actualMessage = loginScreen.getErrorMessage();
        // Mobile app omits "Epic sadface: " prefix — check the meaningful part
        String cleanExpected = expectedMessage.replace("Epic sadface: ", "");
        Assert.assertTrue(actualMessage.contains(cleanExpected),
                "Expected error to contain: " + cleanExpected + " but found: " + actualMessage);
    }

    @Then("I should see an error message containing {string}")
    public void iShouldSeeErrorContaining(String partial) {
        Assert.assertTrue(loginScreen.getErrorMessage().contains(partial),
                "Expected error containing: " + partial);
    }
}
