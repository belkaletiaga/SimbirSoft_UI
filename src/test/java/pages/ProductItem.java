package pages;

/**
 * Класс для хранения информации о товаре: id, название, цена.
 */

public class ProductItem {

    private String id;
    private final String name;
    private final double price;

    public ProductItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public double getPrice() { return price; }

}