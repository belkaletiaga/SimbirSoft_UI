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
import java.util.Locale;

/**
 * Главная страница
 * Содержит методы для навигации по категориям, поиска, добавления товаров в корзину.
 */
public class HomePage extends BaseProductPage {

    private static final By SEARCH_STRING_SELECTOR = By.id("filter_keyword");
    private static final By BUTTON_IN_SEARCH_SELECTOR = By.cssSelector(".button-in-search");

    @FindBy(id = "categorymenu")
    private WebElement categoryMenu;

    @FindBy(xpath = "//nav/ul/li[position() > 1]")
    public List<WebElement> categories;

    @FindBy(css = ".block_6 .dropdown-toggle")
    private WebElement currencyMenu;

    @FindBy(xpath = "//header/div[2]/div/div[2]/ul/li/ul/li")
    private List<WebElement> currencyDropdownMenu;

    @FindBy(css = ".block_6 .label")
    private WebElement currencyEstablished;

    @FindBy(css = ".maintext")
    private WebElement textTitle;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String currencyEstablished(){
        return currencyEstablished.getText();
    }

    @Step("Выбрать любую категорию с количеством товаров >= {count}")
    public CategoryPage selectRandomCategory(int count) {
        WaitHelper.waitForVisibleAndClickable(wait,categoryMenu);
        List<String> categoriesName = new ArrayList<>();
        for (WebElement element : categories) {
            categoriesName.add(element.getText());
        }
        while (!categoriesName.isEmpty()) {
            String randomName  = RandomUtils.selectRandomString(categoriesName);
            String xpath = String.format("//nav/ul/li/a[contains(translate(normalize-space(), "
                    + "'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), '"+ randomName.trim() +"')]");
            WebElement randomElement = driver.findElement(By.xpath(xpath));
            randomElement.click();

            CategoryPage categoryPage = new CategoryPage(driver, wait);
            WaitHelper.waitForVisible(wait, categoryPage.categoryTitle);
            String categoryName = categoryPage.categoryTitle.getText();
            int productsCount = categoryPage.getProductsCount();
            if (productsCount >= count) {
                Allure.step("Выбрана категория: " + categoryName + " (товаров: " + productsCount + ")");
                return categoryPage;
            }else {
                Allure.step("Категория '" + categoryName + "' содержит менее 4 товаров, пробуем другую");
                categoriesName.remove(randomName);
            }
            driver.navigate().back();
            WaitHelper.waitForVisibleAndClickable(wait, categoryMenu);
        }
        throw new RuntimeException("Не найдено категорий с количеством товаров не менее 4");
    }

    @Step("Ввести в поиск {keyword} и выполнить поиск")
    public void searchFor(String keyword) {
        WebElement searchInput = driver.findElement(SEARCH_STRING_SELECTOR );
        searchInput.clear();
        searchInput.sendKeys(keyword);
        driver.findElement(BUTTON_IN_SEARCH_SELECTOR).click();
    }

    @Step("Выбрать {count} случайных товаров с рандомным количеством (1-5)")
    public void addRandomProductsToCart(int count) {
        List<ProductItem> allProducts = getProducts();
        if (allProducts.size() < count) {
            throw new RuntimeException("Недостаточно товаров на главной странице");
        }
        List<ProductItem> selected = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ProductItem randomProduct;
            do {
                randomProduct = allProducts.get(RandomUtils.getRandomInt(0, allProducts.size() - 1));
            } while (selected.contains(randomProduct));
            selected.add(randomProduct);
        }
        for (ProductItem product : selected) {
            int quantity = RandomUtils.getRandomInt(1, 5);
            driver.get("https://automationteststore.com/index.php?rt=product/product&product_id="
                    + product.getId());
            ProductPage productPage = new ProductPage(driver, wait);
            productPage.setQuantity(quantity);
            productPage.addToCart();
            WaitHelper.waitForTextToBePresent(wait, textTitle, "SHOPPING CART");
        }
    }
}
