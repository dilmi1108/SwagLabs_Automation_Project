package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private final By cartTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By removeButtons = By.cssSelector("button[id^='remove']");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCartPageLoaded() {
        return safeIsDisplayed(cartTitle, "Cart Page Title") 
                && getCurrentUrl().contains("cart.html");
    }

    public int getCartItemsCount() {
        try {
            return driver.findElements(cartItems).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getCartItemNames() {
        try {
            List<WebElement> elements = driver.findElements(itemNames);
            List<String> names = new ArrayList<>();
            for (WebElement element : elements) {
                names.add(element.getText());
            }
            return names;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get item names from cart. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Double> getCartItemPrices() {
        try {
            List<WebElement> elements = driver.findElements(itemPrices);
            List<Double> prices = new ArrayList<>();
            for (WebElement element : elements) {
                prices.add(Double.parseDouble(element.getText().replace("$", "")));
            }
            return prices;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get item prices from cart. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private WebElement getCartItemByName(String name) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            WebElement nameEl = item.findElement(itemNames);
            if (nameEl.getText().equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new RuntimeException("Cart item not found by name: " + name);
    }

    public void removeItemByName(String name) {
        try {
            int initialCount = getCartItemsCount();
            WebElement item = getCartItemByName(name);
            WebElement removeBtn = item.findElement(removeButtons);
            removeBtn.click();
            wait.until(d -> getCartItemsCount() == initialCount - 1);
            logToReport(Status.PASS, "Removed item from cart: " + name);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to remove item '" + name + "' from cart. Exception: " + e.getMessage());
            throw e;
        }
    }

    public void removeItemByIndex(int index) {
        try {
            int initialCount = getCartItemsCount();
            waitToBeVisible(removeButtons);
            List<WebElement> buttons = driver.findElements(removeButtons);
            buttons.get(index).click();
            wait.until(d -> getCartItemsCount() == initialCount - 1);
            logToReport(Status.PASS, "Removed item at index " + index + " from cart");
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to remove item at index " + index + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    public void clickContinueShopping() {
        safeClick(continueShoppingButton, "Continue Shopping Button");
    }

    public void clickCheckout() {
        safeClick(checkoutButton, "Checkout Button");
    }
}
