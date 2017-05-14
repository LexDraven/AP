import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Browser {
    private WebDriver webDriver;
    private int timeToWait = 3;
    private String mainPageUrl;

    public Browser(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
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

    private  boolean isLinkAlive (String URL){
        HttpResponse response;
        try {
            RequestConfig config= RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setSocketTimeout(15000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
            HttpClient client= HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet request = new HttpGet(URL);
            response = client.execute(request);
        } catch (Exception e) {
            System.err.println(URL + " error " + e.getLocalizedMessage());
            return false;
        }
        return  response.getStatusLine().getStatusCode()==200;
    }

    public boolean allLinksOnPageAlive(){
        List<WebElement> list = webDriver.findElements(By.tagName("a"));
        boolean result=true;
        for(WebElement webElement:list){
            String url = webElement.getAttribute("href");
            if (!url.startsWith("http")){
               url=mainPageUrl+url;
            }
            if (!isLinkAlive(url)) {
                System.out.println("Failed link: "+url);
                result = false;
            }
        }
        return result;
    }

    public boolean chooseTopLineElement(String name){
        if (!isElementPresent(By.className("top-line__nav"))) {
            return false;
        }
        if (!isElementPresent(By.xpath("//a[contains(.,'"+name+"')]"))) {
            return false;
        }
        return clickElement(By.xpath("//a[contains(.,'"+name+"')]"));
    }

    protected boolean clickElement(By locator) {
        try {
            webDriver.findElement(locator).click();
            return true;
        } catch (WebDriverException e) {
            System.out.println(e.getMessage() + locator.toString());
            return false;
        }
    }

    public String getCurrentUrl(){
        return webDriver.getCurrentUrl();
    }

    public boolean changeLanguage (String newLanguage){
        if (getChosenLanguage().equals(newLanguage)){
            return true;
        }
        waitUntilVisible(By.cssSelector(".custom-select__active"),5);
        if (!clickElement(By.cssSelector(".custom-select__active"))) {
            return false;
        }
        clickElement(By.xpath("//span[text()='"+newLanguage+"']/parent::div"));
        return waitUntilDisappear(By.cssSelector("custom-select__options"),5);
    }

    public String getChosenLanguage(){
        if (!isElementPresent(By.cssSelector(".custom-select__active"))){
            return "no language menu";
        }
        String text =(String) ((JavascriptExecutor)webDriver).executeScript("return document.querySelector('.custom-select__active .text').innerText");
        return text;
    }

    protected boolean waitUntilExist(By locator, int time) { //ожидание появления всех элементов класса
        return waitUntilConditions(locator, time, 0);
    }

    protected boolean waitUntilVisible(By locator, int time) { //ожидание видимости элемента
        return waitUntilConditions(locator, time, 1);
    }

    protected boolean waitUntilClickable(By locator, int time) { //ожидание возможности нажатия
        return waitUntilConditions(locator, time, 2);
    }

    protected boolean waitUntilDisappear(By locator, int time) { //ожидаем исчезновения элемента
        return waitUntilConditions(locator, time, 3);
    }

    private boolean waitUntilConditions(By locator, int time, int type) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, time);
            if (type == 0) { //exist
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            }
            if (type == 1) { //visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            }
            if (type == 2) { //clickable
                wait.until(ExpectedConditions.elementToBeClickable(locator));
            }
            if (type == 3) { //disappear
                wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            }
        } catch (TimeoutException ex) {
            return false;
        }
        return true;
    }

    public WebElement getElement(By locator){
        return webDriver.findElement(locator);
    }

    public WebElement getElementWithText(String text){
        return getElement(By.xpath("//*[text()='"+text+"']"));
    }

    public void closeCockieIfExist() {
      if (getElement(By.className("cookie-policy")).isDisplayed()) {
          clickElement(By.className("cookie-ok"));
          waitUntilDisappear(By.className("cookie-policy"),5);
          waitForJQueryEnds();
      }
    }
}
