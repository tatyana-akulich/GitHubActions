package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {
    private final Locator categoriesMenuItem;
    private final Locator allItemsMenuItem;
    private final Locator contentSaleSection;
    private final String categoryMenuItem = "//a[@class=\"popup_menu_item\" and normalize-space() = \"%s\"]";

    public HomePage(Page page) {
        super(page);
        this.categoriesMenuItem = page.locator("//a[@class=\"pulldown_desktop\" and text()=\"Categories\"]");
        this.contentSaleSection = page.locator("#SaleSection_13268");
        this.allItemsMenuItem = page.locator("//div[text()=\"All Items\"]");
    }

    public HomePage open() {
        log.info("Open homepage");
        page.navigate("https://store.steampowered.com/");
        return this;
    }

    public void clickCategoriesMenuItem() {
        log.info("Hover on Categories");
        categoriesMenuItem.hover();
    }

    public HomePage clickCategory(String category) {
        log.info("Open category {}", category);
        page.locator(String.format(categoryMenuItem, category)).click();
        log.info("Page url is {}", page.url());
        return this;
    }

    public void scrollTillContentSaleSection() {
        log.debug("Scroll with content section is visible");
        contentSaleSection.scrollIntoViewIfNeeded();
    }

    public void chooseCategory(String category) {
        log.info("Click on necessary category and wait till content part load");
        clickCategory(category);
        scrollUntilElementIsVisible();
        scrollTillContentSaleSection();
    }

    public void scrollUntilElementIsVisible() {
        log.debug("Scroll till All Items menu item is visible");
        while (!(allItemsMenuItem.isVisible())) {
            page.mouse().wheel(0, 600);
        }
    }
}
