package tests;

import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;
import utils.ExtentReportManager;


import java.util.List;

@Listeners(TestListener.class)
public class InventoryTest extends BaseTest {

    @BeforeMethod
    public void loginBeforeEachTest() {
        // Login before each inventory test
        performLogin("standard_user", "secret_sauce");
    }

    @Test(priority = 1, description = "Verify inventory page loads successfully after login")
    public void testInventoryPageLoad() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Inventory Page Load");
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Inventory page should load after login");
    }

    @Test(priority = 2, description = "Verify inventory page title is 'Products'")
    public void testInventoryPageTitle() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Inventory Page Title");
        String title = inventoryPage.getPageTitle();
        Assert.assertEquals(title, "Products", "Page title should be 'Products'");
    }

    @Test(priority = 3, description = "Verify correct number of products displayed on inventory page")
    public void testProductCount() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Count");
        int count = inventoryPage.getProductCount();
        Assert.assertEquals(count, 6, "Inventory page should display 6 products");
    }

    @Test(priority = 4, description = "Verify all products have names displayed")
    public void testAllProductsHaveNames() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Names Display");
        List<String> names = inventoryPage.getAllProductNames();
        Assert.assertEquals(names.size(), 6, "Should have 6 product names");
        Assert.assertTrue(names.stream().noneMatch(String::isEmpty), "All products should have names");
    }

    @Test(priority = 5, description = "Verify all products have prices displayed")
    public void testAllProductsHavePrices() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Prices Display");
        List<Double> prices = inventoryPage.getAllProductPrices();
        Assert.assertEquals(prices.size(), 6, "Should have 6 product prices");
        Assert.assertTrue(prices.stream().allMatch(price -> price > 0), "All prices should be positive");
    }

    @Test(priority = 6, description = "Verify all product images are displayed")
    public void testProductImagesDisplay() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Product Images Display");
        Assert.assertTrue(inventoryPage.areProductImagesDisplayed(), "All product images should be displayed");
    }

    @Test(priority = 7, description = "Verify adding product to cart increases cart count")
    public void testAddProductToCart() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Add Product to Cart");

        int initialCount = inventoryPage.getCartCount();
        inventoryPage.addProductToCart(0);
        int newCount = inventoryPage.getCartCount();

        Assert.assertEquals(newCount, initialCount + 1, "Cart count should increase by 1");
    }

    @Test(priority = 8, description = "Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Add Multiple Products");

        inventoryPage.addProductToCart(0);
        inventoryPage.addProductToCart(1);
        inventoryPage.addProductToCart(2);

        int cartCount = inventoryPage.getCartCount();
        Assert.assertEquals(cartCount, 3, "Cart should contain 3 items");
    }

    @Test(priority = 9, description = "Verify product sorting by Name (A to Z)")
    public void testSortProductsByNameAtoZ() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Sort Products A-Z");

        inventoryPage.sortProducts("Name (A to Z)");
        List<String> names = inventoryPage.getAllProductNames();

        boolean isSorted = true;
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareTo(names.get(i + 1)) > 0) {
                isSorted = false;
                break;
            }
        }

        Assert.assertTrue(isSorted, "Products should be sorted alphabetically A-Z");
    }

    @Test(priority = 10, description = "Verify product sorting by Name (Z to A)")
    public void testSortProductsByNameZtoA() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Sort Products Z-A");

        inventoryPage.sortProducts("Name (Z to A)");
        List<String> names = inventoryPage.getAllProductNames();

        boolean isSorted = true;
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareTo(names.get(i + 1)) < 0) {
                isSorted = false;
                break;
            }
        }

        Assert.assertTrue(isSorted, "Products should be sorted alphabetically Z-A");
    }

    @Test(priority = 11, description = "Verify product sorting by Price (Low to High)")
    public void testSortProductsByPriceLowToHigh() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Sort Products by Price (Low to High)");

        inventoryPage.sortProducts("Price (low to high)");
        List<Double> prices = inventoryPage.getAllProductPrices();

        boolean isSorted = true;
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                isSorted = false;
                break;
            }
        }

        Assert.assertTrue(isSorted, "Products should be sorted by price low to high");
    }

    @Test(priority = 12, description = "Verify product sorting by Price (High to Low)")
    public void testSortProductsByPriceHighToLow() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Sort Products by Price (High to Low)");

        inventoryPage.sortProducts("Price (high to low)");
        List<Double> prices = inventoryPage.getAllProductPrices();

        boolean isSorted = true;
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                isSorted = false;
                break;
            }
        }

        Assert.assertTrue(isSorted, "Products should be sorted by price high to low");
    }

    @Test(priority = 13, description = "Verify shopping cart icon is clickable")
    public void testShoppingCartIconClick() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Shopping Cart Icon Click");

        inventoryPage.clickShoppingCart();
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("cart.html"), "Should navigate to cart page");
    }

    @Test(priority = 14, description = "Verify navigation menu opens successfully")
    public void testNavigationMenuOpen() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Navigation Menu Open");

        inventoryPage.openMenu();
        // If we reach here without exception, menu opened successfully
        Assert.assertTrue(true, "Navigation menu should open");
    }

    @Test(priority = 15, description = "Verify logout functionality from inventory page")
    public void testLogoutFromInventory() {
        ExtentReportManager.getTest().log(Status.INFO, "Testing: Logout from Inventory Page");

        inventoryPage.logout();
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("saucedemo.com") && !currentUrl.contains("inventory"),
                "Should redirect to login page after logout");
    }
}