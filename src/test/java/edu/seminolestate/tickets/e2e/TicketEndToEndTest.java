package edu.seminolestate.tickets.e2e;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import static org.junit.jupiter.api.Assertions.assertTrue;
class TicketEndToEndTest {
    private WebDriver driver;
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    void userCanCreateSupportTicket() {
        driver.get("http://localhost:8080/tickets/new");
        driver.findElement(By.name("requesterName"))
                .sendKeys("E2E Test User");
        driver.findElement(By.name("email"))
                .sendKeys("e2e@example.com");
        new Select(driver.findElement(By.name("category")))
                .selectByVisibleText("Software");
        driver.findElement(By.name("description"))
                .sendKeys("Automated end-to-end test ticket.");
        driver.findElement(By.tagName("button"))
                .click();
        assertTrue(driver.getCurrentUrl().contains("/tickets/"));
        assertTrue(driver.getPageSource().contains("E2E Test User"));
        assertTrue(driver.getPageSource()
                .contains("Automated end-to-end test ticket."));
    }

    @Test
    void invalidTicketSubmissionIsRejected() {
        driver.get("http://localhost:8080/tickets/new");
        driver.findElement(By.name("requesterName"))
                .sendKeys("E2E Invalid User");
        driver.findElement(By.name("email"))
                .sendKeys("invalid-email");
        new Select(driver.findElement(By.name("category")))
                .selectByVisibleText("Software");
        driver.findElement(By.name("description"))
                .sendKeys("Testing invalid email validation.");
        driver.findElement(By.tagName("button"))
                .click();
        assertTrue(driver.getPageSource()
                .contains("An unexpected error occurred. Please try again."));
    }
}