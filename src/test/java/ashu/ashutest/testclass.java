package ashu.ashutest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class testclass {
	public WebDriver driver;

    @BeforeMethod
    public void launchDriver() {

        driver = new ChromeDriver(); // Selenium Manager handles driver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void test1() {
        driver.navigate().to("https://automationtalks.com/");
        System.out.println("Test 1 title is " + driver.getTitle());
    }

    @Test
    public void test2() {
        driver.navigate().to("https://automationtalks.com/");
        System.out.println("Test 2 title is " + driver.getTitle());
    }

    @Test
    public void test3() {
        driver.navigate().to("https://automationtalks.com/");
        System.out.println("Test 3 title is " + driver.getTitle());
    }

    @AfterMethod
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

}
