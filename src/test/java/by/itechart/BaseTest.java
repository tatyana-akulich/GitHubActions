package by.itechart;

import by.itechart.util.LogConfigurator;
import by.itechart.util.PropertiesLoader;
import com.microsoft.playwright.*;
import io.qameta.allure.AllureLifecycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Paths;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    BrowserType browserType;
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
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        mainPage = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.pages().get(context.pages().size() - 1).screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("final_screenshot.png"))
                .setFullPage(true));
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
    }


    void launchBrowser() {
        playwright = Playwright.create();
        browserType = null;
        String browserName = PropertiesLoader.getBrowserName();
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

    public void configAllure(AllureLifecycle lifecycle) {
        lifecycle.updateTestCase(testResult -> testResult.setName(browserType.name() + this.getClass().getSimpleName()));
        lifecycle.updateTestCase(testResult -> testResult.setHistoryId(testResult.getHistoryId() + browserType.name()));
        log.info("Browser - {}", browserType.name());
    }
}
