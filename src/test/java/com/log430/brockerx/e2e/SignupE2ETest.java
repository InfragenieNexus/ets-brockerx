package com.log430.brockerx.e2e;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class SignupE2ETest {

    public static void main(String[] args) {
        // Assure-toi que le chemin vers chromedriver est correct

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Chrome headless
        options.addArguments("--no-sandbox"); // utile en CI/CD
        options.addArguments("--disable-dev-shm-usage"); // pour Docker
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("http://localhost:8080/signup");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // On attend que le formulaire soit visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));


            // Remplir le formulaire
            driver.findElement(By.name("email")).sendKeys("test2@example.com");
            driver.findElement(By.name("password")).sendKeys("Password123");
            driver.findElement(By.name("phone")).sendKeys("1234567890");
            driver.findElement(By.name("firstName")).sendKeys("William");
            driver.findElement(By.name("lastName")).sendKeys("Desgagné");
            driver.findElement(By.name("address")).sendKeys("123 Rue Exemple");

            WebElement dobInput = driver.findElement(By.name("dateOfBirth"));


            // Remplir directement avec JS
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='2000-01-01';", dobInput);

            // Soumettre le formulaire
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Vérifier qu'on est redirigé (exemple : vers /login après inscription)
            wait.until(ExpectedConditions.urlContains("/verify-otp"));

            System.out.println("Test E2E réussi : formulaire soumis et redirection OK !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
