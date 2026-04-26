package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Класс для работы с ценами на страницах.
 */
public class PriceUtils {

    private static final By PRICE_NEW_SELECTOR = By.cssSelector(".pricenew");
    private static final By PRICE_REGULAR_SELECTOR = By.cssSelector(".oneprice");

    /**
     * Извлекает цену из карточки товара (учитывает обычную цену или цену со скидкой).
     */
    public static double extractPrice(WebElement card) {
        try {
            WebElement priceNew = card.findElement(PRICE_NEW_SELECTOR);
            return parsePrice(priceNew.getText());
        } catch (Exception e) {
            WebElement priceRegular = card.findElement(PRICE_REGULAR_SELECTOR);
            return parsePrice(priceRegular.getText());
        }
    }

    /**
     * Парсит строку с ценой (удаляет символы валюты, преобразует запятую в точку).
     */
    public static double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");
        return Double.parseDouble(numeric);
    }
}
