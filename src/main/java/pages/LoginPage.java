package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By productsTitle = By.className("title");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        safeSendKeys(usernameField, username, "Username Field", false);
    }

    public void enterPassword(String password) {
        safeSendKeys(passwordField, password, "Password Field", true);
    }

    public void clickLoginButton() {
        safeClick(loginButton, "Login Button");
    }

    /**
     * Executes the login sequence.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isErrorMessageDisplayed() {
        return safeIsDisplayed(errorMessage, "Error Message Container");
    }

    public String getErrorMessage() {
        return safeGetText(errorMessage, "Error Message");
    }

    public boolean isLoginSuccessful() {
        try {
            // Wait for URL redirection to the inventory page
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            return safeIsDisplayed(productsTitle, "Inventory Page Title");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUsernameFieldDisplayed() {
        return safeIsDisplayed(usernameField, "Username Field");
    }

    public boolean isPasswordFieldDisplayed() {
        return safeIsDisplayed(passwordField, "Password Field");
    }

    public boolean isLoginButtonDisplayed() {
        return safeIsDisplayed(loginButton, "Login Button");
    }

    public boolean isLoginButtonEnabled() {
        return safeIsEnabled(loginButton, "Login Button");
    }

    public void clearUsername() {
        try {
            waitToBeVisible(usernameField).clear();
        } catch (Exception e) {
            logToReport(com.aventstack.extentreports.Status.WARNING, "Could not clear username: " + e.getMessage());
        }
    }

    public void clearPassword() {
        try {
            waitToBeVisible(passwordField).clear();
        } catch (Exception e) {
            logToReport(com.aventstack.extentreports.Status.WARNING, "Could not clear password: " + e.getMessage());
        }
    }
}