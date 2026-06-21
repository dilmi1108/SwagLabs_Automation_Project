package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    // Locators
    private final By pageTitle = By.className("title");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productItems = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By productImages = By.cssSelector(".inventory_item_img img");
    private final By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");
    private final By removeButtons = By.cssSelector("button[id^='remove']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryPageLoaded() {
        return safeIsDisplayed(pageTitle, "Inventory Page Title") 
                && getCurrentUrl().contains("inventory.html");
    }

    public String getPageTitle() {
        return safeGetText(pageTitle, "Page Title");
    }

    public int getProductCount() {
        try {
            waitToBeVisible(productItems);
            int count = driver.findElements(productItems).size();
            logToReport(Status.INFO, "Product count on page: " + count);
            return count;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to count products. Exception: " + e.getMessage());
            return 0;
        }
    }

    public List<String> getAllProductNames() {
        try {
            waitToBeVisible(productNames);
            List<WebElement> elements = driver.findElements(productNames);
            List<String> names = new ArrayList<>();
            for (WebElement element : elements) {
                names.add(element.getText());
            }
            return names;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get all product names. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Double> getAllProductPrices() {
        try {
            waitToBeVisible(productPrices);
            List<WebElement> elements = driver.findElements(productPrices);
            List<Double> prices = new ArrayList<>();
            for (WebElement element : elements) {
                prices.add(Double.parseDouble(element.getText().replace("$", "")));
            }
            return prices;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get all product prices. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private WebElement getProductContainerByName(String productName) {
        waitToBeVisible(productItems);
        List<WebElement> items = driver.findElements(productItems);
        for (WebElement item : items) {
            WebElement nameEl = item.findElement(productNames);
            if (nameEl.getText().equalsIgnoreCase(productName)) {
                return item;
            }
        }
        throw new RuntimeException("Product container not found for name: " + productName);
    }

    public void addProductToCartByName(String productName) {
        try {
            int initialCount = getCartCount();
            WebElement container = getProductContainerByName(productName);
            WebElement btn = container.findElement(addToCartButtons);
            safeClick(btn, "Add to Cart button for " + productName);
            wait.until(d -> getCartCount() == initialCount + 1);
            logToReport(Status.PASS, "Added to cart: " + productName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to add product '" + productName + "' to cart. Exception: " + e.getMessage());
            throw e;
        }
    }

    public void removeProductFromCartByName(String productName) {
        try {
            int initialCount = getCartCount();
            WebElement container = getProductContainerByName(productName);
            WebElement btn = container.findElement(removeButtons);
            safeClick(btn, "Remove button for " + productName);
            wait.until(d -> getCartCount() == initialCount - 1);
            logToReport(Status.PASS, "Removed from cart: " + productName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to remove product '" + productName + "' from cart. Exception: " + e.getMessage());
            throw e;
        }
    }

    public void addProductToCart(int productIndex) {
        try {
            int initialCount = getCartCount();
            waitToBeVisible(addToCartButtons);
            List<WebElement> buttons = driver.findElements(addToCartButtons);
            String productName = driver.findElements(productNames).get(productIndex).getText();
            safeClick(buttons.get(productIndex), "Add to Cart button for " + productName);
            wait.until(d -> getCartCount() == initialCount + 1);
            logToReport(Status.PASS, "Added product at index " + productIndex + " (" + productName + ") to cart");
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to add product at index " + productIndex + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    public void removeProductFromCart(int productIndex) {
        try {
            int initialCount = getCartCount();
            waitToBeVisible(removeButtons);
            List<WebElement> buttons = driver.findElements(removeButtons);
            String productName = driver.findElements(productNames).get(productIndex).getText();
            safeClick(buttons.get(productIndex), "Remove button for " + productName);
            wait.until(d -> getCartCount() == initialCount - 1);
            logToReport(Status.PASS, "Removed product at index " + productIndex + " from cart");
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to remove product at index " + productIndex + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    public boolean isProductInRemoveState(String productName) {
        try {
            WebElement container = getProductContainerByName(productName);
            List<WebElement> btns = container.findElements(removeButtons);
            return !btns.isEmpty() && btns.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickProductByName(String productName) {
        try {
            WebElement container = getProductContainerByName(productName);
            WebElement nameLink = container.findElement(productNames);
            nameLink.click();
            logToReport(Status.PASS, "Clicked on product link for details: " + productName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to click on product name '" + productName + "'. Exception: " + e.getMessage());
            throw e;
        }
    }

    public void sortProducts(String sortOption) {
        selectDropdownByVisibleText(sortDropdown, sortOption, "Product Sort Dropdown");
    }

    public boolean areProductImagesDisplayed() {
        try {
            waitToBeVisible(productImages);
            // Give images time to load assets over network
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            List<WebElement> images = driver.findElements(productImages);
            if (images.isEmpty()) {
                logToReport(Status.FAIL, "No product images found on page");
                return false;
            }

            boolean allDisplayed = true;
            int validImages = 0;

            for (WebElement img : images) {
                try {
                    String src = img.getAttribute("src");
                    boolean isDisplayed = img.isDisplayed();

                    // Known problem_user has garbage image URL to break the site
                    if (src != null && !src.isEmpty() && isDisplayed && !src.contains("WithGarbageOnItToBreakTheUrl")) {
                        validImages++;
                    } else {
                        allDisplayed = false;
                    }
                } catch (Exception e) {
                    allDisplayed = false;
                }
            }

            if (allDisplayed) {
                logToReport(Status.PASS, "All product images are displayed and valid");
            } else {
                logToReport(Status.WARNING, validImages + " out of " + images.size() + " product images are valid");
            }
            return allDisplayed;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to verify product images. Exception: " + e.getMessage());
            return false;
        }
    }
}