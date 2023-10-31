import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import util.LogConfigurator;
import util.PropertiesLoader;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page mainPage;
    public static Logger log;

    @BeforeAll
    void beforeAll() {
        String testName = this.getClass().getSimpleName();
        PropertiesLoader.loadProperties(testName);
        LogConfigurator.configureLogs();
        log = LogManager.getLogger();
        launchBrowser();
    }

    @AfterAll
    void afterAll() {
        closePlaywright();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setLocale("en-GB"));
        mainPage = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    void launchBrowser() {
        playwright = Playwright.create();
        BrowserType browserType = null;
        String browserName = "chrome";
        if (System.getenv("browserName")!= null) {
            browserName = System.getenv("browserName");
        }
        switch (browserName) {
            case "chrome": {
                browserType = playwright.chromium();
                break;
            }
            case "webkit": {
                browserType = playwright.webkit();
                break;
            }
            case "firefox": {
                browserType = playwright.firefox();
                break;
            }
            default: {
                browserType = playwright.chromium();
            }
        }
        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(PropertiesLoader.isHeadless()).setSlowMo(50));
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public static Logger getLog() {
        return log;
    }
}
