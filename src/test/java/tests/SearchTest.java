package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;
import utils.RandomUtils;
import utils.SortType;
import utils.WaitHelper;

import java.util.List;

public class SearchTest extends BaseTest{

    @Test
    @AllureId("TC-05")
    @Tag("Positive")
    @DisplayName("Поиск 'shirt', сортировка по имени, добавление 2 и 3 товара с рандомным количеством, удвоение самого дешёвого в корзине, проверка суммы")
    void searchShirtAndCartTest() throws InterruptedException {
        // Поиск "shirt"
        HomePage homePage = new HomePage(driver, wait);
        homePage.searchFor("shirt");

        SearchResultsPage resultsPage = new SearchResultsPage(driver, wait);
        resultsPage.selectSortOption(SortType.NAME_A_Z);
        List<ProductItem> products = resultsPage.getProducts();
        Assertions.assertTrue(products.size() >= 3, "Должно быть минимум 3 товара");

        // Добавить 2-й и 3-й товар с рандомным количеством (от 1 до 5)
        int quantity1 = RandomUtils.getRandomInt(1, 5);
        int quantity2 = RandomUtils.getRandomInt(1, 5);

        ProductPage productPage = resultsPage.openProduct(1); // второй товар (индекс 1)
        productPage.setQuantity(quantity1);
        productPage.addToCart();
        driver.navigate().back(); // вернуться к результатам
        driver.navigate().back();
        Thread.sleep(200);
        resultsPage = new SearchResultsPage(driver, wait);
        productPage = resultsPage.openProduct(2); // третий товар (индекс 2)
        productPage.setQuantity(quantity2);
        productPage.addToCart();

        CartPage cartPage1 = new CartPage(driver, wait);

        // Найти самый дешёвый товар
        List<CartPage.CartItem> itemOlds = cartPage1.getCartItems();
        CartPage.CartItem cheapest = itemOlds.stream().min((a, b) -> Double.compare(a.getPrice(), b.getPrice())).orElseThrow();
        int oldQty = cheapest.getQuantity();
        cartPage1.updateQuantity(cheapest, oldQty * 2);


        // Проверить итоговую сумму
        CartPage cartPage2 = new CartPage(driver, wait);
        List<CartPage.CartItem> itemNew = cartPage2.getCartItems();
        double expectedTotal = 0.0;
        for (CartPage.CartItem item : itemNew) {
            expectedTotal += item.getPrice() * item.getQuantity();
        }
        double actualTotal = cartPage2.getTotalPrice();
        Assertions.assertEquals(expectedTotal, actualTotal, 0.01);
    }
}
