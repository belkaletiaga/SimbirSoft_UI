package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductItem;
import pages.ProductPage;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class RandomAddToCartTest extends BaseTest {

    @Test
    @AllureId("TC-06")
    @Tag("Positive")
    @DisplayName("Добавить 5 случайных товаров с главной с рандомным количеством, удалить чётные по порядку, проверить сумму")
    void randomAddAndRemoveEvenTest() throws InterruptedException {
        HomePage homePage = new HomePage(driver, wait);
        List<ProductItem> allProducts = homePage.getAllProducts();
         Assertions.assertTrue(allProducts.size() >= 5, "Недостаточно товаров на главной");

        // Выбрать 5 случайных товаров
        List<ProductItem> selected = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductItem randomProduct;
            do {
                randomProduct = allProducts.get(RandomUtils.getRandomInt(0, allProducts.size() - 1));
            } while (selected.contains(randomProduct));
            selected.add(randomProduct);
        }

        // Добавить каждый с рандомным количеством
        for (ProductItem product : selected) {
            int quantity = RandomUtils.getRandomInt(1, 5);

            driver.get(BASE_URL + "index.php?rt=product/product&product_id=" + product.getId());
            ProductPage productPage = new ProductPage(driver, wait);

            productPage.setQuantity(quantity);
            Thread.sleep(500);
            productPage.addToCart();
            Thread.sleep(500);
        }
            // Перейти в корзину
            CartPage cartPage = new CartPage(driver, wait);
            cartPage.openCart();

            // Удалить все чётные по порядку (2-й, 4-й, ...)
            List<CartPage.CartItem> items = cartPage.getCartItems();
            List<Integer> indexesToRemove = new ArrayList<>();
               for (int i = 0; i < items.size(); i++) {
                   if ((i + 1) % 2 == 0) { // позиция 2,4,6...
                    indexesToRemove.add(i);
               }
            }


        for (int j = indexesToRemove.size() - 1; j >= 0; j--) {
            int index = indexesToRemove.get(j);
            CartPage.CartItem item = cartPage.getCartItems().get(index);
            cartPage.removeItem(item);
            Thread.sleep(500); // небольшая пауза для обновления DOM
        }

            // Проверить итоговую сумму
            items = cartPage.getCartItems();
            double expectedTotal = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
            double actualTotal = cartPage.getTotalPrice();
            Assertions.assertEquals(expectedTotal, actualTotal, 0.01);
        }
}