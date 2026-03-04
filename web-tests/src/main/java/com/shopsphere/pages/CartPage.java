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
        scrollToElement(checkoutButton);
        jsClick(checkoutButton);
        WaitUtils.waitForUrlToContain("checkout-step-one");
        // Wait for the first-name field to be present and visible in the DOM
        // before constructing CheckoutPage. jsClick() bypasses normal browser
        // focus/navigation events, so the URL changes before React has fully
        // rendered the checkout form. PageFactory.initElements() in the
        // CheckoutPage constructor binds proxies immediately — if the DOM is
        // not ready those proxies resolve to non-existent elements and all
        // sendKeys() calls silently do nothing.
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
