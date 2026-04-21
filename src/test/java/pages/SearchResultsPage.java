package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.SortType;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Используется для проверки поисковой выдачи и добавления товаров в корзину.
 */

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // Выпадающий список сортировки
    @FindBy(id = "sort")
    private WebElement sortDropdown;

    // Названия товаров в результатах поиска
    @FindBy(css = ".fixed_wrapper .prdocutname")
    private List<WebElement> productNames;

    // Список цен товаров (обычная цена)
    @FindBy(css = ".jumbotron .oneprice")
    private List<WebElement> priceElements;

    // Новая цена (со скидкой)
    @FindBy(css = ".jumbotron .pricenew")
    private List<WebElement> productPricesNew;

    // Кнопки добавления в корзину
    @FindBy(css = ".jumbotron .productcart")
    private List<WebElement> addToCartButtons;

    // Карточки товаров
    @FindBy(css = ".thumbnails .col-md-3")
    private List<WebElement> productCards;

    /**
     * Выбрать вариант сортировки результатов
     */
    public void selectSortOption(SortType sortType) {
        WaitHelper.waitForVisibleAndClickable(wait, sortDropdown);
        Select select = new Select(sortDropdown);
        select.selectByValue(sortType.getValue());

        // Ожидание применения сортировки
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Получить список товаров из результатов поиска
     * список товаров с названиями и ценами (учитывает как обычную цену, так и цену со скидкой)
     */
    public List<ProductItem> getProducts() {
        List<ProductItem> products = new ArrayList<>();
        for (WebElement card : productCards) {
            try {
                // Получаем название товара
                String name = card.findElement(By.cssSelector(".fixed_wrapper .prdocutname")).getText();

                // Получаем цену
                double price = extractPrice(card);
                // Получаем ID товара
                WebElement addButton = card.findElement(By.cssSelector(".jumbotron .productcart"));
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

    // Добавление товара с указанным количеством (через страницу товара)
    public ProductPage openProduct(int index) {
        WebElement card = productCards.get(index);
        card.findElement(By.cssSelector(".prdocutname")).click();
        return new ProductPage(driver, wait);
    }
}









