package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExtentReportManager;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators - Updated for Sauce Demo
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By productsTitle = By.className("title");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void safeLog(Status status, String message) {
        try {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(status, message);
            }
        } catch (Exception e) {
            System.out.println(message);
        }
    }

    public void navigateToLoginPage() {
        try {
            safeLog(Status.INFO, "Navigating to login page");
            // Page is already loaded in BaseTest
            safeLog(Status.PASS, "Successfully navigated to login page");
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to navigate to login page: " + e.getMessage());
        }
    }

    public void enterUsername(String username) {
        try {
            safeLog(Status.INFO, "Entering username: " + username);
            WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            usernameElement.clear();
            usernameElement.sendKeys(username);
            safeLog(Status.PASS, "Successfully entered username: " + username);
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to enter username: " + e.getMessage());
            throw e;
        }
    }

    public void enterPassword(String password) {
        try {
            safeLog(Status.INFO, "Entering password");
            WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            passwordElement.clear();
            passwordElement.sendKeys(password);
            safeLog(Status.PASS, "Successfully entered password");
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to enter password: " + e.getMessage());
            throw e;
        }
    }

    public void clickLoginButton() {
        try {
            safeLog(Status.INFO, "Clicking login button");
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
            safeLog(Status.PASS, "Successfully clicked login button");
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to click login button: " + e.getMessage());
            throw e;
        }
    }

    public void login(String username, String password) {
        safeLog(Status.INFO, "Performing login with username: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();

        // Wait a moment for the action to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
            if (isDisplayed) {
                String errorText = getErrorMessage();
                safeLog(Status.INFO, "Error message displayed: " + errorText);
            }
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginSuccessful() {
        try {
            safeLog(Status.INFO, "Verifying login success");
            // Check if we're on the inventory page
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            boolean success = driver.findElement(productsTitle).isDisplayed();
            if (success) {
                safeLog(Status.PASS, "Login successful - User redirected to inventory page");
            }
            return success;
        } catch (Exception e) {
            safeLog(Status.INFO, "Login was not successful or page not loaded");
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isUsernameFieldDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(usernameField).isDisplayed();
            if (isDisplayed) {
                safeLog(Status.PASS, "Username field is displayed on login page");
            }
            return isDisplayed;
        } catch (Exception e) {
            safeLog(Status.FAIL, "Username field is not displayed");
            return false;
        }
    }

    public boolean isPasswordFieldDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(passwordField).isDisplayed();
            if (isDisplayed) {
                safeLog(Status.PASS, "Password field is displayed on login page");
            }
            return isDisplayed;
        } catch (Exception e) {
            safeLog(Status.FAIL, "Password field is not displayed");
            return false;
        }
    }

    public boolean isLoginButtonDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(loginButton).isDisplayed();
            if (isDisplayed) {
                safeLog(Status.PASS, "Login button is displayed on login page");
            }
            return isDisplayed;
        } catch (Exception e) {
            safeLog(Status.FAIL, "Login button is not displayed");
            return false;
        }
    }

    public boolean isLoginButtonEnabled() {
        try {
            boolean isEnabled = driver.findElement(loginButton).isEnabled();
            if (isEnabled) {
                safeLog(Status.PASS, "Login button is enabled");
            }
            return isEnabled;
        } catch (Exception e) {
            safeLog(Status.FAIL, "Login button is not enabled");
            return false;
        }
    }

    public void clearUsername() {
        driver.findElement(usernameField).clear();
    }

    public void clearPassword() {
        driver.findElement(passwordField).clear();
    }
}