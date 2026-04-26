package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SortType;
import utils.WaitHelper;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Страница категории товаров
 * Предоставляет методы для сортировки и проверки порядка товаров.И
 */
public class CategoryPage extends BaseProductPage {

    @FindBy(xpath = "//div/div/div/h1/span[1]")
    public WebElement categoryTitle;

    @FindBy(id = "sort")
    private WebElement sortSelect;

    @FindBy(xpath = "//*[@id=\"sort\"]/option[position() > 0]")
    private List<WebElement> sortsSelect;

    public String getCategoryName() {
        return categoryTitle.getText();
    }

    public int getProductsCount() {
        return productCards.size();
    }

    public CategoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Выполнить сортировку: {sortType}")
    public void sortBy(SortType sortType) {
        WaitHelper.waitForVisibleAndClickable(wait, sortSelect);
        Select select = new Select(sortSelect);
        select.selectByValue(sortType.getValue());
        WaitHelper.waitForListVisible(wait, productCards);
    }

    @Step("Проверка сортировки товаров по имени (возрастание)")
    public boolean isSortedByNameAscending() {
        List<ProductItem> products = getProducts();
        List<String> names = products.stream()
                .map(ProductItem::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(String::compareToIgnoreCase);
        return names.equals(sortedNames);
    }

    @Step("Проверка сортировки товаров по имени Z-A")
    public boolean isSortedByNameDescending() {
        List<ProductItem> products = getProducts();
        List<String> names = products.stream()
                .map(ProductItem::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(String::compareTo);
        java.util.Collections.reverse(sortedNames);
        return names.equals(sortedNames);
    }

    @Step("Проверка сортировки товаров по цене Low > High")
    public boolean isSortedByPriceAscending() {
        List<ProductItem> products = getProducts();
        List<Double> prices = products.stream()
                .map(ProductItem::getPrice)
                .collect(Collectors.toList());
        List<Double> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Double::compareTo);
        return prices.equals(sortedPrices);
    }

    @Step("Проверка сортировки товаров по цене High > Low")
    public boolean isSortedByPriceDescending() {
        List<ProductItem> products = getProducts();
        List<Double> prices = products.stream()
                .map(ProductItem::getPrice)
                .collect(Collectors.toList());
        List<Double> sortedPrices = new ArrayList<>(prices);
        sortedPrices.sort(Double::compareTo);
        java.util.Collections.reverse(sortedPrices);
        return prices.equals(sortedPrices);
    }
}
