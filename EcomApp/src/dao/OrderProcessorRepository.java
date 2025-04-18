package dao;
import entity.Cart;
import entity.Customer;
import entity.OrderItem;
import entity.Product;
import entity.Admin;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import java.util.List;
public interface OrderProcessorRepository {
    
	int createCustomer(Customer c);
    boolean updateCustomer(Customer c) throws CustomerNotFoundException;
    Customer getCustomerById(int id)  throws CustomerNotFoundException;
    List<Customer> listAllCustomers();
    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;

    boolean registerAdmin(Admin admin);
    Admin loginAdmin(String username, String password);
    boolean createProduct(Product p);
    List<Product> listAllProducts();
    boolean updateProduct(Product p);
    boolean deleteProduct(int id)     throws ProductNotFoundException;

    boolean addToCart(Customer c, Product p, int qty);
    boolean removeFromCart(Customer c, Product p) throws ProductNotFoundException;
    List<Cart> getAllFromCart(Customer c);
    List<OrderItem> listAllOrders();

    boolean placeOrder(Customer c, List<Cart> cartItems, String shippingAddress);
    List<OrderItem> getOrdersByCustomer(int customerId) throws OrderNotFoundException;
}