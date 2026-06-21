package pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    // Locators
    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By cancelButton = By.id("cancel");
    private final By finishButton = By.id("finish");

    // Dynamic locators for Payment & Shipping Info based on following-sibling
    private final By paymentInfoLabel = By.xpath("//div[@data-test='payment-info-value' or contains(text(), 'Payment Information')]/following-sibling::div[1]");
    private final By shippingInfoLabel = By.xpath("//div[@data-test='shipping-info-value' or contains(text(), 'Shipping Information')]/following-sibling::div[1]");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutOverviewPageLoaded() {
        return safeIsDisplayed(pageTitle, "Checkout Overview Page Title") 
                && getCurrentUrl().contains("checkout-step-two.html");
    }

    public List<String> getOverviewItemNames() {
        try {
            List<WebElement> elements = driver.findElements(itemNames);
            List<String> names = new ArrayList<>();
            for (WebElement element : elements) {
                names.add(element.getText());
            }
            return names;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get overview item names. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Double> getOverviewItemPrices() {
        try {
            List<WebElement> elements = driver.findElements(itemPrices);
            List<Double> prices = new ArrayList<>();
            for (WebElement element : elements) {
                prices.add(Double.parseDouble(element.getText().replace("$", "")));
            }
            return prices;
        } catch (Exception e) {
            logToReport(Status.FAIL, "Failed to get overview item prices. Exception: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String getPaymentInfo() {
        try {
            // Find element by data-test first, fall back to xpath
            WebElement el;
            if (!driver.findElements(By.cssSelector("[data-test='payment-info-value']")).isEmpty()) {
                el = driver.findElement(By.cssSelector("[data-test='payment-info-value']"));
            } else {
                el = driver.findElement(paymentInfoLabel);
            }
            return el.getText();
        } catch (Exception e) {
            logToReport(Status.WARNING, "Could not fetch Payment Info: " + e.getMessage());
            return "";
        }
    }

    public String getShippingInfo() {
        try {
            // Find element by data-test first, fall back to xpath
            WebElement el;
            if (!driver.findElements(By.cssSelector("[data-test='shipping-info-value']")).isEmpty()) {
                el = driver.findElement(By.cssSelector("[data-test='shipping-info-value']"));
            } else {
                el = driver.findElement(shippingInfoLabel);
            }
            return el.getText();
        } catch (Exception e) {
            logToReport(Status.WARNING, "Could not fetch Shipping Info: " + e.getMessage());
            return "";
        }
    }

    public double getSubtotal() {
        String text = safeGetText(subtotalLabel, "Item Subtotal Label");
        // Format is: "Item total: $29.99"
        return Double.parseDouble(text.replace("Item total: $", "").trim());
    }

    public double getTax() {
        String text = safeGetText(taxLabel, "Tax Label");
        // Format is: "Tax: $2.40"
        return Double.parseDouble(text.replace("Tax: $", "").trim());
    }

    public double getTotal() {
        String text = safeGetText(totalLabel, "Total Price Label");
        // Format is: "Total: $32.39"
        return Double.parseDouble(text.replace("Total: $", "").trim());
    }

    public void clickCancel() {
        safeClick(cancelButton, "Cancel Button");
    }

    public void clickFinish() {
        safeClick(finishButton, "Finish Button");
    }

    /**
     * Calculates the sum of all item prices currently displayed.
     */
    public double calculateExpectedSubtotal() {
        List<Double> prices = getOverviewItemPrices();
        double sum = 0.0;
        for (double price : prices) {
            sum += price;
        }
        // Round to 2 decimal places
        return Math.round(sum * 100.0) / 100.0;
    }

    /**
     * Calculates the expected tax (approx. 8% on SauceDemo).
     */
    public double calculateExpectedTax(double subtotal) {
        double tax = subtotal * 0.08;
        return Math.round(tax * 100.0) / 100.0;
    }
}
