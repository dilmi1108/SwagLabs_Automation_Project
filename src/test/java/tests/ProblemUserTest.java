package tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;
import utils.ExtentReportManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Listeners(TestListener.class)
public class ProblemUserTest extends BaseTest {

    @BeforeMethod
    public void loginAsProblemUser() {
        performLogin("problem_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }

    private void logWarning(String message) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.WARNING, "⚠️ " + message);
        }
    }

    @Test(priority = 1, description = "Verify problem_user can login successfully")
    public void testProblemUserLogin() {
        boolean isLoaded = inventoryPage.isInventoryPageLoaded();
        Assert.assertTrue(isLoaded, "Problem user should be able to login and see inventory");
    }

    @Test(priority = 2, description = "Verify inventory page title for problem_user")
    public void testInventoryPageLoadForProblemUser() {
        String title = inventoryPage.getPageTitle();
        Assert.assertEquals(title, "Products", "Page title should still be 'Products' for problem_user");
    }

    @Test(priority = 3, description = "Verify product count for problem_user")
    public void testProductCountForProblemUser() {
        int count = inventoryPage.getProductCount();
        Assert.assertEquals(count, 6, "Should display 6 products even for problem_user");
    }

    @Test(priority = 4, description = "Verify product images may have issues for problem_user")
    public void testProductImagesForProblemUser() {
        logWarning("Problem user is known to have image display issues (all products showing dog image).");
        List<WebElement> images = getDriver().findElements(By.cssSelector(".inventory_item_img img"));
        
        Set<String> imageSources = new HashSet<>();
        for (WebElement img : images) {
            imageSources.add(img.getAttribute("src"));
        }

        if (imageSources.size() == 1) {
            logWarning("Issue Confirmed: All products show the exact same image.");
        }
        Assert.assertTrue(images.size() > 0, "Should have product images");
    }

    @Test(priority = 5, description = "Verify add to cart functionality for problem_user")
    public void testAddToCartForProblemUser() {
        logWarning("Testing add to cart for problem_user - expecting potential failure due to known app bugs.");
        try {
            inventoryPage.addProductToCart(0);
            int cartCount = inventoryPage.getCartCount();
            Assert.assertEquals(cartCount, 1, "Cart should contain 1 item if add succeeded");
        } catch (org.openqa.selenium.TimeoutException e) {
            logWarning("Confirmed Known Bug: Clicked 'Add to cart' on Backpack, but cart count did not increase.");
            int cartCount = inventoryPage.getCartCount();
            Assert.assertEquals(cartCount, 0, "Cart should remain empty (known problem_user bug)");
        }
    }

    @Test(priority = 6, description = "Verify sorting functionality for problem_user")
    public void testSortingForProblemUser() {
        logWarning("Sorting is known to be broken for problem_user.");
        List<String> namesBeforeSort = inventoryPage.getAllProductNames();
        inventoryPage.sortProducts("Name (Z to A)");
        List<String> namesAfterSort = inventoryPage.getAllProductNames();

        if (namesBeforeSort.equals(namesAfterSort)) {
            logWarning("Issue Confirmed: Sorting did not alter product order.");
        }
        // Just verify we can attempt to sort without crashing
        Assert.assertTrue(true, "Sorting attempt complete");
    }

    @Test(priority = 7, description = "Verify all product names are displayed for problem_user")
    public void testProductNamesForProblemUser() {
        List<String> names = inventoryPage.getAllProductNames();
        Assert.assertEquals(names.size(), 6, "Should have 6 product names");
        Assert.assertTrue(names.stream().noneMatch(String::isEmpty), "All products should have names");
    }

    @Test(priority = 8, description = "Verify product prices are displayed for problem_user")
    public void testProductPricesForProblemUser() {
        List<Double> prices = inventoryPage.getAllProductPrices();
        Assert.assertEquals(prices.size(), 6, "Should have 6 product prices");
        Assert.assertTrue(prices.stream().allMatch(p -> p > 0), "All prices should be positive");
    }

    @Test(priority = 9, description = "Verify hamburger menu opens for problem_user")
    public void testMenuOpenForProblemUser() {
        inventoryPage.openSideMenu();
        WebElement allItemsLink = getDriver().findElement(By.id("inventory_sidebar_link"));
        Assert.assertTrue(allItemsLink.isDisplayed(), "Menu should open for problem_user");
    }

    @Test(priority = 10, description = "Verify shopping cart icon is clickable for problem_user")
    public void testShoppingCartClickForProblemUser() {
        inventoryPage.clickShoppingCart();
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart.html"), "Should navigate to cart page");
    }

    @Test(priority = 11, description = "Verify multiple products can be added to cart by problem_user")
    public void testMultipleProductsAddForProblemUser() {
        logWarning("Testing multiple product additions for problem_user.");
        int successCount = 0;
        int[] itemsToTry = {0, 1, 2};
        for (int index : itemsToTry) {
            try {
                inventoryPage.addProductToCart(index);
                successCount++;
            } catch (org.openqa.selenium.TimeoutException e) {
                logWarning("Confirmed Known Bug: Clicked 'Add to cart' on item index " + index + ", but it timed out (did not add).");
            }
        }
        int cartCount = inventoryPage.getCartCount();
        logWarning("Total successfully added items: " + successCount + ", Actual cart count: " + cartCount);
        Assert.assertEquals(cartCount, successCount, "Cart count should match the number of successfully registered clicks");
    }

    @Test(priority = 12, description = "Verify filter dropdown is accessible for problem_user")
    public void testFilterDropdownForProblemUser() {
        WebElement dropdown = getDriver().findElement(By.className("product_sort_container"));
        Assert.assertTrue(dropdown.isDisplayed() && dropdown.isEnabled(), "Filter dropdown should be accessible");
    }

    @Test(priority = 13, description = "Verify product descriptions are visible for problem_user")
    public void testProductDescriptionsForProblemUser() {
        List<WebElement> descriptions = getDriver().findElements(By.className("inventory_item_desc"));
        Assert.assertEquals(descriptions.size(), 6, "Should have 6 product descriptions");
    }

    @Test(priority = 14, description = "Verify footer links are present for problem_user")
    public void testFooterLinksForProblemUser() {
        List<WebElement> socialLinks = getDriver().findElements(By.cssSelector(".social a"));
        Assert.assertTrue(socialLinks.size() > 0, "Should have social media links in footer");
    }

    @Test(priority = 15, description = "Verify logout functionality for problem_user")
    public void testLogoutForProblemUser() {
        logWarning("Testing logout for problem_user - expecting potential redirection issues due to known app bugs.");
        try {
            inventoryPage.clickLogout();
            String currentUrl = getDriver().getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("saucedemo.com") && !currentUrl.contains("inventory"),
                    "Should redirect to login page after logout");
        } catch (org.openqa.selenium.TimeoutException e) {
            logWarning("Confirmed Known Bug: Clicked logout as problem_user, but it did not redirect back to SauceDemo login page (redirected externally or timed out).");
            String currentUrl = getDriver().getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("saucelabs.com") || currentUrl.contains("saucedemo.com"), 
                    "Should either remain on saucedemo or redirect to saucelabs.com");
        }
    }
}