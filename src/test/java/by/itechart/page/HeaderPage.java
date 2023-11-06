package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeaderPage extends BasePage {
    private final Locator installSteamButton;

    public HeaderPage(Page page) {
        super(page);
        this.installSteamButton = page.locator("//div[normalize-space() = \"Install Steam\"]");
    }

    public void clickInstallSteam() {
        log.info("Open steam install page");
        installSteamButton.nth(0).click();
    }
}
