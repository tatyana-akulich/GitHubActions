import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import dto.Categories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import page.*;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import util.PropertiesLoader;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTest extends BaseTest {
    Page steam;
    HomePage homePage;
    HomeContentPage contentPage;
    SteamDetailsPage detailsPage;
    AgeCheckPage ageCheckPage;
    HeaderPage headerPage;
    InstallSteamPage installSteamPage;
    String priceOnContentPage;
    int maxDiscount;
    double maxPrice;
    String maxDiscountOnContentPage;

    @Test
    public void testInstallSteamWithDiscount() {
        log.info("Start DiscountTest");
        homePage = new HomePage(mainPage);
        contentPage = new HomeContentPage(mainPage);
        homePage.open().clickCategoriesMenuItem();
        String categoryName = Categories.Action.getTitle();
        homePage.chooseCategory(categoryName);

        contentPage.clickAllItems();
        log.warn("Wait till block with steams in All Items is loaded: amount of steams is positive");
        mainPage.waitForCondition(() -> (contentPage.getSteamsWithPriceLocator().count() > 0));
        List<String> pricesInAllItemsSection = contentPage.getSteamsWithPrice();
        log.info("page url is {}", mainPage.url());

        contentPage.clickNewAndTrending();
        log.warn("Wait till block with steams in New & Trending is loaded: " +
                "amount of steams is positive and steams diverse from the ones in All items");
        mainPage.waitForCondition(() -> (contentPage.getSteamsWithPriceLocator().count() > 0)
                & !(contentPage.getSteamsWithPrice().equals(pricesInAllItemsSection)));
        log.info("page url is {}", mainPage.url());
        List<String> discounts = contentPage.getSteamsWithDiscounts();
        if (discounts.size() > 0) {
            log.info("Case when there are discounts on the page");
            maxDiscount = contentPage.getMaxDiscount(discounts);
            log.debug("Format discount to the variant on content page: -price%");
            maxDiscountOnContentPage = "-" + maxDiscount + "%";
            priceOnContentPage = contentPage.getFirstPriceWithMaxDiscount(maxDiscountOnContentPage);
            openSteamWithMaxDiscount();
        } else {
            log.info("Case when there are no discounts on the page");
            maxPrice = contentPage.getMaxPrice();
            log.debug("Format price to the variant on content page: $price .02f");
            priceOnContentPage = "$" + String.format(Locale.US, "%.02f", maxPrice);
            openSteamWithMaxPrice();
        }

        detailsPage = new SteamDetailsPage(steam);
        ageCheckPage = new AgeCheckPage(steam);
        headerPage = new HeaderPage(steam);
        installSteamPage = new InstallSteamPage(steam);
        steam.waitForLoadState(LoadState.LOAD);

        ageCheckPage.fillCheckAgeBlock();

        if (maxDiscount > 0) {
            log.info("Case if there steams with discounts");
            log.info("Assert: details page contains max discount from content page");
            assertThat(detailsPage.getAllDiscountsLocator().allTextContents()).contains(maxDiscountOnContentPage);
        }
        log.info("Assert: details page contains max price from content page");
        assertThat(detailsPage.getAllPrices()).contains(priceOnContentPage + " USD");

        downloadSteam();
        log.info("Finish DiscountTest");
    }

    public void downloadSteam() {
        headerPage.clickInstallSteam();
        log.debug("Wait for download");
        Download download = steam.waitForDownload(installSteamPage::clickInstallSteam);
        log.debug("Create filename suggested from download");
        String fileName = PropertiesLoader.getFolderForDownloadFiles() + "/" + download.suggestedFilename();
        log.debug("Save file with path from filename");
        download.saveAs(Paths.get(fileName));
        File downloadedFile = new File(fileName);
        log.info("Rename downloaded file adding timestamp");
        File renamed = new File(fileName.substring(0, fileName.indexOf(".")) +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".exe");
        log.debug("Check if file is renamed");
        if (downloadedFile.renameTo(renamed)) {
            System.out.printf("File %s was renamed to %s%n", fileName, renamed);
        }
    }

    public void openSteamWithMaxDiscount() {
        log.warn("Wait for steam details page to open");
        steam = context.waitForPage(() -> {
            contentPage.openSteamByDiscount(maxDiscountOnContentPage);
        });
        log.info("page url is {}", steam.url());
    }

    public void openSteamWithMaxPrice() {
        log.warn("Wait for steam details page to open");
        steam = context.waitForPage(() -> {
            contentPage.openSteamByPrice(priceOnContentPage);
        });
        log.info("page url is {}", steam.url());
    }
}

