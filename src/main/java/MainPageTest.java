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
        browser = new Browser();
        browser.goToURL(firstPageToSkipFlashUrl);
    }

    @BeforeMethod
    public void startPage(){
        browser.goToURL(mainPageUrl);
        browser.waitForJQueryEnds();
    }

    @AfterSuite
    public void tearDown() throws Exception {
        browser.quit();
    }

    @Test
    public void openMainPage(){
        browser.goToURL(mainPageUrl);
        browser.pause(2);

    }







}
