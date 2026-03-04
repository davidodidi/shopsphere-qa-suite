package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".inventory_item")
    private List<WebElement> productItems;

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "shopping_cart_container")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    public ProductsPage() {
        super();
    }

    @Step("Getting page title text")
    public String getTitleText() {
        return getText(pageTitle);
    }

    @Step("Getting all product names")
    public List<String> getAllProductNames() {
        return productItems.stream()
                .map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    @Step("Adding product '{productName}' to cart")
    public ProductsPage addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        productItems.stream()
                .filter(item -> item.findElement(By.cssSelector(".inventory_item_name"))
                        .getText().equals(productName))
                .findFirst()
                .ifPresent(item -> item.findElement(By.cssSelector("button")).click());
        return this;
    }

    @Step("Sorting products by: {sortOption}")
    public ProductsPage sortBy(String sortOption) {
        selectByVisibleText(By.cssSelector("[data-test='product-sort-container']"), sortOption);
        return this;
    }

    @Step("Clicking on product: {productName}")
    public ProductDetailPage clickProduct(String productName) {
        productItems.stream()
                .filter(item -> item.findElement(By.cssSelector(".inventory_item_name"))
                        .getText().equals(productName))
                .findFirst()
                .ifPresent(item -> item.findElement(By.cssSelector(".inventory_item_name")).click());
        WaitUtils.waitForUrlToContain("inventory-item");
        return new ProductDetailPage();
    }

    @Step("Navigating to cart")
    public CartPage goToCart() {
        click(cartIcon);
        WaitUtils.waitForUrlToContain("cart");
        return new CartPage();
    }

    public int getCartCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public int getProductCount() {
        return productItems.size();
    }

    @Override
    public boolean isLoaded() {
        try {
            return pageTitle.isDisplayed() && getTitleText().equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }
}
