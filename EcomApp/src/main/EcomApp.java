package main;
import dao.OrderProcessorRepository;
import dao.OrderProcessorRepositoryImpl;
import entity.Admin;
import entity.Cart;
import entity.Customer;
import entity.OrderItem;
import entity.Product;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EcomApp {

    public static void main(String[] args) {
        OrderProcessorRepository repo = new OrderProcessorRepositoryImpl();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== E-Commerce Application ===");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Register New Customer");
            System.out.println("4. Register New Admin");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Username: ");
                    String adminUsername = sc.nextLine();
                    System.out.print("Password: ");
                    String adminPass = sc.nextLine();
                    Admin admin = repo.loginAdmin(adminUsername, adminPass);
                    if (admin != null) {
                        System.out.println("Admin logged in successfully.");
                        adminMenu(sc, repo);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter your Customer ID: ");
                    int customerId = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter your Password: ");
                    String password = sc.nextLine();

                    try {
                        Customer customer = repo.getCustomerById(customerId);
                        if (customer.getPassword().equals(password)) {
                            System.out.println("User logged in successfully.");
                            userMenu(sc, repo, customer);
                        } 
                        else {
                            System.out.println("Incorrect password.");
                        }
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                
                
                case 3 -> {
                    System.out.print("Name: ");
                    String n = sc.nextLine();
                    String e;
                    while (true) {
                        System.out.print("Email: ");
                        e = sc.nextLine();
                        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                        Pattern emailPattern = Pattern.compile(emailRegex);
                        Matcher emailMatcher = emailPattern.matcher(e);
                        if (!emailMatcher.matches()) {
                            System.out.println("Invalid email format. Please enter a valid email.");
                        } else {
                            break;
                        }
                    }
                    System.out.print("Password: ");
                    String p = sc.nextLine();
                    Customer newCust = new Customer(0, n, e, p);
                    int newId = repo.createCustomer(newCust);
                    if (newId > 0) {
                        System.out.println("Customer registered with ID: " + newId);
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                }
                case 4 -> {
                    System.out.print("Enter Admin Code: ");
                    String code = sc.nextLine();
                    if (code.equals("1112")) {
                        System.out.print("Choose Username: ");
                        String newAdminUser = sc.nextLine();
                        System.out.print("Set Password: ");
                        String newAdminPass = sc.nextLine();
                        boolean created = repo.registerAdmin(new Admin(0, newAdminUser, newAdminPass));
                        if (created) {
                            System.out.println("New admin registered successfully.");
                        } else {
                            System.out.println("Admin registration failed (username might already exist).");
                        }
                    } else {
                        System.out.println("Invalid Admin Code.");
                    }
                }
                
                case 5 -> {
                    System.out.println("Thank you for using us! Be back soon");
                    sc.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static void adminMenu(Scanner sc, OrderProcessorRepository repo) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View All Customers");
            System.out.println("2. Delete Customer");
            System.out.println("3. Create Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. View All Orders");
            System.out.println("7. Ship Orders");
            System.out.println("8. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    List<Customer> customers = repo.listAllCustomers();
                    customers.forEach(System.out::println);
                }
                
                case 2 -> {
                    System.out.print("Enter Customer ID to delete: ");
                    int customerIdToDelete = Integer.parseInt(sc.nextLine());
                    try {
                        repo.deleteCustomer(customerIdToDelete);
                        System.out.println("Customer deleted successfully.");
                    } catch (CustomerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Product Name: ");
                    String productName = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Enter Description: ");
                    String description = sc.nextLine();
                    System.out.print("Enter Stock Quantity: ");
                    int stockQuantity = Integer.parseInt(sc.nextLine());
                    if (repo.createProduct(new Product(0, productName, price, description, stockQuantity))) {
                        System.out.println("Product created.");
                    } 
                    
                    else {
                        System.out.println("Failed to create product.");
                    }
                }
                
                
                case 4 -> {
                    System.out.print("Enter Product ID to update: ");
                    int productIdToUpdate = Integer.parseInt(sc.nextLine());
                    Product productToUpdate = repo.listAllProducts().stream()
                            .filter(p -> p.getProductId() == productIdToUpdate)
                            .findFirst()
                            .orElse(null);
                    if (productToUpdate != null) {
                        System.out.print("Enter New Name: ");
                        productToUpdate.setName(sc.nextLine());
                        System.out.print("Enter New Price: ");
                        productToUpdate.setPrice(Double.parseDouble(sc.nextLine()));
                        System.out.print("Enter New Description: ");
                        productToUpdate.setDescription(sc.nextLine());
                        System.out.print("Enter New Stock Quantity: ");
                        productToUpdate.setStockQuantity(Integer.parseInt(sc.nextLine()));
                        if (repo.updateProduct(productToUpdate)) {
                            System.out.println("Product updated.");
                        } else {
                            System.out.println("Failed to update product.");
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                }
                case 5 -> {
                    System.out.print("Enter Product ID to delete: ");
                    int productIdToDelete = Integer.parseInt(sc.nextLine());
                    try {
                        repo.deleteProduct(productIdToDelete);
                        System.out.println("Product deleted.");
                    } catch (ProductNotFoundException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
                case 6 -> {
                    List<OrderItem> allOrders = repo.listAllOrders();
                    allOrders.forEach(System.out::println);
                }
                case 7 -> {
                    System.out.print("Enter Order ID to ship: ");
                    int orderId = Integer.parseInt(sc.nextLine());
                    shipOrders(repo, orderId);
                }
                case 8 -> {
                    System.out.println("Admin has logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static void userMenu(Scanner sc, OrderProcessorRepository repo, Customer customer) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Update Customer");
            System.out.println("2. List Products");
            System.out.println("3. Add to Cart");
            System.out.println("4. Remove from Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Customer ID to update: ");
                    int uid = Integer.parseInt(sc.nextLine());
                    try {
                        Customer uc = repo.getCustomerById(uid);
                        System.out.print("New name (" + uc.getName() + "): ");
                        uc.setName(sc.nextLine());
                        System.out.print("New email (" + uc.getEmail() + "): ");
                        uc.setEmail(sc.nextLine());
                        System.out.print("New password: ");
                        uc.setPassword(sc.nextLine());
                        if (repo.updateCustomer(uc)) {
                            System.out.println("Customer updated.");
                        } else {
                            System.out.println("Failed to update customer.");
                        }
                    } catch (CustomerNotFoundException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
                case 2 -> {
                    List<Product> products = repo.listAllProducts();
                    products.forEach(System.out::println);
                }
                case 3 -> {
                    System.out.print("Enter Product ID: ");
                    int productId = Integer.parseInt(sc.nextLine());
                    Product productToAdd = repo.listAllProducts().stream()
                            .filter(p -> p.getProductId() == productId)
                            .findFirst()
                            .orElse(null);
                    if (productToAdd != null) {
                        System.out.print("Enter Quantity: ");
                        int quantity = Integer.parseInt(sc.nextLine());
                        repo.addToCart(customer, productToAdd, quantity);
                        System.out.println("Product added to cart.");
                    } else {
                        System.out.println("Product not found.");
                    }
                }
                
                
                
                case 4 -> {
                    System.out.print("Enter Product ID to remove from cart: ");
                    int productIdToRemove = Integer.parseInt(sc.nextLine());
                    Product productToRemove = repo.listAllProducts().stream()
                            .filter(p -> p.getProductId() == productIdToRemove)
                            .findFirst()
                            .orElse(null);
                    if (productToRemove != null) {
                        try {
                            repo.removeFromCart(customer, productToRemove);
                            System.out.println("Product removed from cart.");
                        } catch (ProductNotFoundException e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                }
                case 5 -> {
                    List<Cart> cartItems = repo.getAllFromCart(customer);
                    if (cartItems.isEmpty()) {
                        System.out.println("Cart is empty.");
                    } else {
                        cartItems.forEach(System.out::println);
                    }
                }
                case 6 -> System.out.println("Proceeding to checkout...");
                case 7 -> {
                    System.out.println("User logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }
    public static void shipOrders(OrderProcessorRepository repo, int orderId) {
        try {
            List<OrderItem> orderItems = repo.getOrdersByCustomer(orderId);
            for (OrderItem item : orderItems) {
                Product product = repo.listAllProducts().stream()
                        .filter(p -> p.getProductId() == item.getProductId())
                        .findFirst()
                        .orElse(null);
                if (product != null) {
                    int updatedStock = product.getStockQuantity() - item.getQuantity();
                    if (updatedStock >= 0) {
                        product.setStockQuantity(updatedStock);
                        repo.updateProduct(product);
                        System.out.println("Order shipped for Product ID: " + product.getProductId());
                    } else {
                        System.out.println("Not enough stock to ship Product ID: " + product.getProductId());
                    }
                }
            }
        } catch (OrderNotFoundException e) {
            System.out.println("Order not found.");
        }
    }
}