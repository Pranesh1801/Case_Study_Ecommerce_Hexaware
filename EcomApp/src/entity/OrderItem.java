package entity;
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;

    public OrderItem() { }

    public OrderItem(int orderItemId, int orderId, int productId, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId     = orderId;
        this.productId   = productId;
        this.quantity    = quantity;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int id) { this.orderItemId = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int id) { this.orderId = id; }

    public int getProductId() { return productId; }
    public void setProductId(int id) { this.productId = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { this.quantity = q; }

    @Override
    public String toString() {
        return "OrderItem[id=" + orderItemId +
               ", order=" + orderId +
               ", prod=" + productId +
               ", qty=" + quantity + "]";
    }
}