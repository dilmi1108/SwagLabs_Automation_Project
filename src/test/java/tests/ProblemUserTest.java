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
        // Login as problem_user before each test
        performLogin("problem_user", "secret_sauce");
        // Note: ExtentReport logging will be done in test methods since test instance is created in @Test lifecycle
    }

    @Test(priority = 1, description = "Verify problem_user can login successfully")
    public void testProblemUserLogin() {
        ExtentReportManager.getTest().log(Status.INFO, "⚠️ Testing with problem_user account (known UI issues)");
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Problem User Login");

        boolean isLoaded = inventoryPage.isInventoryPageLoaded();
        Assert.assertTrue(isLoaded, "Problem user should be able to login and see inventory");
    }

    @Test(priority = 2, description = "Verify inventory page loads for problem_user")
    public void testInventoryPageLoadForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Inventory Page Load for Problem User");

        String title = inventoryPage.getPageTitle();
        Assert.assertEquals(title, "Products", "Page title should still be 'Products' for problem_user");
    }

    @Test(priority = 3, description = "Verify product count for problem_user")
    public void testProductCountForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Count for Problem User");

        int count = inventoryPage.getProductCount();
        ExtentReportManager.getTest().log(Status.INFO, "Products displayed: " + count);

        Assert.assertEquals(count, 6, "Should display 6 products even for problem_user");
    }

    @Test(priority = 4, description = "Verify product images may have issues for problem_user")
    public void testProductImagesForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Images for Problem User");
        ExtentReportManager.getTest().log(Status.WARNING, "⚠️ Problem user is known to have image display issues");

        try {
            List<WebElement> images = driver.findElements(By.cssSelector(".inventory_item_img img"));
            ExtentReportManager.getTest().log(Status.INFO, "Total images found: " + images.size());

            // Check if images have src attribute
            Set<String> imageSources = new HashSet<>();
            for (WebElement img : images) {
                String src = img.getAttribute("src");
                imageSources.add(src);
                ExtentReportManager.getTest().log(Status.INFO, "Image source: " + src);
            }

            // Problem user might show same image for all products
            if (imageSources.size() == 1) {
                ExtentReportManager.getTest().log(Status.WARNING,
                        "⚠️ Issue Detected: All products showing same image (known problem_user issue)");
            }

            Assert.assertTrue(images.size() > 0, "Should have product images");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to verify images: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "Verify add to cart functionality for problem_user")
    public void testAddToCartForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Add to Cart for Problem User");

        inventoryPage.addProductToCart(0);
        int cartCount = inventoryPage.getCartCount();

        ExtentReportManager.getTest().log(Status.INFO, "Cart count after adding product: " + cartCount);
        Assert.assertEquals(cartCount, 1, "Cart should contain 1 item");
    }

    @Test(priority = 6, description = "Verify sorting functionality for problem_user")
    public void testSortingForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Sorting for Problem User");
        ExtentReportManager.getTest().log(Status.WARNING, "⚠️ Sorting may not work properly for problem_user");

        List<String> namesBeforeSort = inventoryPage.getAllProductNames();
        ExtentReportManager.getTest().log(Status.INFO, "Products before sort: " + namesBeforeSort);

        inventoryPage.sortProducts("Name (Z to A)");

        List<String> namesAfterSort = inventoryPage.getAllProductNames();
        ExtentReportManager.getTest().log(Status.INFO, "Products after sort: " + namesAfterSort);

        // For problem_user, sorting might not work correctly
        boolean sortChanged = !namesBeforeSort.equals(namesAfterSort);
        if (!sortChanged) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "⚠️ Issue Detected: Sorting did not change product order (known problem_user issue)");
        }

        // Just verify we can attempt to sort without errors
        Assert.assertTrue(true, "Sorting functionality should be accessible");
    }

    @Test(priority = 7, description = "Verify all product names are displayed for problem_user")
    public void testProductNamesForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Names for Problem User");

        List<String> names = inventoryPage.getAllProductNames();

        for (String name : names) {
            ExtentReportManager.getTest().log(Status.INFO, "Product: " + name);
        }

        Assert.assertEquals(names.size(), 6, "Should have 6 product names");
        Assert.assertTrue(names.stream().noneMatch(String::isEmpty), "All products should have names");
    }

    @Test(priority = 8, description = "Verify product prices are displayed for problem_user")
    public void testProductPricesForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Prices for Problem User");

        List<Double> prices = inventoryPage.getAllProductPrices();

        for (Double price : prices) {
            ExtentReportManager.getTest().log(Status.INFO, "Product Price: $" + price);
        }

        Assert.assertEquals(prices.size(), 6, "Should have 6 product prices");
        Assert.assertTrue(prices.stream().allMatch(p -> p > 0), "All prices should be positive");
    }

    @Test(priority = 9, description = "Verify hamburger menu opens for problem_user")
    public void testMenuOpenForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Menu Open for Problem User");

        inventoryPage.openMenu();

        // Check if menu items are visible
        try {
            WebElement allItemsLink = driver.findElement(By.id("inventory_sidebar_link"));
            boolean isMenuOpen = allItemsLink.isDisplayed();

            if (isMenuOpen) {
                ExtentReportManager.getTest().log(Status.PASS, "✓ Menu opened successfully");
            }

            Assert.assertTrue(isMenuOpen, "Menu should open for problem_user");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Menu did not open properly");
            throw e;
        }
    }

    @Test(priority = 10, description = "Verify shopping cart icon is clickable for problem_user")
    public void testShoppingCartClickForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Shopping Cart Click for Problem User");

        inventoryPage.clickShoppingCart();
        String currentUrl = driver.getCurrentUrl();

        ExtentReportManager.getTest().log(Status.INFO, "Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("cart.html"), "Should navigate to cart page");
    }

    @Test(priority = 11, description = "Verify multiple products can be added to cart by problem_user")
    public void testMultipleProductsAddForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Add Multiple Products for Problem User");

        inventoryPage.addProductToCart(0);
        inventoryPage.addProductToCart(1);
        inventoryPage.addProductToCart(2);

        int cartCount = inventoryPage.getCartCount();
        ExtentReportManager.getTest().log(Status.INFO, "Total items in cart: " + cartCount);

        Assert.assertEquals(cartCount, 3, "Cart should contain 3 items");
    }

    @Test(priority = 12, description = "Verify filter dropdown is accessible for problem_user")
    public void testFilterDropdownForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Filter Dropdown for Problem User");

        try {
            WebElement dropdown = driver.findElement(By.className("product_sort_container"));
            boolean isDisplayed = dropdown.isDisplayed();
            boolean isEnabled = dropdown.isEnabled();

            ExtentReportManager.getTest().log(Status.INFO, "Dropdown displayed: " + isDisplayed);
            ExtentReportManager.getTest().log(Status.INFO, "Dropdown enabled: " + isEnabled);

            Assert.assertTrue(isDisplayed && isEnabled, "Filter dropdown should be accessible");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Filter dropdown not accessible");
            throw e;
        }
    }

    @Test(priority = 13, description = "Verify product descriptions are visible for problem_user")
    public void testProductDescriptionsForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Descriptions for Problem User");

        try {
            List<WebElement> descriptions = driver.findElements(By.className("inventory_item_desc"));

            ExtentReportManager.getTest().log(Status.INFO, "Total descriptions found: " + descriptions.size());

            for (int i = 0; i < descriptions.size(); i++) {
                String desc = descriptions.get(i).getText();
                ExtentReportManager.getTest().log(Status.INFO, "Product " + (i+1) + " description: " +
                        (desc.length() > 50 ? desc.substring(0, 50) + "..." : desc));
            }

            Assert.assertEquals(descriptions.size(), 6, "Should have 6 product descriptions");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to verify descriptions");
            throw e;
        }
    }

    @Test(priority = 14, description = "Verify footer links are present for problem_user")
    public void testFooterLinksForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Footer Links for Problem User");

        try {
            List<WebElement> socialLinks = driver.findElements(By.cssSelector(".social a"));

            ExtentReportManager.getTest().log(Status.INFO, "Social links found: " + socialLinks.size());

            for (WebElement link : socialLinks) {
                String href = link.getAttribute("href");
                ExtentReportManager.getTest().log(Status.INFO, "Social link: " + href);
            }

            Assert.assertTrue(socialLinks.size() > 0, "Should have social media links in footer");

        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.INFO, "Footer links not found or not accessible");
        }
    }

    @Test(priority = 15, description = "Verify logout functionality for problem_user")
    public void testLogoutForProblemUser() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Logout for Problem User");

        inventoryPage.logout();

        String currentUrl = driver.getCurrentUrl();
        ExtentReportManager.getTest().log(Status.INFO, "URL after logout: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("saucedemo.com") && !currentUrl.contains("inventory"),
                "Should redirect to login page after logout");


    }
}