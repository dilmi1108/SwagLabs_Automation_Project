package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import pages.InventoryPage;
import pages.LoginPage;
import com.aventstack.extentreports.Status;
import utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        String reportPath = "test-output/ExtentReport.html";
        ExtentReportManager.createInstance(reportPath);
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        // Initialize ExtentTest for setup
        test = ExtentReportManager.getTest();

        driver = initializeDriver(browser);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Initialize page objects
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);

        // Navigate to application
        driver.get("https://www.saucedemo.com/");
        if (test != null) {
            test.log(Status.INFO, "Navigating to Swag Labs");
        }
    }

    protected void performLogin(String username, String password) {
        if (test != null) {
            test.log(Status.INFO, "Performing login with username: " + username);
        }
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            if (test != null) {
                test.log(Status.INFO, "Browser closed successfully");
            }
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentReportManager.getExtent().flush();
    }

    private WebDriver initializeDriver(String browser) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
