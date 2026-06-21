package tests.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {
    private ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        // Consolidated single HTML report for the entire execution
        String reportPath = "test-output/ExtentReport.html";
        extent = ExtentReportManager.createInstance(reportPath);
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(testName, description);
        ExtentReportManager.setTest(test);
        test.log(Status.INFO, "Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed: " + result.getName());
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, "Test failed: " + result.getName());
            if (result.getThrowable() != null) {
                test.log(Status.FAIL, result.getThrowable());
            }

            // Capture and attach screenshot on failure
            try {
                WebDriver driver = BaseTest.getDriver();
                if (driver != null) {
                    String relativePath = ScreenshotUtils.takeScreenshot(driver, result.getName());
                    if (relativePath != null) {
                        // Extent Report will look up relative path relative to its own folder (test-output/)
                        test.addScreenCaptureFromPath(relativePath, "Failure Screenshot");
                        test.log(Status.INFO, "Failure screenshot attached successfully.");
                    }
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to capture or attach screenshot: " + e.getMessage());
            }
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getName());
            if (result.getThrowable() != null) {
                test.log(Status.SKIP, result.getThrowable());
            }
        }
        ExtentReportManager.removeTest();
    }
}