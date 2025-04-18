package entity;
public class Product {
    private int productId;
    private String name;
    private double price;
    private String description;
    private int stockQuantity;

    public Product() { }

    public Product(int productId, String name, double price, String description, int stockQuantity) {
        this.productId     = productId;
        this.name          = name;
        this.price         = price;
        this.description   = description;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() { return productId; }
    public void setProductId(int id) { this.productId = id; }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }

    public double getPrice() { return price; }
    public void setPrice(double p) { this.price = p; }

    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int q) { this.stockQuantity = q; }

    @Override
    public String toString() {
        return "Product[id=" + productId + ", name=" + name +
               ", price=" + price + ", stock=" + stockQuantity + "]";
    }
}