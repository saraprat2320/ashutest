package ashu.ashutest;

import com.mailosaur.MailosaurClient;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SalesforceLeadTest {

    WebDriver driver;
    WebDriverWait wait;

    private final String SALESFORCE_URL = "https://login.salesforce.com";
    private final String SALESFORCE_USERNAME = "saraprat2320.0ed9eaf7f3ba@agentforce.com"; 
    private final String SALESFORCE_PASSWORD = "Ashu@1234";

    private final String MAILOSAUR_API_KEY = "9jUnYmSs9askgUvGsrBqRnj1JR9WeZHH";
    private final String MAILOSAUR_SERVER_ID = "qotgdzpp";
    private final String MAILOSAUR_EMAIL = "salesforce@" + MAILOSAUR_SERVER_ID + ".mailosaur.net";

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // Helps avoid detection as a bot
        options.setExperimentalOption("excludeSwitches", java.util.Collections.singletonList("enable-automation"));

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        driver.get(SALESFORCE_URL);
    }

    @Test
    public void loginWithOTP() throws Exception {
        // STEP 1: LOGIN
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(SALESFORCE_USERNAME);
        driver.findElement(By.id("password")).sendKeys(SALESFORCE_PASSWORD);
        driver.findElement(By.id("Login")).click();

        // STEP 2: MAILOSAUR OTP EXTRACTION
        MailosaurClient mailosaur = new MailosaurClient(MAILOSAUR_API_KEY);
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(MAILOSAUR_SERVER_ID);
        params.withTimeout(60000); 

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(MAILOSAUR_EMAIL);

        Message message = mailosaur.messages().get(params, criteria);
        String body = message.text().body();

        Pattern pattern = Pattern.compile("Verification Code:\\s*(\\d{6})");
        Matcher matcher = pattern.matcher(body);
        String otp = matcher.find() ? matcher.group(1) : null;

        if (otp == null) throw new RuntimeException("OTP not found in email!");

        // STEP 3: ENTER OTP
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("emc"))).sendKeys(otp);
        driver.findElement(By.id("save")).click();

        /// ======================================================
     // 🔵 STEP 5 & 6: CLICK LEADS FROM NAVIGATION BAR
     // ======================================================

     // Use a stable XPath for the Navigation Menu Item
     // Salesforce uses 'one-app-nav-bar-item-root' for these tabs
     WebElement leadsTab = wait.until(ExpectedConditions.elementToBeClickable(
             By.xpath("//one-app-nav-bar-item-root[@data-id='Lead']//a | //a[contains(@title,'Leads')]")));

     try {
         // Try standard click first
         leadsTab.click();
     } catch (Exception e) {
         // If intercepted (common in Lightning), use JavaScript Click
         ((org.openqa.selenium.JavascriptExecutor) driver)
                 .executeScript("arguments[0].click();", leadsTab);
     }

     // Wait until the Leads list view actually loads
     wait.until(ExpectedConditions.visibilityOfElementLocated(
             By.xpath("//h1//span[text()='Leads']")));

     System.out.println("✅ Navigated to Leads via Menu Bar");

  // Target the button by its specific 'name' attribute which is stable in Lightning
     WebElement newButton = wait.until(ExpectedConditions.elementToBeClickable(
             By.xpath("//button[@name='New'] | //a[@title='New'] | //div[@title='New']")));

     ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", newButton);

        // STEP 8: FILL DETAILS
        // Salutation Dropdown
        WebElement salutation = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@aria-label, 'Salutation')]")));
        salutation.click();
        
        // Pick 'Mr.'
        WebElement mrOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//lightning-base-combobox-item//span[@title='Mr.']")));
        mrOption.click();

     // First Name
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='First Name']/following::input[1]")))
                .sendKeys("Ashu");

        // Last Name
        driver.findElement(By.xpath("//label[text()='Last Name']/following::input[1]"))
                .sendKeys("AutomationTest");

        // Company
        driver.findElement(By.xpath("//label[text()='Company']/following::input[1]"))
                .sendKeys("TestCompany");

        // Email
        driver.findElement(By.xpath("//label[text()='Email']/following::input[1]"))
                .sendKeys("test@test.com");
        // STEP 9: SAVE
        driver.findElement(By.xpath("//button[@name='SaveEdit']")).click();

        // STEP 10: VERIFY
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//lightning-formatted-name[contains(text(),'AutomationTest')]")));
        System.out.println("🎉 Lead Created Successfully!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}