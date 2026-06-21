package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By pageTitle = By.className("title");
    private final By headerMessage = By.className("complete-header");
    private final By descriptionMessage = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutCompletePageLoaded() {
        return safeIsDisplayed(pageTitle, "Checkout Complete Page Title") 
                && getCurrentUrl().contains("checkout-complete.html");
    }

    public String getConfirmationHeader() {
        return safeGetText(headerMessage, "Confirmation Header Message");
    }

    public String getConfirmationDescription() {
        return safeGetText(descriptionMessage, "Confirmation Description Message");
    }

    public void clickBackHome() {
        safeClick(backHomeButton, "Back Home Button");
    }
}
