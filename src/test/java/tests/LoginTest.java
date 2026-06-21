package tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;

@Listeners(TestListener.class)
public class LoginTest extends BaseTest {

    // ========== POSITIVE TEST CASES ==========

    @Test(priority = 1, description = "Testing login with correct username and password")
    public void testValidLoginWithStandardUser() {
        performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid standard_user credentials");
    }

    @Test(priority = 2, description = "Testing login with locked out user account")
    public void testLoginWithLockedOutUser() {
        performLogin("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked_out_user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"), "Error message should mention 'locked out'");
    }

    @Test(priority = 3, description = "Testing login with problem user credentials")
    public void testLoginWithProblemUser() {
        performLogin("problem_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid problem_user credentials");
    }

    @Test(priority = 4, description = "Testing login with performance glitch user")
    public void testLoginWithPerformanceGlitchUser() {
        performLogin("performance_glitch_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with performance_glitch_user");
    }

    @Test(priority = 5, description = "Testing login with error user credentials")
    public void testLoginWithErrorUser() {
        performLogin("error_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with error_user");
    }

    @Test(priority = 6, description = "Testing login with visual user credentials")
    public void testLoginWithVisualUser() {
        performLogin("visual_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with visual_user");
    }

    // ========== NEGATIVE TEST CASES ==========

    @Test(priority = 7, description = "Testing login with invalid username and valid password")
    public void testInvalidUsernameTest() {
        performLogin("invalid_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid username");
    }

    @Test(priority = 8, description = "Testing login with valid username and incorrect password")
    public void testInvalidPasswordTest() {
        performLogin("standard_user", "wrong_password");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid password");
    }

    @Test(priority = 9, description = "Testing login with both invalid credentials")
    public void testInvalidCredentialsTest() {
        performLogin("invalid_user", "wrong_password");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
    }

    @Test(priority = 10, description = "Testing login with empty username field")
    public void testEmptyUsernameTest() {
        performLogin("", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"), "Error should state Username is required");
    }

    @Test(priority = 11, description = "Testing login with empty password field")
    public void testEmptyPasswordTest() {
        performLogin("standard_user", "");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"), "Error should state Password is required");
    }

    @Test(priority = 12, description = "Testing login with both fields empty")
    public void testEmptyCredentialsTest() {
        performLogin("", "");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty credentials");
    }

    @Test(priority = 13, description = "Testing username field with leading/trailing spaces")
    public void testUsernameWithSpaces() {
        performLogin(" standard_user ", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for username with spaces");
    }

    @Test(priority = 14, description = "Testing password field with leading/trailing spaces")
    public void testPasswordWithSpaces() {
        performLogin("standard_user", " secret_sauce ");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for password with spaces");
    }

    @Test(priority = 15, description = "Testing username field with special characters")
    public void testSpecialCharactersInUsername() {
        performLogin("user@#$%", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for special characters in username");
    }

    @Test(priority = 16, description = "Testing SQL injection prevention")
    public void testSQLInjectionPrevention() {
        performLogin("' OR '1'='1", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should prevent SQL injection attacks");
    }

    @Test(priority = 17, description = "Testing case sensitivity for username")
    public void testCaseSensitiveUsername() {
        performLogin("STANDARD_USER", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should be case-sensitive for username");
    }

    @Test(priority = 18, description = "Testing case sensitivity for password")
    public void testCaseSensitivePassword() {
        performLogin("standard_user", "SECRET_SAUCE");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should be case-sensitive for password");
    }

    @Test(priority = 19, description = "Testing with extremely long username")
    public void testVeryLongUsername() {
        String longUsername = "a".repeat(500);
        performLogin(longUsername, "secret_sauce");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should handle very long username appropriately");
    }

    @Test(priority = 20, description = "Testing with extremely long password")
    public void testVeryLongPassword() {
        String longPassword = "a".repeat(500);
        performLogin("standard_user", longPassword);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Should handle very long password appropriately");
    }

    // ========== UI VALIDATION TEST CASES ==========

    @Test(priority = 21, description = "Verifying username field is visible on page load")
    public void testUsernameFieldInputTest() {
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field should be displayed on login page");
    }

    @Test(priority = 22, description = "Verifying password field masking is enabled")
    public void testPasswordFieldMaskingTest() {
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field should be displayed on login page");
    }

    @Test(priority = 23, description = "Verifying login button is visible and clickable")
    public void testLoginButtonDisplay() {
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button should be displayed on login page");
    }

    @Test(priority = 24, description = "Verifying login button is in enabled state")
    public void testLoginButtonEnabled() {
        Assert.assertTrue(loginPage.isLoginButtonEnabled(), "Login button should be enabled");
    }

    @Test(priority = 25, description = "Verifying page title contains application name")
    public void testPageTitle() {
        String pageTitle = getDriver().getTitle();
        Assert.assertTrue(pageTitle.contains("Swag Labs"), "Page title should contain 'Swag Labs'");
    }
}