package com.log430.brockerx.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import java.time.Duration;

public class SignupE2ETest {

    @Test public void signupFlowTest() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        URL seleniumUrl = new URL("http://localhost:4444/wd/hub");
        WebDriver driver = new RemoteWebDriver(seleniumUrl, options);

        try {
            driver.get("http://brockerx-test:8081/signup");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));

            driver.findElement(By.name("email")).sendKeys("test2@example.com");
            driver.findElement(By.name("password")).sendKeys("Password123");
            driver.findElement(By.name("phone")).sendKeys("1234567890");
            driver.findElement(By.name("firstName")).sendKeys("William");
            driver.findElement(By.name("lastName")).sendKeys("Desgagné");
            driver.findElement(By.name("address")).sendKeys("123 Rue Exemple");

            WebElement dobInput = driver.findElement(By.name("dateOfBirth"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='2000-01-01';", dobInput);

            driver.findElement(By.cssSelector("button[type='submit']")).click();
            wait.until(ExpectedConditions.urlContains("/verify-otp"));

            System.out.println("Test E2E réussi : formulaire soumis et redirection OK !");
        } finally {
            driver.quit();
        }
    }
}
