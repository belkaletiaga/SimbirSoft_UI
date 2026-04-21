package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.RandomUtils;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Главная страница
 * Содержит методы для поиска и добавления товаров в корзину
 */
public class HomePage extends BasePage {
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // Главное меню категорий
    @FindBy(id = "categorymenu")
    private WebElement categoryMenu;

    // Поле выбора валюты
    @FindBy(css = ".block_6 .dropdown-toggle")
    private WebElement currencyMenu;

    // Варианты валюты
    @FindBy(xpath = "//header/div[2]/div/div[2]/ul/li/ul/li")
    private List<WebElement> currencyDropdownMenu;

    // Установленная валюта
    @FindBy(css = ".block_6 .label")
    private WebElement currencyEstablished;

    // Все категории в меню (кроме Home)
    @FindBy(xpath = "//nav/ul/li[position() > 1]")
    public List<WebElement> categories;

    // Все карточки товаров на главной странице
    @FindBy(css = ".thumbnails .col-md-3")
    private List<WebElement> allProductCards;

    // Строка поиска
    @FindBy(id = "filter_keyword")
    private WebElement searchString;

    // Кнопка поиска
    @FindBy(css = ".button-in-search")
    private WebElement buttonInSearch;

    // Названия товаров
    @FindBy(css = ".prdocutname")
    private List<WebElement> productNames;

    // Кнопки добавления в корзину
    @FindBy(css = ".jumbotron .productcart")
    private List<WebElement> addToCartButtons;



    public String currencyEstablished(){
        return currencyEstablished.getText();
    }

    @Step("Выбрать любую категорию товара, в которой не менее 4 товаров")
    public CategoryPage selectRandomCategory() {
        WaitHelper.waitForVisibleAndClickable(wait,categoryMenu);
        List<String> categoriesName = new ArrayList<>();
        for (WebElement element : categories) {
            categoriesName.add(element.getText());
        }

        // Ищем категорию с количеством товаров не менее 4
        while (!categoriesName.isEmpty()){
            String randomName  = RandomUtils.selectRandomString(categoriesName);
            String xpath = String.format("//nav/ul/li/a[contains(translate(normalize-space(), " +
                    "'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), '"+ randomName.trim() +"')]");
            WebElement randomElement = driver.findElement(By.xpath(xpath));
            randomElement.click();

            CategoryPage categoryPage = new CategoryPage(driver, wait);
            WaitHelper.waitForVisible(wait, categoryPage.categoryTitle);
            String categoryName = categoryPage.categoryTitle.getText();
            int productsCount = categoryPage.getProductsCount();
            if (productsCount >= 4) {

                Allure.step("Выбрана категория: " + categoryName + " (товаров: " + productsCount + ")");
                return categoryPage;
            }else {
                // Возвращаемся на главную страницу
                Allure.step("Категория '" + categoryName + "' содержит менее 4 товаров, пробуем другую");
                categoriesName.remove(randomName);
            }
            driver.navigate().back();
            WaitHelper.waitForVisibleAndClickable(wait, categoryMenu);
        }
        throw new RuntimeException("Не найдено категорий с количеством товаров не менее 4");
    }

    @Step("Получить список всех товаров на главной странице")
    public List<ProductItem> getAllProducts() {
        List<ProductItem> products = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".thumbnails .col-md-3")));
        for (WebElement card : allProductCards) {
            // Получаем название товара
            String name = card.findElement(By.cssSelector(" .fixed_wrapper .prdocutname")).getText();

            // Получаем цену
            double price = extractPrice(card);

            // Получаем ID товара
            WebElement addButton = card.findElement(By.cssSelector(".jumbotron .productcart"));
            String productId = addButton.getAttribute("data-id");

            products.add(new ProductItem(productId, name, price));
        }
        return products;
    }

    private double extractPrice(WebElement card) {
        try {
            WebElement priceNew = card.findElement(By.cssSelector(".pricenew"));
            String priceText = priceNew.getText();
            return parsePrice(priceText);
        } catch (Exception e) {
            WebElement priceRegular = card.findElement(By.cssSelector(".oneprice"));
            return parsePrice(priceRegular.getText());
        }
    }

    private double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");
        return Double.parseDouble(numeric);
    }

    public void searchFor(String keyword) {
        WebElement searchInput = driver.findElement(By.id("filter_keyword"));
        searchInput.clear();
        searchInput.sendKeys(keyword);
        driver.findElement(By.cssSelector(".button-in-search")).click();
    }

}
