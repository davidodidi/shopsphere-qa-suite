package com.shopsphere.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailPage extends BasePage {

    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = "button[data-test^='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(id = "back-to-products")
    private WebElement backButton;

    public ProductDetailPage() { super(); }

    public String getProductName()        { return getText(productName); }
    public String getProductDescription() { return getText(productDescription); }
    public String getProductPrice()       { return getText(productPrice); }

    @Step("Adding product to cart from detail page")
    public ProductDetailPage addToCart() {
        click(addToCartButton);
        return this;
    }

    @Step("Going back to products list")
    public ProductsPage backToProducts() {
        click(backButton);
        return new ProductsPage();
    }

    @Override
    public boolean isLoaded() {
        try { return productName.isDisplayed(); }
        catch (Exception e) { return false; }
    }
}
