package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CategoryPage;
import pages.HomePage;
import utils.SortType;

public class FilterTest extends BaseTest {

    @Test
    @AllureId("TC-01")
    @Tag("Positive")
    @DisplayName("Сортировка товаров по имени A-Z (возрастание)")
    void sortByNameAscendingTest()  {
        HomePage homePage = new HomePage(driver,wait);

        // Выбираем случайную категорию с >= 4 товарами
        CategoryPage  categoryPage = homePage.selectRandomCategory();

        // Выполняем сортировку по имени A-Z
        categoryPage.sortBy(SortType.NAME_A_Z);

        // Проверяем сортировку
        Assertions.assertTrue(categoryPage.isSortedByNameAscending(),
                "Товары не отсортированы по имени A-Z");
    }

    @Test
    @AllureId("TC-02")
    @Tag("Positive")
    @DisplayName(" =Сортировка товаров по имени Z-A(убывание)")
    void sortByNameDescendingTest()  {
        HomePage homePage = new HomePage(driver,wait);

        // Выбираем случайную категорию с >= 4 товарами
        CategoryPage categoryPage = homePage.selectRandomCategory();

        // Выполняем сортировку по имени Z-A
        categoryPage.sortBy(SortType.NAME_Z_A);

        // проверяем сортировку
        Assertions.assertTrue(categoryPage.isSortedByNameDescending(),
                "Товары не отсортированы по имени Z-A");
    }

    @Test
    @AllureId("TC-03")
    @Tag("Positive")
    @DisplayName("Сортировка товаров по цене Low > High (возрастание)")
    void testSortByPriceAscending() {
        HomePage homePage = new HomePage(driver, wait);

        // Выбираем случайную категорию с >= 4 товарами
        CategoryPage categoryPage =  homePage.selectRandomCategory();

        // Выполняем сортировку по цене Low > High
        categoryPage.sortBy(SortType.PRICE_LOW_HIGH);

        // Проверяем сортировку
        Assertions.assertTrue(categoryPage.isSortedByPriceAscending(),
                "Товары не отсортированы по цене (от низкой к высокой)");
    }

    @Test
    @AllureId("TC-04")
    @Tag("Positive")
    @DisplayName("Сортировка товаров по цене High > Low (убывание)")
    void testSortByPriceDescending() {
        HomePage homePage = new HomePage(driver, wait);

        // Выбираем случайную категорию с >= 4 товарами
        CategoryPage categoryPage = homePage.selectRandomCategory();

        // Выполняем сортировку по цене High > Low
        categoryPage.sortBy(SortType.PRICE_HIGH_LOW);

        // Проверяем сортировку
        Assertions.assertTrue(categoryPage.isSortedByPriceDescending(),
                "Товары не отсортированы по цене (от высокой к низкой)");
    }
}
