import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
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
    private RequestConfig config;
    private CloseableHttpClient client;
    private HttpGet request;

    public By mainLogo = By.cssSelector(".main-header__logo");
    public By playButton = By.id("fightActive");
    public By newsBlockFirstTitle = By.cssSelector(".news-block__block .title");
    public By newsBlockNextButton = By.cssSelector(".news-block__buttons>.news-block__button.next");
    public By newsBlockPrevButton = By.cssSelector(".news-block__buttons>.news-block__button.prev");
    public By cookiePanel = By.className("cookie-policy");
    public By cookieOkButton = By.className("cookie-ok");
    public By textBlock = By.xpath("//p");
    public By videoBlockNextButton = By.cssSelector(".video-block__buttons>.video-block__button.next");
    public By videoBlockPrevButton = By.cssSelector(".video-block__buttons>.video-block__button.prev");
    public By videoBlock = By.cssSelector("iframe[class^=video-block__youtube-container]");

    public By getElementByText(String title) {
        return By.xpath("//*[text()='"+title+"']");
    }

    public By iframeWithVideo(String linkOnVideo) {
        return By.xpath("//iframe[@src='"+linkOnVideo+"']");
    }

    public Browser(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
        init();
    }

    private void init() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/vendors/chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        config = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public void waitForJQueryEnds() {
            while ((Boolean) ((JavascriptExecutor) webDriver).executeScript("return jQuery.active!=0"));
    }

    public boolean isJQueryOnThisPage() {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        Object result = executor.executeScript("return (window.jQuery)");
        return result != null;
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

    public void quit() {
        webDriver.quit();
    }

    private boolean isLinkAlive(String URL) {
        HttpResponse response;
        try {
            client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            request = new HttpGet(URL);
            request.addHeader("User-Agent","Mozilla/5.0");
            response = client.execute(request);
            client.close();
        } catch (Exception e) {
            System.err.println(URL + " error " + e.getLocalizedMessage());
            return false;
        }
        return response.getStatusLine().getStatusCode() == 200;
    }

    public boolean allLinksOnPageAlive() {
        List<WebElement> list = webDriver.findElements(By.tagName("a"));
        boolean result = true;
        for (WebElement webElement : list) {
            String url = webElement.getAttribute("href");
            if (!url.startsWith("http")) {
                url = mainPageUrl + url;
            }
            if (!isLinkAlive(url)) {
                System.out.println("Failed link: " + url);
                result = false;
            }
        }
        return result;
    }

    public boolean chooseTopLineElement(String name) {
        if (!isElementPresent(By.className("top-line__nav"))) {
            return false;
        }
        if (!isElementPresent(By.xpath("//a[contains(.,'" + name + "')]"))) {
            return false;
        }
        return clickElement(By.xpath("//a[contains(.,'" + name + "')]"));
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

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public boolean changeLanguage(String newLanguage) {
        if (getChosenLanguage().equals(newLanguage)) {
            return true;
        }
        waitUntilVisible(By.cssSelector(".custom-select__active"), 5);
        if (!clickElement(By.cssSelector(".custom-select__active"))) {
            return false;
        }
        clickElement(By.xpath("//span[text()='" + newLanguage + "']/parent::div"));
        return waitUntilDisappear(By.cssSelector("custom-select__options"), 5);
    }

    public String getChosenLanguage() {
        if (!isElementPresent(By.cssSelector(".custom-select__active"))) {
            return "no language menu";
        }
        String text = (String) ((JavascriptExecutor) webDriver).executeScript("return document.querySelector('.custom-select__active .text').innerText");
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

    public WebElement getElement(By locator) {
        return webDriver.findElement(locator);
    }

    public WebElement getElementWithText(String text) {
        return getElement(By.xpath("//*[text()='" + text + "']"));
    }

    public void closeCockieIfExist() {
        if (getElement(By.className("cookie-policy")).isDisplayed()) {
            clickElement(By.className("cookie-ok"));
            waitUntilDisappear(By.className("cookie-policy"), 5);
            waitForJQueryEnds();
        }
    }

    public boolean clickMainLogo() {
        return clickElement(mainLogo);
    }

    public boolean clickPlayButton() {
        return clickElement(playButton);
    }

    public boolean clickNewsBlockNext() {
        return clickElement(newsBlockNextButton);
    }

    public boolean clickNewsBlockPrev() {
        return clickElement(newsBlockPrevButton);
    }

    public boolean clickVideoBlockNext() {
        return clickElement(videoBlockNextButton);
    }

    public boolean clickVideoBlockPrev() {
        return clickElement(videoBlockPrevButton);
    }

    public boolean clickCookieLink() {
        return clickElement(By.cssSelector(".cookie-policy a"));
    }

    public boolean clickCookieOkButton() {
        return clickElement(cookieOkButton);
    }

    public boolean waitForPlayButtonClickable() {
        return waitUntilClickable(playButton,5);
    }

    public boolean waitForText() {
        return waitUntilExist(textBlock,5);
    }
}
