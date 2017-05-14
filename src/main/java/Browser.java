import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Browser {
    private WebDriver webDriver;
    private int timeToWait = 3;

    public Browser(int timeToWait) {
        this.timeToWait = timeToWait;
        init();
    }

    public Browser() {
        init();
    }

    private void init(){
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/vendors/chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
    }

    public void waitForJQueryEnds() {
        while ((Boolean) ((JavascriptExecutor) webDriver).executeScript("return jQuery.active!=0"));
    }

    public boolean isJQueryOnThisPage() {
        return ((JavascriptExecutor) webDriver).executeScript("return (window.jQuery)") != null;
    }

    protected void goToURL(String url) {
        webDriver.get(url);
        waitForJQueryEnds();
    }

    protected boolean isElementPresent(By locator) {
        webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        List<WebElement> listok = webDriver.findElements(locator);
        webDriver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.SECONDS);
        return listok.size() > 0;
    }

    public void quit(){
        webDriver.quit();
    }

    protected void pause() {
        pause(0);
    }

    protected void pause(int timeInSeconds) {
        try {
            if (timeInSeconds > 0) {
                Thread.sleep(timeInSeconds * 1000);
            } else {
                Thread.sleep(250);
            }
        } catch (InterruptedException e) {

        }
    }
}
