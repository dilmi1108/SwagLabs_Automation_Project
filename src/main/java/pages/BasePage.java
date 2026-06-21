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
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Shared Header and Sidebar Menu Locators
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By closeMenuButton = By.id("react-burger-cross-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetStateLink = By.id("reset_sidebar_link");
    private final By allItemsLink = By.id("inventory_sidebar_link");
    private final By aboutLink = By.id("about_sidebar_link");
    private final By shoppingCartIcon = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Load explicit wait duration from configuration or default to 15 seconds
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Safely logs messages to the Extent Reports.
     */
    protected void logToReport(Status status, String message) {
        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(status, message);
            }
        } catch (Exception e) {
            System.out.println(status + ": " + message);
        }
    }

    // ========== Reusable WebDriver Interaction Wrapper Methods ==========

    protected WebElement waitToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void safeClick(WebElement element, String elementName) {
        try {
            // Scroll element into center of viewport for stability
            try {
                org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
                executor.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
            } catch (Exception ignored) {}

            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            logToReport(Status.PASS, "Clicked on: " + elementName);
        } catch (Exception e) {
            logToReport(Status.WARNING, "Standard click failed for " + elementName + ". Retrying with JavaScript click. Exception: " + e.getMessage());
            try {
                org.openqa.selenium.JavascriptExecutor executor = (org.openqa.selenium.JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);
                logToReport(Status.PASS, "Clicked on: " + elementName + " (via JS)");
            } catch (Exception ex) {
                logToReport(Status.FAIL, "Failed to click on: " + elementName + ". Exception: " + ex.getMessage());
                throw ex;
            }
        }
    }

    protected void safeClick(By locator, String elementName) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            safeClick(element, elementName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to locate element " + elementName + " for clicking. Exception: " + e.getMessage());
            throw e;
        }
    }

    protected void safeSendKeys(By locator, String text, String elementName, boolean maskValue) {
        try {
            WebElement element = waitToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            String logText = maskValue ? "********" : text;
            logToReport(Status.PASS, "Entered '" + logText + "' in: " + elementName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to enter text in: " + elementName + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    protected String safeGetText(By locator, String elementName) {
        try {
            String text = waitToBeVisible(locator).getText();
            logToReport(Status.INFO, "Retrieved text from " + elementName + ": '" + text + "'");
            return text;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get text from: " + elementName + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    protected boolean safeIsDisplayed(By locator, String elementName) {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            logToReport(Status.INFO, elementName + " is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logToReport(Status.INFO, elementName + " is not displayed.");
            return false;
        }
    }

    protected boolean safeIsEnabled(By locator, String elementName) {
        try {
            boolean isEnabled = wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isEnabled();
            logToReport(Status.INFO, elementName + " is enabled: " + isEnabled);
            return isEnabled;
        } catch (Exception e) {
            logToReport(Status.INFO, elementName + " is not enabled.");
            return false;
        }
    }

    protected void selectDropdownByVisibleText(By locator, String text, String dropdownName) {
        try {
            WebElement element = waitToBeVisible(locator);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            logToReport(Status.PASS, "Selected option '" + text + "' from dropdown: " + dropdownName);
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to select option '" + text + "' from dropdown: " + dropdownName + ". Exception: " + e.getMessage());
            throw e;
        }
    }

    // ========== Shared Header & Sidebar Navigation Actions ==========

    public void openSideMenu() {
        logToReport(Status.INFO, "Opening sidebar menu");
        safeClick(menuButton, "Sidebar Menu Button");
        // Wait for menu wrap to have aria-hidden="false" or items to be visible
        wait.until(ExpectedConditions.attributeToBe(By.className("bm-menu-wrap"), "aria-hidden", "false"));
        waitToBeVisible(allItemsLink);
        // Wait a short moment for transition animation to stabilize elements
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    public void closeSideMenu() {
        logToReport(Status.INFO, "Closing sidebar menu");
        safeClick(closeMenuButton, "Close Sidebar Button");
        // Wait for menu wrap to have aria-hidden="true" or items to be invisible
        wait.until(ExpectedConditions.attributeToBe(By.className("bm-menu-wrap"), "aria-hidden", "true"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(allItemsLink));
    }

    public void clickLogout() {
        logToReport(Status.INFO, "Initiating Logout procedure");
        openSideMenu();
        safeClick(logoutLink, "Logout sidebar link");
        // Wait for redirection to login page
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("index.html"),
                ExpectedConditions.urlMatches("https://www.saucedemo.com/?$"),
                ExpectedConditions.visibilityOfElementLocated(By.id("login-button"))
        ));
    }

    public void clickResetAppState() {
        logToReport(Status.INFO, "Resetting App State");
        openSideMenu();
        safeClick(resetStateLink, "Reset App State sidebar link");
        closeSideMenu();
        driver.navigate().refresh();
    }

    public void clickAllItemsLink() {
        logToReport(Status.INFO, "Navigating to Product Inventory Page");
        openSideMenu();
        safeClick(allItemsLink, "All Items link");
    }

    public void clickAboutLink() {
        logToReport(Status.INFO, "Navigating to About Page");
        openSideMenu();
        safeClick(aboutLink, "About link");
    }

    public void clickShoppingCart() {
        logToReport(Status.INFO, "Navigating to Shopping Cart");
        safeClick(shoppingCartIcon, "Shopping Cart Icon");
    }

    public int getCartCount() {
        try {
            if (driver.findElements(cartBadge).isEmpty()) {
                logToReport(Status.INFO, "Cart badge is not present (0 items)");
                return 0;
            }
            String countText = driver.findElement(cartBadge).getText();
            int count = countText.isEmpty() ? 0 : Integer.parseInt(countText);
            logToReport(Status.INFO, "Current Cart count: " + count);
            return count;
        } catch (Exception e) {
            logToReport(Status.INFO, "Cart badge contains no text or is absent (0 items)");
            return 0;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
