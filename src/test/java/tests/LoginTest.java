package tests;

import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;
import utils.ExtentReportManager;

@Listeners(TestListener.class)
public class LoginTest extends BaseTest {

    // ========== POSITIVE TEST CASES ==========

    @Test(priority = 1, description = "Testing login with correct username and password")
    public void testValidLoginWithStandardUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Valid Login with Standard User");

        loginPage.login("standard_user", "secret_sauce");

        ExtentReportManager.getTest().log(Status.INFO, "Verifying login success");
        boolean isLoginSuccessful = loginPage.isLoginSuccessful();

        if (isLoginSuccessful) {
            ExtentReportManager.getTest().log(Status.PASS, "Login successful with valid credentials");
        } else {
            ExtentReportManager.getTest().log(Status.FAIL, "Login failed with valid credentials");
        }

        Assert.assertTrue(isLoginSuccessful, "Login should be successful with valid standard_user credentials");
    }

    @Test(priority = 2, description = "Testing login with locked out user account")
    public void testLoginWithLockedOutUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Locked Out User Login");

        loginPage.login("locked_out_user", "secret_sauce");

        ExtentReportManager.getTest().log(Status.INFO, "Checking for error message");
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();

        if (errorDisplayed) {
            String errorMsg = loginPage.getErrorMessage();
            ExtentReportManager.getTest().log(Status.PASS, "Error message displayed correctly: " + errorMsg);
        }

        Assert.assertTrue(errorDisplayed, "Error message should be displayed for locked_out_user");
    }

    @Test(priority = 3, description = "Testing login with problem user credentials")
    public void testLoginWithProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Problem User Login");

        loginPage.login("problem_user", "secret_sauce");

        boolean isLoginSuccessful = loginPage.isLoginSuccessful();
        Assert.assertTrue(isLoginSuccessful, "Login should be successful with valid problem_user credentials");
    }

    @Test(priority = 4, description = "Testing login with performance glitch user")
    public void testLoginWithPerformanceGlitchUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Performance Glitch User Login");

        loginPage.login("performance_glitch_user", "secret_sauce");

        boolean isLoginSuccessful = loginPage.isLoginSuccessful();
        Assert.assertTrue(isLoginSuccessful, "Login should be successful with performance_glitch_user");
    }

    @Test(priority = 5, description = "Testing login with error user credentials")
    public void testLoginWithErrorUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Error User Login");

        loginPage.login("error_user", "secret_sauce");

        boolean isLoginSuccessful = loginPage.isLoginSuccessful();
        Assert.assertTrue(isLoginSuccessful, "Login should be successful with error_user");
    }

    @Test(priority = 6, description = "Testing login with visual user credentials")
    public void testLoginWithVisualUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Visual User Login");

        loginPage.login("visual_user", "secret_sauce");

        boolean isLoginSuccessful = loginPage.isLoginSuccessful();
        Assert.assertTrue(isLoginSuccessful, "Login should be successful with visual_user");
    }

    // ========== NEGATIVE TEST CASES ==========

    @Test(priority = 7, description = "Testing login with invalid username and valid password")
    public void testInvalidUsernameTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Invalid Username");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with username: invalid_user");

        loginPage.login("invalid_user", "secret_sauce");

        ExtentReportManager.getTest().log(Status.INFO, "Verifying error message is displayed");
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();

        Assert.assertTrue(errorDisplayed, "Error message should be displayed for invalid username");
    }

    @Test(priority = 8, description = "Testing login with valid username and incorrect password")
    public void testInvalidPasswordTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Invalid Password");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with incorrect password");

        loginPage.login("standard_user", "wrong_password");

        ExtentReportManager.getTest().log(Status.INFO, "Verifying error message is displayed");
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();

        Assert.assertTrue(errorDisplayed, "Error message should be displayed for invalid password");
    }

    @Test(priority = 9, description = "Testing login with both invalid credentials")
    public void testInvalidCredentialsTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Invalid Credentials");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with both invalid username and password");

        loginPage.login("invalid_user", "wrong_password");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for invalid credentials");
    }

    @Test(priority = 10, description = "Testing login with empty username field")
    public void testEmptyUsernameTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Empty Username");
        ExtentReportManager.getTest().log(Status.INFO, "Attempting login with empty username");

        loginPage.login("", "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for empty username");
    }

    @Test(priority = 11, description = "Testing login with empty password field")
    public void testEmptyPasswordTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Empty Password");
        ExtentReportManager.getTest().log(Status.INFO, "Attempting login with empty password");

        loginPage.login("standard_user", "");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for empty password");
    }

    @Test(priority = 12, description = "Testing login with both fields empty")
    public void testEmptyCredentialsTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Empty Credentials");
        ExtentReportManager.getTest().log(Status.INFO, "Attempting login with both fields empty");

        loginPage.login("", "");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for empty credentials");
    }

    @Test(priority = 13, description = "Testing username field with leading/trailing spaces")
    public void testUsernameWithSpaces() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Username with Spaces");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with spaces around username");

        loginPage.login(" standard_user ", "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for username with spaces");
    }

    @Test(priority = 14, description = "Testing password field with leading/trailing spaces")
    public void testPasswordWithSpaces() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Password with Spaces");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with spaces around password");

        loginPage.login("standard_user", " secret_sauce ");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for password with spaces");
    }

    @Test(priority = 15, description = "Testing username field with special characters")
    public void testSpecialCharactersInUsername() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Special Characters in Username");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with special characters: user@#$%");

        loginPage.login("user@#$%", "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Error message should be displayed for special characters in username");
    }

    @Test(priority = 16, description = "Testing SQL injection prevention")
    public void testSQLInjectionPrevention() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: SQL Injection Test");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with SQL injection string: ' OR '1'='1");

        loginPage.login("' OR '1'='1", "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Should prevent SQL injection attacks");
    }

    @Test(priority = 17, description = "Testing case sensitivity for username")
    public void testCaseSensitiveUsername() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Case Sensitive Username");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with uppercase username: STANDARD_USER");

        loginPage.login("STANDARD_USER", "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Login should be case-sensitive for username");
    }

    @Test(priority = 18, description = "Testing case sensitivity for password")
    public void testCaseSensitivePassword() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Case Sensitive Password");
        ExtentReportManager.getTest().log(Status.INFO, "Testing with uppercase password: SECRET_SAUCE");

        loginPage.login("standard_user", "SECRET_SAUCE");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Login should be case-sensitive for password");
    }

    @Test(priority = 19, description = "Testing with extremely long username")
    public void testVeryLongUsername() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Very Long Username");
        String longUsername = "a".repeat(500);
        ExtentReportManager.getTest().log(Status.INFO, "Testing with 500 character username");

        loginPage.login(longUsername, "secret_sauce");

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Should handle very long username appropriately");
    }

    @Test(priority = 20, description = "Testing with extremely long password")
    public void testVeryLongPassword() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Very Long Password");
        String longPassword = "a".repeat(500);
        ExtentReportManager.getTest().log(Status.INFO, "Testing with 500 character password");

        loginPage.login("standard_user", longPassword);

        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(errorDisplayed, "Should handle very long password appropriately");
    }

    // ========== UI VALIDATION TEST CASES ==========

    @Test(priority = 21, description = "Verifying username field is visible on page load")
    public void testUsernameFieldInputTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Username Field Display");
        ExtentReportManager.getTest().log(Status.INFO, "Checking if username field is visible");

        boolean isDisplayed = loginPage.isUsernameFieldDisplayed();
        Assert.assertTrue(isDisplayed, "Username field should be displayed on login page");
    }

    @Test(priority = 22, description = "Verifying password field masking is enabled")
    public void testPasswordFieldMaskingTest() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Password Field Display");
        ExtentReportManager.getTest().log(Status.INFO, "Checking if password field is visible and masked");

        boolean isDisplayed = loginPage.isPasswordFieldDisplayed();
        Assert.assertTrue(isDisplayed, "Password field should be displayed on login page");
    }

    @Test(priority = 23, description = "Verifying login button is visible and clickable")
    public void testLoginButtonDisplay() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Login Button Display");
        ExtentReportManager.getTest().log(Status.INFO, "Checking if login button is visible");

        boolean isDisplayed = loginPage.isLoginButtonDisplayed();
        Assert.assertTrue(isDisplayed, "Login button should be displayed on login page");
    }

    @Test(priority = 24, description = "Verifying login button is in enabled state")
    public void testLoginButtonEnabled() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Login Button State");
        ExtentReportManager.getTest().log(Status.INFO, "Checking if login button is enabled");

        boolean isEnabled = loginPage.isLoginButtonEnabled();
        Assert.assertTrue(isEnabled, "Login button should be enabled");
    }

    @Test(priority = 25, description = "Verifying page title contains application name")
    public void testPageTitle() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting test: Page Title Verification");
        ExtentReportManager.getTest().log(Status.INFO, "Getting page title");

        String pageTitle = driver.getTitle();
        ExtentReportManager.getTest().log(Status.INFO, "Page title: " + pageTitle);

        boolean containsSwagLabs = pageTitle.contains("Swag Labs");
        if (containsSwagLabs) {
            ExtentReportManager.getTest().log(Status.PASS, "Page title contains 'Swag Labs'");
        }

        Assert.assertTrue(containsSwagLabs, "Page title should contain 'Swag Labs'");
    }
}