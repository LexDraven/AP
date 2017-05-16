import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class MainPageTest {
    private Browser browser;
    private final String mainPageUrl="http://tankionline.com/ru/";
    private final String firstPageToSkipFlashUrl="http://tankionline.com/battle-ru.html#/server=RU19";
    private By mainLogo = By.cssSelector(".main-header__logo");
    private By playButton = By.id("fightActive");
    private By newsBlockFirstTitle = By.cssSelector(".news-block__block .title");
    private By newBlockNextButton = By.cssSelector(".news-block__buttons>.news-block__button.next");
    private By newsBlockPrevButton = By.cssSelector(".news-block__buttons>.news-block__button.prev");
    private By cookiePanel = By.className("cookie-policy");
    private By cookieOkButton = By.className("cookie-ok");
    private By textBlock = By.xpath("//p");
    private By videoBlockNextButton = By.cssSelector(".video-block__buttons>.video-block__button.next");
    private By videoBlockPrevButton = By.cssSelector(".video-block__buttons>.video-block__button.prev");
    private By videoBlock = By.cssSelector("iframe[class^=video-block__youtube-container]");

    private By getElementByText(String title) {
        return By.xpath("//*[text()='"+title+"']");
    }

    private By iframeWithVideo(String linkOnVideo) {
        return By.xpath("//iframe[@src='"+linkOnVideo+"']");
    }

    @BeforeSuite
    public void setUp() throws Exception {
        browser = new Browser(mainPageUrl);
        browser.goToURL(firstPageToSkipFlashUrl);
    }

    @BeforeMethod
    public void startPage(){
        browser.goToURL(mainPageUrl);
    }

    @AfterSuite
    public void tearDown() throws Exception {
        browser.quit();
    }

    @Test
    public void openMainPageAndCheckAllLinks(){
        Assert.assertTrue(browser.allLinksOnPageAlive());
    }

    @Test
    public void navigateToMedia(){
        Assert.assertTrue(browser.chooseTopLineElement("Материалы"));
        Assert.assertTrue(browser.getCurrentUrl().endsWith("/media/"));
    }

    @Test
    public void navigateToTournaments(){
        Assert.assertTrue(browser.chooseTopLineElement("Турниры"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tournament.tankionline.com/ru/"));
    }

    @Test
    public void navigateToForum(){
        Assert.assertTrue(browser.chooseTopLineElement("Форум"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://ru.tankiforum.com/"));
    }

    @Test
    public void navigateToWiki(){
        Assert.assertTrue(browser.chooseTopLineElement("Вики"));
        Assert.assertTrue(browser.getCurrentUrl().startsWith("http://ru.tankiwiki.com/"));
    }

    @Test
    public void navigateToRatings(){
        Assert.assertTrue(browser.chooseTopLineElement("Рейтинги"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://ratings.tankionline.com/ru/"));
    }

    @Test
    public void navigateToHelp(){
        Assert.assertTrue(browser.chooseTopLineElement("Помощь"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://help.tankionline.com/"));
    }

    @Test
    public void clickMainLogoLeadToMainPage(){
        Assert.assertTrue(browser.clickElement(mainLogo));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals(mainPageUrl));
    }

    @Test
    public void clickPlayButton(){
        Assert.assertTrue(browser.clickElement(playButton));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/battle-ru.html"));
    }

    @Test
    public void clickNextAndPreviousInNewsBlock(){
        String titleOfFirstNews = browser.getElement(newsBlockFirstTitle).getText();
        Assert.assertTrue(browser.clickElement(newBlockNextButton));
        Assert.assertTrue(browser.waitUntilDisappear(getElementByText(titleOfFirstNews),5));
        String newTitle = browser.getElement(newsBlockFirstTitle).getText();
        Assert.assertFalse(titleOfFirstNews.equals(newTitle));
        Assert.assertTrue(browser.clickElement(newsBlockPrevButton));
        Assert.assertTrue(browser.waitUntilExist(getElementByText(titleOfFirstNews),5));
    }

    @Test
    public void clickNextAndPreviousInVideoBlock(){
        String linkOnVideo = browser.getElement(videoBlock).getAttribute("src");
        Assert.assertTrue(browser.clickElement(videoBlockNextButton));
        Assert.assertTrue(browser.waitUntilDisappear(iframeWithVideo(linkOnVideo),5));
        Assert.assertTrue(browser.waitUntilClickable(videoBlockPrevButton,5));
        Assert.assertTrue(browser.clickElement(videoBlockPrevButton));
        Assert.assertTrue(browser.waitUntilExist(iframeWithVideo(linkOnVideo),5));
    }

    @Test
    public void checkChangingLanguageEnglish(){
        Assert.assertTrue(browser.changeLanguage("English"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("English"),browser.getChosenLanguage());
    }

    @Test
    public void checkChangingLanguageChina(){
        Assert.assertTrue(browser.changeLanguage("中文"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("中文"),browser.getChosenLanguage());
    }

    @Test
    public void checkChangingLanguageSpanish(){
        Assert.assertTrue(browser.changeLanguage("Español"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("Español"),browser.getChosenLanguage());
    }

    @Test
    public void checkChangingLanguagePoland(){
        Assert.assertTrue(browser.changeLanguage("Polski"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("Polski"),browser.getChosenLanguage());
    }

    @Test
    public void checkChangingLanguagePortugues(){
        Assert.assertTrue(browser.changeLanguage("Português"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("Português"),browser.getChosenLanguage());
    }

    @Test
    public void checkChangingLanguageDeutch(){
        Assert.assertTrue(browser.changeLanguage("Deutsch"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("Deutsch"),browser.getChosenLanguage());
    }

    @Test
    public void checkCookiePanelDisappearAfterClickOK(){
        Assert.assertTrue(browser.isElementPresent(cookiePanel));
        Assert.assertTrue(browser.clickElement(cookieOkButton));
        Assert.assertTrue(browser.waitUntilDisappear(cookiePanel,5));
    }

    @Test
    public void checkEULA(){
        browser.closeCockieIfExist();
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение").isDisplayed());
        browser.getElementWithText("Лицензионное соглашение").click();
        browser.waitUntilExist(textBlock,5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/eula/"));
        Assert.assertTrue(browser.waitUntilClickable(playButton,5));
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение с Пользователем").isDisplayed());
    }

    @Test
    public void checkRules(){
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());
        browser.getElementWithText("Правила игры").click();
        browser.waitUntilExist(textBlock,5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/rules/"));
        Assert.assertTrue(browser.waitUntilClickable(playButton,5));
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());
    }

    @Test
    public void checkPrivacy(){
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());
        browser.getElementWithText("Политика конфиденциальности и cookies").click();
        browser.waitUntilExist(textBlock,5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/privacy/"));
        Assert.assertTrue(browser.waitUntilClickable(playButton,5));
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());
    }

}
