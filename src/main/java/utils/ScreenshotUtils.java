package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    /**
     * Takes a screenshot of the current browser state.
     * Saves it under the test-output/Screenshots folder.
     * Returns the relative path to the screenshot for embedding in reports.
     */
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            System.err.println("Driver is null. Cannot capture screenshot.");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Save screenshot inside test-output directory to keep it with the HTML report
        String folderPath = System.getProperty("user.dir") + "/test-output/Screenshots/";
        String fileName = screenshotName + "_" + timestamp + ".png";
        File destinationFile = new File(folderPath + fileName);

        try {
            FileUtils.copyFile(source, destinationFile);
            // Return relative path relative to the test-output folder where ExtentReport is stored
            return "Screenshots/" + fileName;
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
}
