package entity;
public class Cart {
    private int cartId;
    private int customerId;
    private int productId;
    private int quantity;

    public Cart() { }

    public Cart(int cartId, int customerId, int productId, int quantity) {
        this.cartId     = cartId;
        this.customerId = customerId;
        this.productId  = productId;
        this.quantity   = quantity;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int id) { this.cartId = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int id) { this.customerId = id; }

    public int getProductId() { return productId; }
    public void setProductId(int id) { this.productId = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }

    @Override
    public String toString() {
        return "Cart[id=" + cartId +
               ", cust=" + customerId +
               ", prod=" + productId +
               ", qty=" + quantity + "]";
    }
}