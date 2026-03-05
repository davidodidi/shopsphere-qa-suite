package com.shopsphere.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * FIX: Added PageFactory.initElements() with AppiumFieldDecorator — same root
 * cause fix as LoginScreen. Without it @AndroidFindBy fields are null.
 */
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

    public ProductsScreen(AppiumDriver driver) {
        super(driver);
        // CRITICAL FIX: same as LoginScreen — must init annotated fields.
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public String getHeaderText() { return getText(productsHeader); }
    public int getProductCount()  { return productItems.size(); }

    @Step("Adding product at index {index} to cart")
    public ProductsScreen addProductToCartByIndex(int index) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.view.ViewGroup[@content-desc='test-ADD TO CART']")));

            List<WebElement> addToCartButtons = driver.findElements(
                By.xpath("//android.view.ViewGroup[@content-desc='test-ADD TO CART']"));

            log.info("Found {} ADD TO CART buttons", addToCartButtons.size());

            if (index < addToCartButtons.size()) {
                addToCartButtons.get(index).click();
                log.info("Tapped ADD TO CART at index {}", index);

                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//android.view.ViewGroup[@content-desc='test-REMOVE']")));
                log.info("Product successfully added - REMOVE button appeared");
            } else {
                log.warn("No ADD TO CART button at index {}. Total found: {}", index, addToCartButtons.size());
            }
        } catch (Exception e) {
            log.error("Failed to add product to cart: {}", e.getMessage());
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
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(d -> {
                        try {
                            var elements = d.findElements(By.xpath("//*[@content-desc='test-PRODUCTS']"));
                            if (!elements.isEmpty() && elements.get(0).isDisplayed()) return true;
                            var byText = d.findElements(By.xpath("//*[contains(@text,'PRODUCTS')]"));
                            return !byText.isEmpty() && byText.get(0).isDisplayed();
                        } catch (Exception e) {
                            return false;
                        }
                    });
            return true;
        } catch (Exception e) {
            log.warn("Products screen not loaded within timeout: {}", e.getMessage());
            return false;
        }
    }
}
