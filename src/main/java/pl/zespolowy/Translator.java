package pl.zespolowy;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Objects;

@Getter
public class Translator {
    private WebDriver driver;

    public Translator() {
        initDriver();
    }

    public void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("no-sandbox", "headless");
        driver = new ChromeDriver(options);
    }

    public Translation translate(String content, String sourceLanguage, String targetLanguage) {
        String baseUrl = "https://translate.google.com/?sl=" + sourceLanguage + "&tl=" + targetLanguage + "&op=translate";
        driver.get(baseUrl);

        if (!Objects.equals(driver.getCurrentUrl(), baseUrl)) {
            var button = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/c-wiz/div/div/div/div[2]/div[1]/div[3]/div[1]/div[1]/form[1]/div/div/button"));
            button.click();
        }


        var inputBox = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/c-wiz/div/div[2]/c-wiz/div[2]/c-wiz/div[1]/div[2]/div[2]/div/c-wiz/span/span/div/textarea"));
        inputBox.sendKeys(content);

        while (true) {
            try {
                Thread.sleep(10); // Sleep for 10 milliseconds
                var outputBox = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/c-wiz/div/div[2]/c-wiz/div[2]/c-wiz/div[1]/div[2]/div[2]/c-wiz/div/div[6]/div/div[1]/span[1]"));
                return new Translation(outputBox.getText());
            } catch (Exception e) {}
        }

    }

    public void refreshDriver() {
        if (driver != null) {
            driver.quit();
        }
        initDriver();
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}