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
    public void checkChangingLanguage(){
        Assert.assertTrue(browser.changeLanguage("English"));
        browser.waitForJQueryEnds();
        Assert.assertTrue(browser.getChosenLanguage().equals("English"),browser.getChosenLanguage());
        Assert.assertTrue(browser.chooseTopLineElement("Game"));
        Assert.assertTrue(browser.changeLanguage("Русский"));
        Assert.assertTrue(browser.getChosenLanguage().equals("Русский"));
    }












}
