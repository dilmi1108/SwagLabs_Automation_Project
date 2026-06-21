package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    // Locators
    private final By backToProductsButton = By.id("back-to-products");
    private final By productName = By.cssSelector(".inventory_details_name.large_size");
    private final By productDescription = By.cssSelector(".inventory_details_desc.large_size");
    private final By productPrice = By.className("inventory_details_price");
    private final By addToCartButton = By.cssSelector("button[id^='add-to-cart']");
    private final By removeButton = By.cssSelector("button[id^='remove']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return safeGetText(productName, "Product Name");
    }

    public String getProductDescription() {
        return safeGetText(productDescription, "Product Description");
    }

    public double getProductPrice() {
        String priceText = safeGetText(productPrice, "Product Price");
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public void clickAddToCart() {
        safeClick(addToCartButton, "Add to Cart Button");
    }

    public void clickRemove() {
        safeClick(removeButton, "Remove Button");
    }

    public void clickBackToProducts() {
        safeClick(backToProductsButton, "Back to Products Button");
    }

    public boolean isRemoveButtonDisplayed() {
        return safeIsDisplayed(removeButton, "Remove Button");
    }

    public boolean isAddToCartButtonDisplayed() {
        return safeIsDisplayed(addToCartButton, "Add to Cart Button");
    }
}
