package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import utils.ConfigReader;

import java.time.Duration;

public class BaseTest {
    // ThreadLocal driver for thread-safe parallel executions
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    // Protected page object references for tests to access directly
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage cartPage;
    protected ProductDetailsPage productDetailsPage;
    protected CheckoutInformationPage checkoutInformationPage;
    protected CheckoutOverviewPage checkoutOverviewPage;
    protected CheckoutCompletePage checkoutCompletePage;

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static void setDriver(WebDriver driver) {
        threadLocalDriver.set(driver);
    }

    public static void removeDriver() {
        threadLocalDriver.remove();
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        // Determine browser (CLI argument > TestNG Parameter > Config properties > Default to Chrome)
        String browserName = System.getProperty("browser");
        if (browserName == null || browserName.isEmpty()) {
            browserName = browser;
        }
        if (browserName == null || browserName.equalsIgnoreCase("chrome") || browserName.isEmpty()) {
            String configBrowser = ConfigReader.getProperty("browser");
            if (configBrowser != null && !configBrowser.isEmpty()) {
                browserName = configBrowser;
            } else {
                browserName = "chrome";
            }
        }

        // Determine headless mode (CLI override > Config file > Default to false)
        String headlessSys = System.getProperty("headless");
        boolean isHeadless = false;
        if (headlessSys != null && !headlessSys.isEmpty()) {
            isHeadless = Boolean.parseBoolean(headlessSys);
        } else {
            String configHeadless = ConfigReader.getProperty("headless");
            if (configHeadless != null && !configHeadless.isEmpty()) {
                isHeadless = Boolean.parseBoolean(configHeadless);
            }
        }

        // Initialize driver
        WebDriver driver = initializeDriver(browserName, isHeadless);
        setDriver(driver);

        // Fetch timeouts from config or default values
        int implicitWait = Integer.parseInt(ConfigReader.getProperty("implicit.wait", "10"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Initialize Page Objects
        loginPage = new LoginPage(getDriver());
        inventoryPage = new InventoryPage(getDriver());
        cartPage = new CartPage(getDriver());
        productDetailsPage = new ProductDetailsPage(getDriver());
        checkoutInformationPage = new CheckoutInformationPage(getDriver());
        checkoutOverviewPage = new CheckoutOverviewPage(getDriver());
        checkoutCompletePage = new CheckoutCompletePage(getDriver());

        // Navigate to the Application URL
        String url = ConfigReader.getProperty("url", "https://www.saucedemo.com/");
        getDriver().get(url);
    }

    protected void performLogin(String username, String password) {
        loginPage.login(username, password);
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
        }
        removeDriver();
    }

    private WebDriver initializeDriver(String browser, boolean headless) {
        WebDriver driver;
        String normalizedBrowser = browser.toLowerCase().trim();

        switch (normalizedBrowser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        if (!headless) {
            driver.manage().window().maximize();
        } else {
            try {
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
            } catch (Exception ignored) {}
        }
        return driver;
    }
}
