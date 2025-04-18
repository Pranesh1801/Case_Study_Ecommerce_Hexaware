CREATE DATABASE ecommerce;
USE ecommerce;

CREATE TABLE customers (
  customer_id    INT AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(100) NOT NULL,
  email          VARCHAR(150) NOT NULL UNIQUE,
  password       VARCHAR(100) NOT NULL
);

CREATE TABLE products (
  product_id     INT AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(150) NOT NULL,
  price          DOUBLE NOT NULL,
  description    VARCHAR(500),
  stock_quantity INT NOT NULL
);

CREATE TABLE cart (
  cart_id       INT AUTO_INCREMENT PRIMARY KEY,
  customer_id   INT NOT NULL,
  product_id    INT NOT NULL,
  quantity      INT NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  FOREIGN KEY (product_id)  REFERENCES products(product_id)
);

CREATE TABLE orders (
  order_id         INT AUTO_INCREMENT PRIMARY KEY,
  customer_id      INT NOT NULL,
  order_date       DATETIME NOT NULL,
  total_price      DOUBLE NOT NULL,
  shipping_address VARCHAR(300) NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id      INT NOT NULL,
  product_id    INT NOT NULL,
  quantity      INT NOT NULL,
  FOREIGN KEY (order_id)   REFERENCES orders(order_id),
  FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO customers (name, email, password) VALUES
  ('Alice Johnson',        'alice.johnson@example.com',       'alicePass1'),
  ('Bob Kumar',            'bob.kumar@example.com',           'bobPass2'),
  ('Carla Singh',          'carla.singh@example.com',         'carlaPass3'),
  ('David Patel',          'david.patel@example.com',         'davidPass4'),
  ('Eve Reddy',            'eve.reddy@example.com',           'evePass5'),
  ('Franklin George',      'frank.george@example.com',        'frankPass6'),
  ('Grace Nair',           'grace.nair@example.com',          'gracePass7'),
  ('Harish Menon',         'harish.menon@example.com',        'harishPass8'),
  ('Isha Varma',           'isha.varma@example.com',          'ishaPass9'),
  ('Jayant Rao',           'jayant.rao@example.com',          'jayantPass10');

INSERT INTO products (name, price, description, stock_quantity) VALUES
  ('Wireless Mouse',           19.99,   'Ergonomic 2.4GHz wireless mouse',       50),
  ('Mechanical Keyboard',      79.50,   'RGB backlit, blue switches',            30),
  ('27\" 4K Monitor',         299.00,   'IPS panel, HDR support',                20),
  ('USB-C Hub',               34.25,   '5-port USB-C to USB-A + HDMI + LAN',    40),
  ('Noise-Cancelling Headphones',129.99, 'Over-ear, Bluetooth 5.0',               25),
  ('Webcam 1080p',            49.99,   'Auto-focus, built‑in mic',              35),
  ('Gaming Chair',           149.00,   'Ergonomic swivel chair',                15),
  ('External SSD 1TB',        119.95,  'USB-C portable SSD, 1 TB',              60),
  ('Bluetooth Speaker',        59.90,  'Waterproof, 10 hr battery life',        45),
  ('Smartphone Stand',         12.49,  'Adjustable aluminum desk mount',        80),
  ('Chocolate Chip Cookies',  3.49, '200g pack of chocolate chip cookies',      100),
  ('Salted Potato Chips',     2.99, '150g bag of classic salted potato chips',         120),
  ('Oatmeal Raisin Cookies',  3.99, '200g pack of oatmeal raisin cookies',            80),
  ('Mixed Nuts Trail Mix',     5.49, '200g pack of mixed nuts & dried fruits',          50),
  ('Granola Bars (Box of 6)',  4.99, 'Assorted flavors, 6 bars per box',               70),
  ('Butter Microwave Popcorn', 1.99, '100g microwave butter popcorn',                  150),
  ('Cheddar Cheese Crackers',  2.49, '100g pack of cheddar cheese crackers',          90),
  ('Dried Mango Slices',       4.25, '100g pack of sweet dried mango slices',          60),
  ('Pretzel Sticks',           2.75, '150g bag of crunchy pretzel sticks',             110),
  ('Mixed Fruit Gummies',      3.29, '150g bag of assorted fruit gummy candies',       95);

CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

select * from products;
select * from admin;
select * from customers;
