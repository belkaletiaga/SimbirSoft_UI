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

/**
 * Используется для проверки поисковой выдачи и добавления товаров в корзину.
 */
public class SearchResultsPage extends BaseProductPage {

    private static final By PRICE_NEW_SELECTOR = By.cssSelector(".pricenew");
    private static final By PRICE_REGULAR_SELECTOR = By.cssSelector(".oneprice");

    @FindBy(id = "sort")
    private WebElement sortSelect;

    public SearchResultsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Выполнить сортировку: {sortType}")
    public void selectSortOption(SortType sortType) {
        WaitHelper.waitForVisibleAndClickable(wait, sortSelect);
        Select select = new Select(sortSelect);
        select.selectByValue(sortType.getValue());
        WaitHelper.waitForListVisible(wait, productCards);
    }

    /**
     * Открыть страницу товара по индексу
     */
    public ProductPage openProduct(int index) {
        WebElement card = productCards.get(index);
        card.findElement(By.cssSelector(".prdocutname")).click();
        return new ProductPage(driver, wait);
    }
}









