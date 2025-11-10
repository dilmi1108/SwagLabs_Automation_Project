package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExtentReportManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By pageTitle = By.className("title");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productItems = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By productImages = By.cssSelector(".inventory_item_img img");
    private final By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");
    private final By logoutLink = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isInventoryPageLoaded() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Verifying inventory page loaded");
            boolean loaded = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed();
            if (loaded) {
                ExtentReportManager.getTest().log(Status.PASS, "✓ Inventory page loaded successfully");
            }
            return loaded;
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Inventory page failed to load");
            return false;
        }
    }

    public String getPageTitle() {
        try {
            String title = driver.findElement(pageTitle).getText();
            ExtentReportManager.getTest().log(Status.INFO, "Page title: '" + title + "'");
            return title;
        } catch (Exception e) {
            return "";
        }
    }

    public int getProductCount() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Counting products on inventory page");
            int count = driver.findElements(productItems).size();
            ExtentReportManager.getTest().log(Status.PASS, "✓ Found " + count + " products");
            return count;
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to count products");
            return 0;
        }
    }

    public List<String> getAllProductNames() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Fetching all product names");
            List<String> names = new ArrayList<>();
            for (WebElement element : driver.findElements(productNames)) {
                names.add(element.getText());
            }
            ExtentReportManager.getTest().log(Status.PASS, "✓ Retrieved " + names.size() + " product names");
            return names;
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to fetch product names");
            return new ArrayList<>();
        }
    }

    public List<Double> getAllProductPrices() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Fetching all product prices");
            List<Double> prices = new ArrayList<>();
            for (WebElement element : driver.findElements(productPrices)) {
                prices.add(Double.parseDouble(element.getText().replace("$", "")));
            }
            ExtentReportManager.getTest().log(Status.PASS, "✓ Retrieved " + prices.size() + " product prices");
            return prices;
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to fetch prices");
            return new ArrayList<>();
        }
    }

    public void addProductToCart(int productIndex) {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Adding product #" + (productIndex + 1) + " to cart");
            String productName = driver.findElements(productNames).get(productIndex).getText();
            driver.findElements(addToCartButtons).get(productIndex).click();
            ExtentReportManager.getTest().log(Status.PASS, "✓ Added to cart: " + productName);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to add product: " + e.getMessage());
        }
    }

    public int getCartCount() {
        try {
            String count = driver.findElement(cartBadge).getText();
            ExtentReportManager.getTest().log(Status.INFO, "Cart contains " + count + " item(s)");
            return Integer.parseInt(count);
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.INFO, "Cart is empty");
            return 0;
        }
    }

    public void sortProducts(String sortOption) {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Sorting products by: " + sortOption);
            Select select = new Select(driver.findElement(sortDropdown));
            select.selectByVisibleText(sortOption);
            Thread.sleep(500);
            ExtentReportManager.getTest().log(Status.PASS, "✓ Products sorted successfully");
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to sort: " + e.getMessage());
        }
    }

    public boolean areProductImagesDisplayed() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Checking if product images are displayed");
            List<WebElement> images = driver.findElements(productImages);
            boolean allDisplayed = images.stream().allMatch(WebElement::isDisplayed);
            if (allDisplayed) {
                ExtentReportManager.getTest().log(Status.PASS, "✓ All " + images.size() + " product images displayed");
            }
            return allDisplayed;
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to verify images");
            return false;
        }
    }

    public void openMenu() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Opening navigation menu");
            driver.findElement(menuButton).click();
            Thread.sleep(500);
            ExtentReportManager.getTest().log(Status.PASS, "✓ Menu opened");
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to open menu: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Logging out");
            openMenu();
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
            ExtentReportManager.getTest().log(Status.PASS, "✓ Logged out successfully");
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to logout: " + e.getMessage());
        }
    }

    public void clickShoppingCart() {
        try {
            ExtentReportManager.getTest().log(Status.INFO, "Navigating to shopping cart");
            driver.findElement(shoppingCartIcon).click();
            ExtentReportManager.getTest().log(Status.PASS, "✓ Opened shopping cart");
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to open cart: " + e.getMessage());
        }
    }
}
