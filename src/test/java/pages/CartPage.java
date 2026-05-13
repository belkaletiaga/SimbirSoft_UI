package pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PriceUtils;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с товаром в корзине
 */
public class CartPage extends BasePage{

    private static final By NAME_SELECTOR = By.cssSelector("td:nth-child(2) a");
    private static final By PRICE_SELECTOR = By.cssSelector("td:nth-child(4)");
    private static final By QUANTITY_INPUT_SELECTOR = By.cssSelector(".input-group-sm .form-control");
    private static final By TOTAL_SELECTOR = By.cssSelector("td:nth-child(6)");
    private static final By REMOVE_BUTTON_SELECTOR = By.cssSelector(".btn-sm");

    @FindBy(xpath = "//*[@id=\"cart\"]/div/div[1]/table/tbody/tr[position() > 1]")
    private List<WebElement> cartRows;

    @FindBy(xpath = "//*[@id=\"totals_table\"]//tr[1]/td[2]/span")
    private WebElement subTotalAmound;

    @FindBy(css = ".mb20 .btn-default")
    private WebElement updateBtn;

    @FindBy(css = ".maintext")
    private WebElement shoppingCartMessage;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickUpdateButton(){
        scrollToElement(updateBtn);
        updateBtn.click();
        WaitHelper.waitForVisible(wait, subTotalAmound);
    }

    /**
     * Получает список товаров в корзине.
     */
    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (WebElement row : cartRows) {
            String name = row.findElement(NAME_SELECTOR).getText();
            double price = PriceUtils.parsePrice(row.findElement(PRICE_SELECTOR).getText());
            int quantity = Integer.parseInt(row.findElement(QUANTITY_INPUT_SELECTOR ).getAttribute("value"));
            double total = PriceUtils.parsePrice(row.findElement(TOTAL_SELECTOR).getText());
            WebElement removeBtn = row.findElement(REMOVE_BUTTON_SELECTOR);
            WebElement qtyInput = row.findElement(QUANTITY_INPUT_SELECTOR);
            items.add(new CartItem(name, price, quantity, total, removeBtn, qtyInput));
        }
        return items;
    }

    public double getTotalPrice() {
        WaitHelper.waitForVisible(wait, subTotalAmound);
        String text = subTotalAmound.getText();
        return PriceUtils.parsePrice(text);
    }

    public void updateQuantity(CartItem item, int newQuantity) {
        WebElement qtyInput1 = item.getQtyInput();
        qtyInput1.clear();
        qtyInput1.sendKeys(String.valueOf(newQuantity));
        clickUpdateButton();
        WaitHelper.waitForVisible(wait, shoppingCartMessage);
    }

    @Step("В таблице товаров удалить все четные по порядку товары (2-й, 4-й)")
    public void removeEvenItems() {
        List<CartItem> items = getCartItems();
        List<Integer> indexesToRemove = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if ((i + 1) % 2 == 0) {
                indexesToRemove.add(i);
            }
        }
        for (int j = indexesToRemove.size() - 1; j >= 0; j--) {
            int index = indexesToRemove.get(j);
            CartItem item = getCartItems().get(index);
            removeItem(item);
        }
    }

    private void removeItem(CartItem item) {
        item.getRemoveButton().click();
        wait.until(ExpectedConditions.invisibilityOf(item.getRemoveButton()));
    }

    @Step("Найти самый дешевый товар в корзине")
    public CartItem cheapestItemQuantity() {
        List<CartItem> items = getCartItems();
        CartItem cheapest = items.stream()
                .min((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .orElseThrow(() -> new RuntimeException("Корзина пуста"));
        Allure.step("Самый дешевый товар: " + cheapest.getName() + " (с ценой: " + cheapest.getPrice() + ")");
        return cheapest;
    }

    @Step("Удвоить количество самого дешевого товара")
    public void doubleCheapestItemQuantity(CartItem cheapest) {
        int oldQty = cheapest.getQuantity();
        updateQuantity(cheapest, oldQty * 2);
    }

    @Step("Проверить итоговую сумму корзины ")
    public double calculateExpectedTotal() {
        List<CartPage.CartItem> itemNew = getCartItems();
        double expectedTotal = 0.0;
        for (CartPage.CartItem item : itemNew) {
            expectedTotal += item.getPrice() * item.getQuantity();
        }
        return expectedTotal;
    }

    public static class CartItem {

        private final String name;
        private final double price;
        private final int quantity;
        private final double total;
        private final WebElement removeButton;
        private final WebElement qtyInput;

        public CartItem(String name, double price, int quantity, double total,
                        WebElement removeBtn, WebElement qtyInput) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.total = total;
            this.removeButton = removeBtn;
            this.qtyInput = qtyInput;
        }

        public String getName() { return name; }

        public double getPrice() { return price; }

        public int getQuantity() { return quantity; }

        public double getTotal() { return total; }

        public WebElement getRemoveButton() { return removeButton;}

        public WebElement getQtyInput() { return qtyInput;}
    }
}
