package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import tests.listeners.TestListener;

import java.util.List;

@Listeners(TestListener.class)
public class CheckoutTest extends BaseTest {

    @BeforeMethod
    public void prepareCartAndNavigateToCheckout() {
        performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }

    @Test(priority = 1, description = "Verify required field validation on checkout information page")
    public void testCheckoutFormValidations() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        Assert.assertTrue(checkoutInformationPage.isCheckoutInformationPageLoaded(), "Should be on step 1 checkout page");

        // Case 1: Submit completely empty form
        checkoutInformationPage.clickContinue();
        Assert.assertTrue(checkoutInformationPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(checkoutInformationPage.getErrorMessage().contains("First Name is required"),
                "Error should mention First Name");

        // Case 2: Fill First Name, leave others empty
        checkoutInformationPage.enterFirstName("John");
        checkoutInformationPage.clickContinue();
        Assert.assertTrue(checkoutInformationPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(checkoutInformationPage.getErrorMessage().contains("Last Name is required"),
                "Error should mention Last Name");

        // Case 3: Fill First and Last Name, leave Postal Code empty
        checkoutInformationPage.enterLastName("Doe");
        checkoutInformationPage.clickContinue();
        Assert.assertTrue(checkoutInformationPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(checkoutInformationPage.getErrorMessage().contains("Postal Code is required"),
                "Error should mention Postal Code");
    }

    @Test(priority = 2, description = "Verify order overview page item details and price calculations")
    public void testCheckoutOverviewCalculations() {
        // Add multiple items
        inventoryPage.addProductToCartByName("Sauce Labs Backpack"); // 29.99
        inventoryPage.addProductToCartByName("Sauce Labs Bike Light"); // 9.99
        inventoryPage.addProductToCartByName("Sauce Labs Bolt T-Shirt"); // 15.99

        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        // Verify page loads before typing
        Assert.assertTrue(checkoutInformationPage.isCheckoutInformationPageLoaded(), "Checkout info page should load");

        // Fill valid info
        checkoutInformationPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutInformationPage.clickContinue();

        Assert.assertTrue(checkoutOverviewPage.isCheckoutOverviewPageLoaded(), "Should be on checkout overview step 2 page");

        // Verify items summary
        List<String> names = checkoutOverviewPage.getOverviewItemNames();
        Assert.assertEquals(names.size(), 3, "Overview should show 3 items");
        Assert.assertTrue(names.contains("Sauce Labs Backpack"));
        Assert.assertTrue(names.contains("Sauce Labs Bike Light"));
        Assert.assertTrue(names.contains("Sauce Labs Bolt T-Shirt"));

        // Validate Price Calculations
        double subtotal = checkoutOverviewPage.getSubtotal();
        double expectedSubtotal = checkoutOverviewPage.calculateExpectedSubtotal();
        Assert.assertEquals(subtotal, expectedSubtotal, "Displayed subtotal should match sum of item prices: 55.97");
        Assert.assertEquals(subtotal, 55.97, "Calculated sum should be 55.97");

        double tax = checkoutOverviewPage.getTax();
        double expectedTax = checkoutOverviewPage.calculateExpectedTax(subtotal);
        Assert.assertEquals(tax, expectedTax, "Displayed tax should match expected 8% tax rate: 4.48");

        double total = checkoutOverviewPage.getTotal();
        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, "Total price should be subtotal + tax");
        Assert.assertEquals(total, 60.45, "Total price should equal 60.45");

        // Verify Payment & Shipping Info are present
        String payment = checkoutOverviewPage.getPaymentInfo();
        String shipping = checkoutOverviewPage.getShippingInfo();
        Assert.assertFalse(payment.isEmpty(), "Payment information should be displayed");
        Assert.assertFalse(shipping.isEmpty(), "Shipping information should be displayed");
    }

    @Test(priority = 3, description = "Verify canceling checkout from step 2 redirects back to Products list")
    public void testCancelCheckoutOverview() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();
        
        Assert.assertTrue(checkoutInformationPage.isCheckoutInformationPageLoaded(), "Checkout info page should load");
        checkoutInformationPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutInformationPage.clickContinue();

        Assert.assertTrue(checkoutOverviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should load");

        // Cancel
        checkoutOverviewPage.clickCancel();
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Should redirect to inventory page after canceling overview");
    }

    @Test(priority = 4, description = "Verify successful completion of checkout flow and return to home page")
    public void testSuccessfulCheckoutFlow() {
        inventoryPage.addProductToCartByName("Sauce Labs Backpack");
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        Assert.assertTrue(checkoutInformationPage.isCheckoutInformationPageLoaded(), "Checkout info page should load");
        checkoutInformationPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutInformationPage.clickContinue();

        Assert.assertTrue(checkoutOverviewPage.isCheckoutOverviewPageLoaded(), "Checkout overview page should load");

        // Finish checkout
        checkoutOverviewPage.clickFinish();

        Assert.assertTrue(checkoutCompletePage.isCheckoutCompletePageLoaded(), "Should redirect to checkout complete page");

        // Verify Success Messages
        String header = checkoutCompletePage.getConfirmationHeader();
        String desc = checkoutCompletePage.getConfirmationDescription();
        Assert.assertTrue(header.equalsIgnoreCase("Thank you for your order!"), "Success header mismatch: " + header);
        Assert.assertTrue(desc.toLowerCase().contains("dispatched"), "Success description should mention dispatched. Found: " + desc);

        // Click Back Home and verify redirection and empty cart
        checkoutCompletePage.clickBackHome();
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), "Should return to product inventory page");
        Assert.assertEquals(inventoryPage.getCartCount(), 0, "Cart count should be reset to 0 after order completion");
    }
}
