package com.log430.brockerx.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.utility.DockerImageName;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@ActiveProfiles("e2e")
public class SignupE2ETest {

    @Test public void signupFlowTest() {
        // On utilise une image officielle Chrome avec Testcontainers
        DockerImageName chromeImage = DockerImageName.parse("selenium/standalone-chrome:latest");

        try (BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>(chromeImage).withCapabilities(
                new org.openqa.selenium.chrome.ChromeOptions().addArguments("--headless").addArguments("--no-sandbox")
                                                              .addArguments("--disable-dev-shm-usage"))) {
            chrome.start();

            WebDriver driver = chrome.getWebDriver();

            String signupUrl = "http://172.17.0.1:8081/signup";
            driver.get(signupUrl);

            // Attente pour que le formulaire soit visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));

            // Remplir le formulaire
            driver.findElement(By.name("email")).sendKeys("test2@example.com");
            driver.findElement(By.name("password")).sendKeys("Password123");
            driver.findElement(By.name("phone")).sendKeys("1234567890");
            driver.findElement(By.name("firstName")).sendKeys("William");
            driver.findElement(By.name("lastName")).sendKeys("Desgagné");
            driver.findElement(By.name("address")).sendKeys("123 Rue Exemple");

            // Date de naissance via Javascript
            WebElement dobInput = driver.findElement(By.name("dateOfBirth"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='2000-01-01';", dobInput);

            // Soumettre le formulaire
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Vérifier redirection vers la page OTP
            wait.until(ExpectedConditions.urlContains("/verify-otp"));

            System.out.println("Test E2E réussi : formulaire soumis et redirection OK !");
        }
    }
}
