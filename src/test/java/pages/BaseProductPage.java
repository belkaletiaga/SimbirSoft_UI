package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PriceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Объединяет общую логику для главной страницы, категорий и результатов поиска.
 */
public class BaseProductPage extends BasePage{

    @FindBy(css = ".thumbnails .col-md-3")
    protected List<WebElement> productCards;

    @FindBy(css = ".fixed_wrapper .prdocutname")
    protected List<WebElement> productNames;

    @FindBy(css = ".jumbotron .productcart")
    protected List<WebElement> addToCartButtons;

    public BaseProductPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Возвращает список всех товаров на странице.
     */
    public List<ProductItem> getProducts() {
        List<ProductItem> products = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".col-md-3 .thumbnail")));
        for (int i = 0; i < productCards.size(); i++) {
            try {
                String name = productNames.get(i).getText();
                double price = PriceUtils.extractPrice(productCards.get(i));
                WebElement addButton = addToCartButtons.get(i);
                String productId = addButton.getAttribute("data-id");
                products.add(new ProductItem(productId, name, price));
            } catch (Exception e) {
                // Пропускаем товары без цены или с ошибкой
            }
        }
        return products;
    }

    public int getProductsCount() {
        return productCards.size();
    }

    public List<WebElement> getProductCards() {
        return productCards;
    }

    public List<WebElement> getAddToCartButtons() {
        return addToCartButtons;
    }


}
