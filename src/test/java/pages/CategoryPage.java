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
 * Страницы категорий товаров
 * Используется для тестирования сортировки товаров
 */
public class CategoryPage extends BasePage {
    HomePage homePage;

    public CategoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // Заголовок категории
    @FindBy(xpath = "//div/div/div/h1/span[1]")
    public WebElement categoryTitle;

    // Поле для выбора сортировки
    @FindBy(id = "sort")
    private WebElement sortSelect;

    // Выпадающий список вариантов список сортировки
    @FindBy(xpath = "//*[@id=\"sort\"]/option[position() > 0]")
    private List<WebElement> sortsSelect;

    // Товары на странице
    @FindBy(css = ".col-md-3 .thumbnail")
    private List<WebElement> productCards;

    // Названия товаров
    @FindBy(css = ".fixed .prdocutname")
    private List<WebElement> productNames;

    // Список цен товаров (обычная цена)
    @FindBy(css = ".jumbotron .oneprice")
    private List<WebElement> regularPrices;

    // Новая цена (со скидкой)
    @FindBy(css = ".jumbotron .pricenew")
    private List<WebElement> productPricesNew;

    // Все элементы с ценами (объединяет oneprice и pricenew)
    @FindBy(css = ".jumbotron .price")
    private List<WebElement> priceContainers;

    // Кнопки добавления в корзину
    @FindBy(css = ".jumbotron .productcart")
    private List<WebElement> addToCartButtons;

    /**
     * Получение названия текущей категории
     */
    public String getCategoryName() {
        return categoryTitle.getText();
    }

    /**
     * Получение количества товаров на странице
     */
    @Step("Получение количества товаров на странице")
    public int getProductsCount() {
        return productCards.size();
    }

    /**
     * Получение списка товаров в категории
     * список товаров с названиями и ценами (учитывает как обычную цену, так и цену со скидкой)
     */
    public List<ProductItem> getProducts() {
        List<ProductItem> products = new ArrayList<>();
        for (WebElement card : productCards) {
            try {
                // Получаем название товара
                String name = card.findElement(By.cssSelector(".fixed .prdocutname")).getText();

                // Получаем цену
                double price = extractPrice(card);

                // Получаем ID товара
                WebElement addButton = card.findElement((By) addToCartButtons);
                String productId = addButton.getAttribute("data-id");

                products.add(new ProductItem(productId, name, price));
            } catch (Exception e) {
                // Пропускаем товары без цены или с ошибкой
            }
        }
        return products;
    }

    /**
     * Получение списка ценн (учитывается как обычная цену, так и цена со скидкой)
     */
    private double extractPrice(WebElement card) {
        try {
            WebElement priceNew = card.findElement(By.cssSelector(".pricenew"));
            return parsePrice(priceNew.getText());
        } catch (Exception e) {
            WebElement priceRegular = card.findElement(By.cssSelector(".oneprice"));
            return parsePrice(priceRegular.getText());
        }
    }
    private double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");
        return Double.parseDouble(numeric);
    }

    /**
     * Сортировка товаров по указанному критерию
     */
    @Step("Сортировка: {sortType}")
    public void sortBy(SortType sortType) {
        WaitHelper.waitForVisibleAndClickable(wait, sortSelect);
        Select select = new Select(sortSelect);
        select.selectByValue(sortType.getValue());

        // Ожидание применения сортировки
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
