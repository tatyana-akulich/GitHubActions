package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeContentPage extends BasePage {
    private final Locator newAndTrendingMenuItem;
    private final Locator allItemsMenuItem;
    private final Locator discountText;
    private final Locator priceText;
    private final Locator dateText;
    private final String priceByDiscountText = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/following-sibling::div/div[@class=\"salepreviewwidgets_StoreSalePriceBox_Wh0L8\"]";
    private final String titleByDiscountLink = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/ancestor::div[@class=\"salepreviewwidgets_SaleItemBrowserRow_y9MSd\"]" +
            "//div[@class=\"salepreviewwidgets_StoreSaleWidgetHalfLeft_2Va3O\"]";
    private final String titleByPriceLink = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/ancestor::div[@class=\"salepreviewwidgets_SaleItemBrowserRow_y9MSd\"]" +
            "//div[@class=\"salepreviewwidgets_StoreSaleWidgetHalfLeft_2Va3O\"]";

    public HomeContentPage(Page page) {
        super(page);
        this.newAndTrendingMenuItem = page.locator("//div[text()=\"New & Trending\"]");
        this.discountText = page.locator("//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
                "//div[@class=\"salepreviewwidgets_StoreSaleDiscountBox_2fpFv\"]");
        this.priceText = page.locator("//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
                "//div[@class=\"salepreviewwidgets_StoreSalePriceBox_Wh0L8\"]");
        this.allItemsMenuItem =page.locator("//div[text()=\"All Items\"]");
        this.dateText = page.locator(".facetedbrowse_FacetedBrowseItems_NO-IP .salepreviewwidgets_StoreSaleWidgetRelease_3eOdk");
    }

    public void clickNewAndTrending() {
        log.debug("Choose New & Trending section");
        newAndTrendingMenuItem.click();
    }

    public List<String> getSteamsWithDiscounts() {
        log.debug("Get list of discounts from New & Trending");
        return discountText.allTextContents();
    }

    public List<String> getSteamsWithPrice() {
        log.debug("Get list of prices from New & Trending");
        return priceText.allTextContents();
    }

    public Locator getSteamsWithPriceLocator(){
        return priceText;
    }

    public String getFirstPriceWithMaxDiscount(String discountForLocator) {
        log.debug("Looking for price of first steam with maximum discount - {}", discountForLocator);
        return page.locator(String.format(priceByDiscountText, discountForLocator)).nth(0).textContent();
    }

    public void openSteamByDiscount(String discountForLocator) {
        log.info("Open first steam with maximum discount - {}", discountForLocator);
        page.locator(String.format(titleByDiscountLink, discountForLocator)).nth(0).click();
        log.info("page url is {}", page.url());
    }

    public void openSteamByPrice(String priceForLocator) {
        log.info("Open first steam with maximum price - {}, if there are no discounts", priceForLocator);
        page.locator(String.format(titleByPriceLink, priceForLocator)).nth(0).click();
        log.info("page url is {}", page.url());
    }

    public void clickAllItems(){
        log.debug("Choose All items section");
        allItemsMenuItem.click();
    }

    public Double getMaxPrice() {
        log.info("Get max price from New & Trending");
        log.debug("Get all prices as list");
        List<String> prices = getSteamsWithPrice();
        log.debug("Add numeric part of prices to list, excluding free steams, using regex");
        List<Double> pricesDouble = new ArrayList<>();
        for (String element : prices
        ) {
            if (element.matches("\\$[\\d]+\\.?[\\d]*")) {
                pricesDouble.add(Double.parseDouble(element.substring(1)));
            }
        }
        log.debug("Get max price");
        return Collections.max(pricesDouble);
    }

    public int getMaxDiscount(List<String> discounts) {
        log.info("Get max discount in New & Trending");
        log.debug("Get all discounts from locator");
        List<Integer> discountsInt = new ArrayList<>();
        log.debug("Adding numeric parts of discounts into list using regex");
        for (String element : discounts
        ) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(element);
            if (matcher.find()) {
                discountsInt.add(Integer.parseInt(matcher.group()));
            }
        }
        log.debug("Get max discount");
        return Collections.max(discountsInt);
    }
}
