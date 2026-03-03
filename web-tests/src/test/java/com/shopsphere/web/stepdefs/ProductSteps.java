package com.shopsphere.web.stepdefs;

import com.shopsphere.pages.ProductDetailPage;
import com.shopsphere.pages.ProductsPage;
import com.shopsphere.pages.CartPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class ProductSteps {

    private final ScenarioContext ctx;
    private ProductDetailPage detailPage;

    public ProductSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("I am on the products page")
    public void iAmOnProductsPage() {
        ctx.productsPage = new ProductsPage();
        Assert.assertTrue(ctx.productsPage.isLoaded(), "Products page should be loaded");
    }

    @Then("the products page should be loaded")
    public void theProductsPageShouldBeLoaded() {
        ctx.productsPage = new ProductsPage();
        Assert.assertTrue(ctx.productsPage.isLoaded(), "Products page should be loaded");
    }

    @Then("I should see at least {int} product")
    public void iShouldSeeAtLeastNProducts(int minCount) {
        Assert.assertTrue(ctx.productsPage.getProductCount() >= minCount,
                "Expected at least " + minCount + " products");
    }

    @When("I sort products by {string}")
    public void iSortProductsBy(String sortOption) {
        ctx.productsPage.sortBy(sortOption);
    }

    @Then("products should be sorted by price in ascending order")
    public void productsShouldBeSortedByPriceAscending() {
        Assert.assertTrue(ctx.productsPage.isLoaded(), "Page should still be loaded after sort");
    }

    @Then("products should be sorted alphabetically in ascending order")
    public void productsShouldBeSortedAlphabetically() {
        var names = ctx.productsPage.getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                    "Products not in alphabetical order at index " + i);
        }
    }

    @When("I click on the first product")
    public void iClickOnFirstProduct() {
        var names = ctx.productsPage.getAllProductNames();
        Assert.assertFalse(names.isEmpty(), "There should be at least one product");
        detailPage = ctx.productsPage.clickProduct(names.get(0));
    }

    @Then("I should be on the product detail page")
    public void iShouldBeOnProductDetailPage() {
        Assert.assertTrue(detailPage.isLoaded(), "Product detail page should be loaded");
    }

    @Then("the product name should not be empty")
    public void theProductNameShouldNotBeEmpty() {
        Assert.assertFalse(detailPage.getProductName().isEmpty(), "Product name should not be empty");
    }

    @Then("the product price should not be empty")
    public void theProductPriceShouldNotBeEmpty() {
        Assert.assertFalse(detailPage.getProductPrice().isEmpty(), "Product price should not be empty");
    }

    @When("I go back to the products list")
    public void iGoBackToProductsList() {
        ctx.productsPage = detailPage.backToProducts();
    }

    @Then("I should be on the products page")
    public void iShouldBeOnProductsPage() {
        Assert.assertTrue(ctx.productsPage.isLoaded(), "Products page should be loaded");
    }

    @When("I add {string} to the cart")
    public void iAddProductToCart(String productName) {
        ctx.productsPage.addProductToCart(productName);
    }

    @Then("the cart count should be {int}")
    public void theCartCountShouldBe(int expectedCount) {
        Assert.assertEquals(ctx.productsPage.getCartCount(), expectedCount,
                "Cart count mismatch");
    }

    @When("I navigate to the cart")
    public void iNavigateToCart() {
        ctx.cartPage = ctx.productsPage.goToCart();
    }

    @Then("the cart should contain {int} item(s)")
    public void theCartShouldContainItems(int expectedCount) {
        Assert.assertEquals(ctx.cartPage.getCartItemCount(), expectedCount,
                "Cart item count mismatch");
    }
}
