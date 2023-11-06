package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class SteamDetailsPage extends BasePage {
    private final Locator pricesWithoutDiscount;
    private final Locator pricesWithDiscount;
    private final Locator allDiscounts;

    public SteamDetailsPage(Page page) {
        super(page);
        this.pricesWithoutDiscount = page.locator(".game_purchase_price");
        this.pricesWithDiscount = page.locator(".game_area_purchase .discount_final_price");
        this.allDiscounts = page.locator(".game_area_purchase .discount_pct");
    }

    public List<String> getAllPrices() {
        log.info("Get list of prices on details page");
        log.debug("Get prices with discount");
        List<String> allPrices = pricesWithDiscount.allTextContents();
        log.debug("Add to list prices without discount");
        allPrices.addAll(pricesWithoutDiscount.allTextContents());
        return allPrices;
    }

    public Locator getAllDiscountsLocator() {
        log.debug("Get locator for all discounts");
        return allDiscounts;
    }
}
