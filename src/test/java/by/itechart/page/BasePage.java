package by.itechart.page;

import by.itechart.BaseTest;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {
    Page page;
    Logger log;

    public BasePage(Page page) {
        this.page = page;
        log = BaseTest.getLog();
    }

    BasePage open() {
        return null;
    }

    boolean isOpened() {
        return true;
    }
}