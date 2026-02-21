package ashu.ashutest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Makemytrip {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        // Disable password manager & leak detection
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-features=PasswordLeakDetection");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.makemytrip.com/");
    }

    @Test
    public void Booking() {

        // Close login popup safely
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@data-cy='closeModal']")
            )).click();
        } 
        catch (Exception e) {
            System.out.println("Popup not displayed");
        }

        // FROM CITY
        wait.until(ExpectedConditions.elementToBeClickable(By.id("fromCity"))).click();

        WebElement fromInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='From']")
                )
        );
        fromInput.sendKeys("Chandigarh");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'Chandigarh')]")
        )).click();

        // TO CITY
        wait.until(ExpectedConditions.elementToBeClickable(By.id("toCity"))).click();

        WebElement toInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='To']")
                )
        );
        toInput.sendKeys("Hyderabad");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'Hyderabad')]")
        )).click();

        // Select departure date (today)
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[@for='departure']")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class,'DayPicker-Day')])[10]")
        )).click();

        // Click Search
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-cy='submit']")
        )).click();
    }


    @AfterClass
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
