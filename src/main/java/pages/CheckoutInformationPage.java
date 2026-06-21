package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPage extends BasePage {

    // Locators
    private final By pageTitle = By.className("title");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutInformationPageLoaded() {
        return safeIsDisplayed(pageTitle, "Checkout Information Page Title") 
                && getCurrentUrl().contains("checkout-step-one.html");
    }

    public void enterFirstName(String firstName) {
        safeSendKeys(firstNameField, firstName, "First Name Field", false);
    }

    public void enterLastName(String lastName) {
        safeSendKeys(lastNameField, lastName, "Last Name Field", false);
    }

    public void enterPostalCode(String postalCode) {
        safeSendKeys(postalCodeField, postalCode, "Postal Code Field", false);
    }

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public void clickContinue() {
        safeClick(continueButton, "Continue Button");
    }

    public void clickCancel() {
        safeClick(cancelButton, "Cancel Button");
    }

    public boolean isErrorMessageDisplayed() {
        return safeIsDisplayed(errorMessage, "Error Message Container");
    }

    public String getErrorMessage() {
        return safeGetText(errorMessage, "Error Message");
    }
}
