package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import pages.*;
import utils.RandomUtils;
import utils.SortType;
import utils.WaitHelper;

import java.util.List;

/**
 * Тест-класс для проверки поисковой выдачи и корзины
 */
public class SearchTest extends BaseTest{

    @Test
    @AllureId("TC-05")
    @Tags({@Tag("Positive"), @Tag("Search")})
    @DisplayName("Поиск 'shirt', сортировка по имени, добавление 2 и 3 товара с рандомным количеством, удвоение самого дешёвого в корзине, проверка суммы")
    void searchShirtAndCartTest() {
        HomePage homePage = new HomePage(driver, wait);
        homePage.searchFor("shirt");

        SearchResultsPage resultsPage = new SearchResultsPage(driver, wait);
        resultsPage.selectSortOption(SortType.NAME_A_Z);

        String searchResultsUrl = driver.getCurrentUrl();

        List<ProductItem> products = resultsPage.getProducts();
        Assertions.assertTrue(products.size() >= 3, "Должно быть минимум 3 товара");

        int quantity1 = RandomUtils.getRandomInt(1, 5);
        int quantity2 = RandomUtils.getRandomInt(1, 5);

        ProductPage productPage = resultsPage.openProduct(1);
        productPage.setQuantity(quantity1);
        productPage.addToCart();

        driver.get(searchResultsUrl);
        WaitHelper.waitForTextToBePresent(wait, productPage.getTextTitle(),"SEARCH");
        resultsPage = new SearchResultsPage(driver, wait);

        productPage = resultsPage.openProduct(2);
        productPage.setQuantity(quantity2);
        productPage.addToCart();

        CartPage cartPage = new CartPage(driver, wait);
        cartPage.doubleCheapestItemQuantity(cartPage.cheapestItemQuantity());

        double actualTotal = cartPage.getTotalPrice();
        double expectedTotal = cartPage.calculateExpectedTotal();
        Assertions.assertEquals(expectedTotal, actualTotal, 0.01);
    }
}
