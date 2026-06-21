package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;

import java.util.List;

@Listeners(TestListener.class)
public class CartTest extends BaseTest {

    @BeforeMethod
    public void loginAndPrepareCart() {
        performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }

    @Test(priority = 1, description = "Verify cart page loads and displays correct page headers")
    public void testCartPageLoad() {
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should load successfully");
    }

    @Test(priority = 2, description = "Verify items added to cart from products list display correctly in the cart")
    public void testItemsInCart() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.addProductToCartByName("Sauce Labs Bike Light");

        inventoryPage.clickShoppingCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Should navigate to cart page");

        Assert.assertEquals(cartPage.getCartItemsCount(), 2, "Cart should contain 2 items");

        List<String> names = cartPage.getCartItemNames();
        Assert.assertTrue(names.contains("Sauce Labs Backpack"), "Cart should contain Sauce Labs Backpack");
        Assert.assertTrue(names.contains("Sauce Labs Bike Light"), "Cart should contain Sauce Labs Bike Light");

        List<Double> prices = cartPage.getCartItemPrices();
        Assert.assertTrue(prices.contains(29.99), "Cart should contain backpack price: 29.99");
        Assert.assertTrue(prices.contains(9.99), "Cart should contain bike light price: 9.99");
    }

    @Test(priority = 3, description = "Verify removing items from the cart page decreases count and updates list")
    public void testRemoveItemFromCart() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.addProductToCartByName("Sauce Labs Bike Light");

        inventoryPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemsCount(), 2, "Cart should contain 2 items initially");

        // Remove backpack
        cartPage.removeItemByName("Sauce Labs Backpack");

        Assert.assertEquals(cartPage.getCartItemsCount(), 1, "Cart count should decrease to 1");
        List<String> names = cartPage.getCartItemNames();
        Assert.assertFalse(names.contains("Sauce Labs Backpack"), "Sauce Labs Backpack should be removed");
        Assert.assertTrue(names.contains("Sauce Labs Bike Light"), "Sauce Labs Bike Light should remain");
    }

    @Test(priority = 4, description = "Verify Continue Shopping button redirects back to Inventory page")
    public void testContinueShoppingNavigation() {
        inventoryPage.clickShoppingCart();
        cartPage.clickContinueShopping();

        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Should redirect back to inventory page");
    }

    @Test(priority = 5, description = "Verify Checkout button redirects to step 1 of Checkout page")
    public void testCheckoutButtonNavigation() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        Assert.assertTrue(checkoutInformationPage.isCheckoutInformationPageLoaded(), "Should redirect to Checkout Information page");
    }
}
