package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartScreen extends BaseScreen {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Cart Item']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Cart Item']")
    private List<WebElement> cartItems;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "test-CHECKOUT")
    private WebElement checkoutButton;

    public CartScreen(AppiumDriver driver) { super(driver); }

    public int getItemCount() { return cartItems.size(); }

    @Step("Tapping checkout button")
    public CheckoutScreen proceedToCheckout() {
        tap(checkoutButton);
        return new CheckoutScreen(driver);
    }

    @Override
    public boolean isLoaded() { return driver.getCurrentUrl().contains("cart"); }
}
