package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;

import java.time.Duration;

@Listeners(TestListener.class)
public class NavigationTest extends BaseTest {

    @BeforeMethod
    public void loginAndInitialize() {
        performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }

    @Test(priority = 1, description = "Verify opening and closing the sidebar menu")
    public void testOpenCloseSideMenu() {
        // Open
        inventoryPage.openSideMenu();
        Assert.assertTrue(getDriver().findElement(By.id("inventory_sidebar_link")).isDisplayed(), 
                "Sidebar menu links should be visible when menu is opened");

        // Close
        inventoryPage.closeSideMenu();
        // Use an explicit wait to assert invisibility
        WebDriverWait shortWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        boolean isInvisible = shortWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("inventory_sidebar_link")));
        Assert.assertTrue(isInvisible, "Sidebar menu links should be invisible after menu is closed");
    }

    @Test(priority = 2, description = "Verify resetting the app state clears the shopping cart and resets button states")
    public void testResetAppState() {
        // Add items to cart
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.addProductToCartByName("Sauce Labs Bike Light");
        Assert.assertEquals(inventoryPage.getCartCount(), 2, "Cart should contain 2 items");

        // Reset state
        inventoryPage.clickResetAppState();

        // Verify cart is cleared
        Assert.assertEquals(inventoryPage.getCartCount(), 0, "Cart count should be reset to 0");

        // Verify add-to-cart buttons revert back to original state
        Assert.assertFalse(inventoryPage.isProductInRemoveState("Sauce Labs Backpack"), 
                "Backpack button should show 'Add to cart'");
        Assert.assertFalse(inventoryPage.isProductInRemoveState("Sauce Labs Bike Light"), 
                "Bike Light button should show 'Add to cart'");
    }

    @Test(priority = 3, description = "Verify logging out redirects to Login Page and terminates session")
    public void testLogoutAndSessionTermination() {
        inventoryPage.clickLogout();

        // Verify redirect to login page
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field should be visible on login page");
        Assert.assertTrue(getDriver().getCurrentUrl().matches("https://www.saucedemo.com/?$") 
                || getDriver().getCurrentUrl().contains("index.html"), 
                "URL should be the login screen URL");

        // Attempt direct access to inventory page
        getDriver().get("https://www.saucedemo.com/inventory.html");

        // Verify user is redirected back to login page (Unauthorized access check)
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Unauthorized access error message should be displayed when trying to bypass login page");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("you can only access"), 
                "Error message should mention authorization restriction");
    }
}
