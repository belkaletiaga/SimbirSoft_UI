package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import pages.CategoryPage;
import pages.HomePage;
import utils.SortType;

/**
 * Тест-класс для проверки сортировки товаров в категориях
 * Проверить сортировку товаров по имени и стоимости в обоих направлениях
 * */
public class SortTest extends BaseTest {

    @Test
    @AllureId("TC-01")
    @Tags({@Tag("Positive"), @Tag("Sort")})
    @DisplayName("Сортировка товаров по имени A-Z (возрастание)")
    void sortByNameAscendingTest() {
        HomePage homePage = new HomePage(driver,wait);
        CategoryPage  categoryPage = homePage.selectRandomCategory(4);
        categoryPage.sortBy(SortType.NAME_A_Z);
        Assertions.assertTrue(categoryPage.isSortedByNameAscending(),
                "Товары не отсортированы по имени A-Z");
    }

    @Test
    @AllureId("TC-02")
    @Tags({@Tag("Positive"), @Tag("Sort")})
    @DisplayName("Сортировка товаров по имени Z-A(убывание)")
    void sortByNameDescendingTest() {
        HomePage homePage = new HomePage(driver,wait);
        CategoryPage categoryPage = homePage.selectRandomCategory(4);
        categoryPage.sortBy(SortType.NAME_Z_A);
        Assertions.assertTrue(categoryPage.isSortedByNameDescending(),
                "Товары не отсортированы по имени Z-A");
    }

    @Test
    @AllureId("TC-03")
    @Tags({@Tag("Positive"), @Tag("Sort")})
    @DisplayName("Сортировка товаров по цене Low > High (возрастание)")
    void SortByPriceAscendingTest() {
        HomePage homePage = new HomePage(driver, wait);
        CategoryPage categoryPage =  homePage.selectRandomCategory(4);
        categoryPage.sortBy(SortType.PRICE_LOW_HIGH);
        Assertions.assertTrue(categoryPage.isSortedByPriceAscending(),
                "Товары не отсортированы по цене (от низкой к высокой)");
    }

    @Test
    @AllureId("TC-04")
    @Tags({@Tag("Positive"), @Tag("Sort")})
    @DisplayName("Сортировка товаров по цене High > Low (убывание)")
    void SortByPriceDescendingTest() {
        HomePage homePage = new HomePage(driver, wait);
        CategoryPage categoryPage = homePage.selectRandomCategory(4);
        categoryPage.sortBy(SortType.PRICE_HIGH_LOW);
        Assertions.assertTrue(categoryPage.isSortedByPriceDescending(),
                "Товары не отсортированы по цене (от высокой к низкой)");
    }
}
