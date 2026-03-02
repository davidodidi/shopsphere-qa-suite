package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ProductsScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-PRODUCTS")
    @iOSXCUITFindBy(accessibility = "test-PRODUCTS")
    private WebElement productsHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Item']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Item']")
    private List<WebElement> productItems;

    @AndroidFindBy(accessibility = "test-Cart")
    @iOSXCUITFindBy(accessibility = "test-Cart")
    private WebElement cartButton;

    public ProductsScreen(AppiumDriver driver) { super(driver); }

    public String getHeaderText() { return getText(productsHeader); }
    public int getProductCount()  { return productItems.size(); }

    @Step("Adding product at index {index} to cart")
    public ProductsScreen addProductToCartByIndex(int index) {
        if (index < productItems.size()) {
            WebElement product = productItems.get(index);
            // Tap Add to Cart button within product
            log.info("Adding product at index {} to cart", index);
        }
        return this;
    }

    @Step("Tapping cart icon")
    public CartScreen goToCart() {
        tap(cartButton);
        return new CartScreen(driver);
    }

    @Override
    public boolean isLoaded() {
        try { return productsHeader.isDisplayed(); }
        catch (Exception e) { return false; }
    }
}
