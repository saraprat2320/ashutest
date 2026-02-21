package ashu.ashutest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CuraHospital {

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

        // Disable security warnings
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-features=PasswordLeakDetection");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://katalon-demo-cura.herokuapp.com/");
    }

    @SuppressWarnings("unused")
	@Test
    public void Appointment() {

        // 1️⃣ Click Make Appointment
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("btn-make-appointment")
        )).click();

        // 2️⃣ Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("txt-username")
        )).sendKeys("John Doe");

        driver.findElement(By.id("txt-password"))
              .sendKeys("ThisIsNotAPassword");

        driver.findElement(By.id("btn-login")).click();

        // 3️⃣ Wait for Appointment Page
        wait.until(ExpectedConditions.urlContains("#appointment"));

        // 4️⃣ Select Facility
        Select facility = new Select(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("combo_facility")
                ))
        );
        facility.selectByVisibleText("Hongkong CURA Healthcare Center");

        // 5️⃣ Click Checkbox
        WebElement checkbox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("chk_hospotal_readmission")
                )
        );
        if (!checkbox.isSelected()) {
            checkbox.click();
        }

        // 6️⃣ Select Radio Button
        WebElement radioBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("radio_program_medicaid")
                )
        );
        if (!radioBtn.isSelected()) {
            radioBtn.click();
        }

        // 7️⃣ Enter Visit Date
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement dateField = driver.findElement(By.id("txt_visit_date"));

        js.executeScript("arguments[0].value='02/23/2026';", dateField);

        // 8️⃣ Enter Comment
        driver.findElement(By.id("txt_comment"))
              .sendKeys("Test Appointment Booking");

     // Click Book Appointment
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("btn-book-appointment")
        )).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("menu-toggle")
        )).click();

        // 🔹 Wait for sidebar to become visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("sidebar-wrapper")
        ));

        // 🔹 Wait for History link to be clickable
        WebElement historyLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[normalize-space()='History']")
                )
        );

        // 🔹 Scroll into view (important for animation menu)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", historyLink
        );

        // 🔹 Click using JavaScript to avoid interception
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", historyLink
        );
        WebElement historyHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[text()='History']")
                )
        );
        // Wait for confirmation page
        //wait.until(ExpectedConditions.urlContains("confirmation"));

        // Assertion
        //Assert.assertTrue(driver.getCurrentUrl().contains("confirmation"),
               // "Appointment booking failed!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
