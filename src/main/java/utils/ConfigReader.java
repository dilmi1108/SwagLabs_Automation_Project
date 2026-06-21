package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try {
            // Using ClassLoader to load resources is more robust for maven executions
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = ConfigReader.class.getClassLoader();
            }
            try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load config.properties file from path: " + e.getMessage());
            // Fallback: load via ClassLoader if direct path fails
            try {
                properties.load(ConfigReader.class.getClassLoader().getResourceAsStream("config.properties"));
            } catch (Exception ex) {
                System.err.println("Error: Failed to load config.properties via ClassLoader: " + ex.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
