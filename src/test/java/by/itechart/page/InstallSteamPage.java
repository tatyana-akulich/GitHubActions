package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class InstallSteamPage extends BasePage {
    private final Locator installSteamButton;

    public InstallSteamPage(Page page) {
        super(page);
        this.installSteamButton = page.locator("//a[@class = \"about_install_steam_link\" and text()=\"Install Steam\"]");
    }

    public void clickInstallSteam() {
        log.info("Click install button on Install page");
        installSteamButton.nth(0).click();
    }
}
