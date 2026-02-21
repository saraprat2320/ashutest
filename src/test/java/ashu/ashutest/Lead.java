package ashu.ashutest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Scanner;
import java.time.Duration;

public class Lead {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        // Set ChromeDriver path
       // System.setProperty("webdriver.chrome.driver", "C:\\Users\\Prateek Sharma\\Downloads\\chromedriver-win64\\chromedriver.exe");

        // Initialize ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Initialize wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Navigate to CRM login page
        driver.get("https://login.salesforce.com");
    }

    @Test
    public void createLead() {
        // --- LOGIN ---
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("saraprat2320.0ed9eaf7f3ba@agentforce.com");
        driver.findElement(By.id("password")).sendKeys("Ashu@1234");
        driver.findElement(By.id("Login")).click(); // Salesforce login button id is usually "Login"
        
     // ---------------- MFA MANUAL STEP ----------------
        System.out.println("Enter SMS verification code manually in browser.");
        System.out.println("After successful login and Home page loads, press ENTER here...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        System.out.println("Continuing automation...");

        // ---------------- WAIT FOR HOME PAGE ----------------
        // Wait until App Launcher icon is visible (more stable than linkText)
       wait.until(ExpectedConditions.visibilityOfElementLocated(
               By.xpath("//div[@role='navigation']")));



     // ---------------- NAVIGATE TO LEADS ----------------
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@title='Leads']"))).click();

        // Click New Lead button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@title='New']"))).click();
        // --- FILL LEAD FORM ---
        driver.findElement(By.id("name_firstlea2")).sendKeys("John"); // first name field id may vary
        driver.findElement(By.id("name_lastlea2")).sendKeys("Doe");   // last name field id may vary
        driver.findElement(By.id("lea3")).sendKeys("Acme Corp");      // company field id may vary
        driver.findElement(By.id("lea5")).sendKeys("john.doe@example.com"); // email field id may vary

        // --- SAVE LEAD ---
        driver.findElement(By.name("save")).click();

        // --- VERIFY LEAD CREATION ---
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("topName"))); // Usually Salesforce shows lead name
        if (successMessage.isDisplayed()) {
            System.out.println("Lead created successfully!");
        } else {
            System.out.println("Failed to create lead!");
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
