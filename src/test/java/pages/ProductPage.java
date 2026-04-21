package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitHelper;

public class ProductPage extends BasePage {

    @FindBy(css = ".input-group .short")
    private WebElement quantityInput;

    @FindBy(css = ".productpagecart .cart ")
    private WebElement addToCartButton;

    @FindBy(css = ".maintext")
    private WebElement shoppingCartMessage;

    public ProductPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setQuantity(int quantity) {
        WaitHelper.waitForVisibleAndClickable(wait, quantityInput);
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    public void addToCart() {
        WaitHelper.waitForVisible(wait, addToCartButton);
        addToCartButton.click();
    }
}
