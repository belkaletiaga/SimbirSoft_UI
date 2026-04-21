package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitHelper;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage{
    @FindBy(xpath = "//*[@id=\"cart\"]/div/div[1]/table/tbody/tr[position() > 1]")
    private List<WebElement> cartRows;

    @FindBy(xpath = "//*[@id=\"totals_table\"]//tr[1]/td[2]/span")
    private WebElement subTotalAmoun;

    @FindBy(css = ".block_7 .dropdown-toggle")
    private WebElement basketLink;

    @FindBy(css = ".mb20 .btn-default")
    private WebElement updateBtn;

    @FindBy(css = ".maintext")
    private WebElement shoppingCartMessage;

    public CartPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public void getUpdateButton(){
        scrollToElement(updateBtn);
        updateBtn.click();
        WaitHelper.waitForVisible(wait, subTotalAmoun);
    }
    public void openCart() {
        scrollToElement(basketLink);
        basketLink.click();
        WaitHelper.waitForVisible(wait, subTotalAmoun);
    }

    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        for (WebElement row : cartRows) {

            String name = row.findElement(By.cssSelector("td:nth-child(2) a")).getText();

            double price = parsePrice(row.findElement(By.cssSelector("td:nth-child(4)")).getText());

            int quantity = Integer.parseInt(row.findElement(By.cssSelector(".input-group-sm .form-control")).getAttribute("value"));

            double total = parsePrice(row.findElement(By.cssSelector("td:nth-child(6)")).getText());

            WebElement removeBtn = row.findElement(By.cssSelector(".btn-sm"));
            WebElement qtyInput = row.findElement(By.cssSelector(".input-group-sm .form-control"));
            items.add(new CartItem(name, price, quantity, total, removeBtn, qtyInput));
        }
        return items;
    }

    public double getTotalPrice() {
        String text = subTotalAmoun.getText();
        return parsePrice(text);
    }

    public void updateQuantity(CartItem item, int newQuantity) {
        WebElement qtyInput1 = item.getQtyInput();
        qtyInput1.clear();
        qtyInput1.sendKeys(String.valueOf(newQuantity));
        getUpdateButton();
        WaitHelper.waitForVisible(wait, shoppingCartMessage);
    }

    public void removeItem(CartItem item) {
        item.getRemoveButton().click();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    private double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");
        return Double.parseDouble(numeric);
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
