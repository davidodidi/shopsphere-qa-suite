package com.shopsphere.pages;

import com.shopsphere.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public CartPage() { super(); }

    @Step("Proceeding to checkout")
    public CheckoutPage proceedToCheckout() {
        // Use a real WebDriver click, not jsClick. jsClick fires a JS synthetic
        // event which bypasses the browser's trusted-user-gesture chain. In
        // headless Chrome on Jenkins this leaves the page in a state where
        // sendKeys() on the checkout form inputs is silently ignored. A normal
        // WebDriver click goes through the full browser input event pipeline,
        // matching exactly what LoginPage does — and login works fine.
        scrollToElement(checkoutButton);
        WaitUtils.waitForClickable(checkoutButton).click();
        WaitUtils.waitForUrlToContain("checkout-step-one");
        WaitUtils.waitForVisibility(By.id("first-name"));
        return new CheckoutPage();
    }

    public List<String> getCartItemNames() {
        return cartItems.stream()
                .map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    public int getCartItemCount() { return cartItems.size(); }

    @Step("Removing item '{itemName}' from cart")
    public CartPage removeItem(String itemName) {
        cartItems.stream()
                .filter(item -> item.findElement(By.cssSelector(".inventory_item_name"))
                        .getText().equals(itemName))
                .findFirst()
                .ifPresent(item -> {
                    WebElement button = item.findElement(By.cssSelector("button"));
                    scrollToElement(button);
                    jsClick(button);
                });
        return this;
    }

    @Override
    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("cart");
    }
}
