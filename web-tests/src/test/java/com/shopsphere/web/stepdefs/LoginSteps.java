package com.shopsphere.web.stepdefs;

import com.shopsphere.config.ConfigReader;
import com.shopsphere.pages.LoginPage;
import com.shopsphere.pages.ProductsPage;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.testng.Assert;

public class LoginSteps {
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private final ConfigReader config = ConfigReader.getInstance();

    @Given("I am on the ShopSphere login page")
    public void iAmOnLoginPage() {
        loginPage = new LoginPage();
        loginPage.navigateTo(config.getBaseUrl());
        Assert.assertTrue(loginPage.isLoaded(), "Login page should be loaded");
    }

    @When("I enter username {string} and password {string}")
    public void iEnterCredentials(String username, String password) {
        loginPage.enterUsername(username).enterPassword(password);
    }

    @When("I click the login button")
    public void iClickLoginButton() {
        productsPage = loginPage.clickLoginExpectingFailure().equals(loginPage)
                ? null
                : new ProductsPage();
        // Handle both success and failure paths
        try {
            productsPage = new ProductsPage();
        } catch (Exception ignored) {}
    }

    @Then("I should be redirected to the products page")
    public void iShouldBeRedirectedToProductsPage() {
        Assert.assertNotNull(productsPage, "Products page should not be null");
        Assert.assertTrue(productsPage.isLoaded(), "Products page should be loaded after login");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String title) {
        Assert.assertEquals(productsPage.getTitleText(), title,
                "Page title mismatch");
    }

    @Then("I should see the error message {string}")
    public void iShouldSeeErrorMessage(String expectedMessage) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedMessage,
                "Error message mismatch");
    }

    @Then("I should see an error message containing {string}")
    public void iShouldSeeErrorContaining(String partialMessage) {
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains(partialMessage),
                "Error should contain: " + partialMessage);
    }

    @Then("I should be on page {string}")
    public void iShouldBeOnPage(String page) {
        if (page.equals("products")) {
            Assert.assertTrue(new ProductsPage().isLoaded(), "Should be on products page");
        }
    }

    @Given("I am logged in as {string} with password {string}")
    public void iAmLoggedIn(String username, String password) {
        loginPage = new LoginPage();
        loginPage.navigateTo(config.getBaseUrl());
        productsPage = loginPage.loginAs(username, password);
    }
}
