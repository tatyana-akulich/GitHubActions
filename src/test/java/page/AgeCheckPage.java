package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AgeCheckPage extends BasePage {
    private final Locator viewPageButton;
    private final Locator ageControlText;
    private final Locator yearOfBirthSelect;

    public AgeCheckPage(Page page) {
        super(page);
        this.ageControlText = page.locator("//div[text()=\"Please enter your birth date to continue:\"]");
        this.yearOfBirthSelect = page.locator("#ageYear");
        this.viewPageButton = page.getByText("View Page");
    }

    public Locator getAgeControlText() {
        log.debug("Check if age control page is displayed");
        return ageControlText;
    }

    public AgeCheckPage chooseYearOfBirth(String year) {
        log.debug("Fill year of birth");
        yearOfBirthSelect.selectOption(year);
        return this;
    }

    public AgeCheckPage clickViewPage() {
        log.info("Check if age is suitable -> pass to details page");
        viewPageButton.click();
        return this;
    }

    public void fillCheckAgeBlock() {
        if (getAgeControlText().isVisible()) {
            chooseYearOfBirth("2000").clickViewPage();
        }
    }
}
