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
        Assert.assertTrue(browser.clickElement(By.cssSelector(".main-header__logo")));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals(mainPageUrl));
    }

    @Test
    public void clickPlayButton(){
        Assert.assertTrue(browser.clickElement(By.id("fightActive")));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/battle-ru.html"));
    }

    @Test
    public void clickNextAndPreviousInNewsBlock(){
        String titleOfFirstNews = browser.getElement(By.cssSelector(".news-block__block .title")).getText();
        Assert.assertTrue(browser.clickElement(By.cssSelector(".news-block__buttons>.news-block__button.next")));
        Assert.assertTrue(browser.waitUntilDisappear(By.xpath("//span[text()='"+titleOfFirstNews+"']"),5));
        String newTitle = browser.getElement(By.cssSelector(".news-block__block .title")).getText();
        Assert.assertFalse(titleOfFirstNews.equals(newTitle));
        Assert.assertTrue(browser.clickElement(By.cssSelector(".news-block__buttons>.news-block__button.prev")));
        Assert.assertTrue(browser.waitUntilExist(By.xpath("//span[text()='"+titleOfFirstNews+"']"),5));
    }

    @Test
    public void clickNextAndPreviousInVideoBlock(){
        String linkOnVideo = browser.getElement(By.cssSelector("iframe[class^=video-block__youtube-container]")).getAttribute("src");
        Assert.assertTrue(browser.clickElement(By.cssSelector(".video-block__buttons>.video-block__button.next")));
        Assert.assertTrue(browser.waitUntilDisappear(By.xpath("//iframe[@src='"+linkOnVideo+"']"),5));
        Assert.assertTrue(browser.waitUntilClickable(By.cssSelector(".video-block__buttons>.video-block__button.prev"),5));
        Assert.assertTrue(browser.clickElement(By.cssSelector(".video-block__buttons>.video-block__button.prev")));
        Assert.assertTrue(browser.waitUntilExist(By.xpath("//iframe[@src='"+linkOnVideo+"']"),5));
    }

    @Test
    public void checkChangingLanguage(){
        Assert.assertTrue(browser.changeLanguage("English"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("English"),browser.getChosenLanguage());
        Assert.assertTrue(browser.chooseTopLineElement("Game"));
        Assert.assertTrue(browser.changeLanguage("Русский"));
        Assert.assertTrue(browser.getChosenLanguage().equals("Русский"));
    }

    @Test
    public void checkCookiePanelDisappearAfterClickOK(){
        Assert.assertTrue(browser.isElementPresent(By.className("cookie-policy")));
        Assert.assertTrue(browser.clickElement(By.className("cookie-ok")));
        Assert.assertTrue(browser.waitUntilDisappear(By.className("cookie-policy"),5));
    }

    @Test
    public void checkEULA(){
        browser.closeCockieIfExist();
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение").isDisplayed());
        browser.getElementWithText("Лицензионное соглашение").click();
        browser.waitUntilExist(By.xpath("//p"),5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/eula/"));
        Assert.assertTrue(browser.waitUntilClickable(By.id("fightActive"),5));
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение с Пользователем").isDisplayed());

    }

    @Test
    public void checkRules(){
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());
        browser.getElementWithText("Правила игры").click();
        browser.waitUntilExist(By.xpath("//p"),5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/rules/"));
        Assert.assertTrue(browser.waitUntilClickable(By.id("fightActive"),5));
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());

    }

    @Test
    public void checkPrivacy(){
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());
        browser.getElementWithText("Политика конфиденциальности и cookies").click();
        browser.waitUntilExist(By.xpath("//p"),5);
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/privacy/"));
        Assert.assertTrue(browser.waitUntilClickable(By.id("fightActive"),5));
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());

    }

}
