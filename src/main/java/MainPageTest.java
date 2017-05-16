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
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("Для мобильных")));
    }

    @Test
    public void navigateToTournaments(){
        Assert.assertTrue(browser.chooseTopLineElement("Турниры"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tournament.tankionline.com/ru/"));
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("Основные положения")));
    }

    @Test
    public void navigateToForum(){
        Assert.assertTrue(browser.chooseTopLineElement("Форум"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://ru.tankiforum.com/"));
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("Форумы")));
    }

    @Test
    public void navigateToWiki(){
        Assert.assertTrue(browser.chooseTopLineElement("Вики"));
        Assert.assertTrue(browser.getCurrentUrl().startsWith("http://ru.tankiwiki.com/"));
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("Wiki-энциклопедия")));
    }

    @Test
    public void navigateToRatings(){
        Assert.assertTrue(browser.chooseTopLineElement("Рейтинги"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://ratings.tankionline.com/ru/"));
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("Топ-100 игроков")));
    }

    @Test
    public void navigateToHelp(){
        Assert.assertTrue(browser.chooseTopLineElement("Помощь"));
        Assert.assertTrue(browser.getCurrentUrl().equals("http://help.tankionline.com/"));
        Assert.assertTrue(browser.isElementPresent(browser.getElementByText("База знаний")));
    }

    @Test
    public void clickMainLogoLeadToMainPage(){
        Assert.assertTrue(browser.clickMainLogo());
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals(mainPageUrl));
    }

    @Test
    public void clickPlayButton(){
        Assert.assertTrue(browser.clickPlayButton());
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/battle-ru.html"));
    }

    @Test
    public void clickNextAndPreviousInNewsBlock(){
        String titleOfFirstNews = browser.getElement(browser.newsBlockFirstTitle).getText();
        Assert.assertTrue(browser.clickNewsBlockNext());
        Assert.assertTrue(browser.waitUntilDisappear(browser.getElementByText(titleOfFirstNews),5));
        String newTitle = browser.getElement(browser.newsBlockFirstTitle).getText();
        Assert.assertFalse(titleOfFirstNews.equals(newTitle));
        Assert.assertTrue(browser.clickNewsBlockPrev());
        Assert.assertTrue(browser.waitUntilExist(browser.getElementByText(titleOfFirstNews),5));
    }

    @Test
    public void clickNextAndPreviousInVideoBlock(){
        String linkOnVideo = browser.getElement(browser.videoBlock).getAttribute("src");
        Assert.assertTrue(browser.clickVideoBlockNext());
        Assert.assertTrue(browser.waitUntilDisappear(browser.iframeWithVideo(linkOnVideo),5));
        Assert.assertTrue(browser.waitUntilClickable(browser.videoBlockPrevButton,5));
        Assert.assertTrue(browser.clickVideoBlockPrev());
        Assert.assertTrue(browser.waitUntilExist(browser.iframeWithVideo(linkOnVideo),5));
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
    public void checkCookieContainsPrivacyLink(){
        Assert.assertTrue(browser.isElementPresent(browser.cookiePanel));
        Assert.assertTrue(browser.clickCookieLink());
        browser.waitForText();
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/privacy/"));
    }

    @Test
    public void checkCookiePanelDisappearAfterClickOK(){
        Assert.assertTrue(browser.isElementPresent(browser.cookiePanel));
        Assert.assertTrue(browser.clickCookieOkButton());
        Assert.assertTrue(browser.waitUntilDisappear(browser.cookiePanel,5));
    }

    @Test
    public void checkEULA(){
        browser.closeCockieIfExist();
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение").isDisplayed());
        browser.getElementWithText("Лицензионное соглашение").click();
        browser.waitForText();
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/eula/"));
        Assert.assertTrue(browser.waitForPlayButtonClickable());
        Assert.assertTrue(browser.getElementWithText("Лицензионное соглашение с Пользователем").isDisplayed());
    }

    @Test
    public void checkRules(){
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());
        browser.getElementWithText("Правила игры").click();
        browser.waitForText();
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/rules/"));
        Assert.assertTrue(browser.waitForPlayButtonClickable());
        Assert.assertTrue(browser.getElementWithText("Правила игры").isDisplayed());
    }

    @Test
    public void checkPrivacy(){
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());
        browser.getElementWithText("Политика конфиденциальности и cookies").click();
        browser.waitForText();
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getCurrentUrl().equals("http://tankionline.com/ru/privacy/"));
        Assert.assertTrue(browser.waitForPlayButtonClickable());
        Assert.assertTrue(browser.getElementWithText("Политика конфиденциальности и cookies").isDisplayed());
    }

}
