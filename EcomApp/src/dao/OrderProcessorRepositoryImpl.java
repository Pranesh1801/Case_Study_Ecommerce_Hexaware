package dao;
import entity.Cart;
import entity.Customer;
import entity.OrderItem;
import entity.Product;
import entity.Admin;
//import util.DBConnection;
import exception.CustomerNotFoundException;
import exception.ProductNotFoundException;
import exception.OrderNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
    private Connection conn;

    public OrderProcessorRepositoryImpl() {
        try {
            String dbURL    = "jdbc:mysql://localhost:3306/ecommerce";
            String dbUser   = "root";
            String dbPass   = "Javasree2003#";
            this.conn       = DriverManager.getConnection(dbURL, dbUser, dbPass);
            System.out.println("Repository connected to database");
        } catch (SQLException ex) {
            System.err.println("Repository failed to connect to database:");
            ex.printStackTrace();
            throw new RuntimeException("Cannot initialize repository without a DB connection", ex);
        }
    }

    @Override
    public int createCustomer(Customer c) {
        // Check if the email already exists
        String checkEmailSql = "SELECT customer_id FROM customers WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkEmailSql)) {
            ps.setString(1, c.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("ERROR: This email is already registered.");
                return -1; // Return -1 if email is already taken
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        // If the email does not exist, proceed with the registration
        String sql = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPassword());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    @Override
    public boolean updateCustomer(Customer c) throws CustomerNotFoundException{
        getCustomerById(c.getCustomerId());
        String sql = "UPDATE customers SET name=?, email=?, password=? WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPassword());
            ps.setInt(4, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    @Override
    public List<Customer> listAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                  rs.getInt("customer_id"),
                  rs.getString("name"),
                  rs.getString("email"),
                  rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    

    // Admin registration
    @Override
    public boolean registerAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Admin loginAdmin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    return new Admin(rs.getInt("admin_id"), rs.getString("username"), storedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItem> listAllOrders() {
        List<OrderItem> orders = new ArrayList<>();
        String sql = "SELECT oi.* FROM order_items oi JOIN orders o ON oi.order_id=o.order_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {
        String checkSql = "SELECT customer_id FROM customers WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String deleteOrdersSql = "DELETE FROM orders WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteOrdersSql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String deleteCartSql = "DELETE FROM cart WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteCartSql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String deleteCustomerSql = "DELETE FROM customers WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteCustomerSql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Customer getCustomerById(int id) throws CustomerNotFoundException{
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new CustomerNotFoundException("Customer " + id + " not found");
            return new Customer(
                rs.getInt("customer_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
            );
        } catch (SQLException e) { e.printStackTrace(); }
        throw new CustomerNotFoundException("Customer " + id + " not found");
    }

    @Override
    public boolean createProduct(Product p) {
        String sql = "INSERT INTO products (name,price,description,stock_quantity) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setString(3, p.getDescription());
            ps.setInt(4, p.getStockQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Product> listAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                  rs.getInt("product_id"),
                  rs.getString("name"),
                  rs.getDouble("price"),
                  rs.getString("description"),
                  rs.getInt("stock_quantity")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, price=?, description=?, stock_quantity=? WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setString(3, p.getDescription());
            ps.setInt(4, p.getStockQuantity());
            ps.setInt(5, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean deleteProduct(int id) throws ProductNotFoundException{
        String check = "SELECT product_id FROM products WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, id);
            if (!ps.executeQuery().next())
                throw new ProductNotFoundException("Product " + id + " not found");
        } catch (SQLException e) { e.printStackTrace(); return false; }

        String sql = "DELETE FROM products WHERE product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean addToCart(Customer c, Product p, int qty) {
        String sql = "INSERT INTO cart (customer_id,product_id,quantity) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ps.setInt(2, p.getProductId());
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean removeFromCart(Customer c, Product p) throws ProductNotFoundException {
        String check = "SELECT * FROM cart WHERE customer_id=? AND product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, c.getCustomerId());
            ps.setInt(2, p.getProductId());
            if (!ps.executeQuery().next()) {
                // If the product is not in the cart, throw the exception
                throw new ProductNotFoundException("Item not in cart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "DELETE FROM cart WHERE customer_id=? AND product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ps.setInt(2, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Cart> getAllFromCart(Customer c) {
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Cart(
                  rs.getInt("cart_id"),
                  rs.getInt("customer_id"),
                  rs.getInt("product_id"),
                  rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public boolean placeOrder(Customer c, List<Cart> cartItems, String shippingAddress) {
        if (cartItems == null || cartItems.isEmpty()) return false;
        try {
            conn.setAutoCommit(false);
            double total = 0;
            // calculate total
            for (Cart item : cartItems) {
                String psql = "SELECT price FROM products WHERE product_id=?";
                try (PreparedStatement ps2 = conn.prepareStatement(psql)) {
                    ps2.setInt(1, item.getProductId());
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) total += rs.getDouble("price") * item.getQuantity();
                }
            }
            String osql = "INSERT INTO orders (customer_id,order_date,total_price,shipping_address) VALUES (?,?,?,?)";
            PreparedStatement ops = conn.prepareStatement(osql, Statement.RETURN_GENERATED_KEYS);
            ops.setInt(1, c.getCustomerId());
            ops.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ops.setDouble(3, total);
            ops.setString(4, shippingAddress);
            ops.executeUpdate();
            ResultSet gk = ops.getGeneratedKeys();
            gk.next();
            int orderId = gk.getInt(1);
            String iSql = "INSERT INTO order_items (order_id,product_id,quantity) VALUES (?,?,?)";
            for (Cart item : cartItems) {
                try (PreparedStatement ips = conn.prepareStatement(iSql)) {
                    ips.setInt(1, orderId);
                    ips.setInt(2, item.getProductId());
                    ips.setInt(3, item.getQuantity());
                    ips.executeUpdate();
                }
            }
            String csql = "DELETE FROM cart WHERE customer_id=?";
            try (PreparedStatement cps = conn.prepareStatement(csql)) {
                cps.setInt(1, c.getCustomerId());
                cps.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return false;
    }

    @Override
    public List<OrderItem> getOrdersByCustomer(int customerId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.* FROM order_items oi JOIN orders o ON oi.order_id=o.order_id WHERE o.customer_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                list.add(new OrderItem(
                  rs.getInt("order_item_id"),
                  rs.getInt("order_id"),
                  rs.getInt("product_id"),
                  rs.getInt("quantity")
                ));
            }
            if (!found) throw new OrderNotFoundException("No orders for customer " + customerId);
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}