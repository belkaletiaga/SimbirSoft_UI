package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import pages.CartPage;
import pages.HomePage;
import pages.ProductItem;
import pages.ProductPage;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Тест-класс для проверки корзины
 */
public class RandomAddToCartTest extends BaseTest {

    @Test
    @AllureId("TC-06")
    @Tags({@Tag("Positive"), @Tag("RandomAddToCart")})
    @DisplayName("Добавить 5 случайных товаров с главной с рандомным количеством, удалить чётные по порядку, проверить сумму")
    void randomAddAndRemoveEvenTest() {
        HomePage homePage = new HomePage(driver, wait);
        homePage.addRandomProductsToCart(5);

        CartPage cartPage = new CartPage(driver, wait);
        cartPage.removeEvenItems();

        double expectedTotal = cartPage.calculateExpectedTotal();
        double actualTotal = cartPage.getTotalPrice();
        Assertions.assertEquals(expectedTotal, actualTotal, 0.01);
    }
}