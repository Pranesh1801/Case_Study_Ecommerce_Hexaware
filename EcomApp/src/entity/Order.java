package entity;
import java.util.Date;
public class Order {
    private int orderId;
    private int customerId;
    private Date orderDate;
    private double totalPrice;
    private String shippingAddress;

    public Order() { }

    public Order(int orderId, int customerId, Date orderDate, double totalPrice, String shippingAddress) {
        this.orderId         = orderId;
        this.customerId      = customerId;
        this.orderDate       = orderDate;
        this.totalPrice      = totalPrice;
        this.shippingAddress = shippingAddress;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int id) { this.orderId = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int id) { this.customerId = id; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date d) { this.orderDate = d; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double p) { this.totalPrice = p; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String addr) { this.shippingAddress = addr; }

    @Override
    public String toString() {
        return "Order[id=" + orderId +
               ", cust=" + customerId +
               ", date=" + orderDate +
               ", total=" + totalPrice +
               ", shipAddr=" + shippingAddress + "]";
    }
}