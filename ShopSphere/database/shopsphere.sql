-- =====================================================================
-- ShopSphere Database Schema & Seed Data
-- Target Database: MySQL 8.0+ / 9.x
-- =====================================================================

CREATE DATABASE IF NOT EXISTS shopsphere
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE shopsphere;

-- ---------------------------------------------------------------------
-- 1. Table: users
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS coupon_usage;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 2. Table: addresses
-- ---------------------------------------------------------------------
CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    street_address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_addresses_user_id ON addresses(user_id);

-- ---------------------------------------------------------------------
-- 3. Table: categories
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 4. Table: products
-- ---------------------------------------------------------------------
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    discount_percentage DECIMAL(5,2) DEFAULT 0.00 CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    image_url VARCHAR(255),
    rating DECIMAL(3,2) DEFAULT 0.00 CHECK (rating >= 0 AND rating <= 5),
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_status ON products(status);

-- ---------------------------------------------------------------------
-- 5. Table: cart
-- ---------------------------------------------------------------------
CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity >= 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

CREATE INDEX idx_cart_user_id ON cart(user_id);

-- ---------------------------------------------------------------------
-- 6. Table: wishlist
-- ---------------------------------------------------------------------
CREATE TABLE wishlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_wishlist_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

CREATE INDEX idx_wishlist_user_id ON wishlist(user_id);

-- ---------------------------------------------------------------------
-- 7. Table: coupons
-- ---------------------------------------------------------------------
CREATE TABLE coupons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type ENUM('PERCENTAGE', 'FLAT') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL CHECK (discount_value > 0),
    min_order_amount DECIMAL(10,2) DEFAULT 0.00 CHECK (min_order_amount >= 0),
    max_discount_amount DECIMAL(10,2) DEFAULT NULL,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 8. Table: orders
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT NOT NULL,
    coupon_id INT DEFAULT NULL,
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    discount_amount DECIMAL(10,2) DEFAULT 0.00 CHECK (discount_amount >= 0),
    shipping_fee DECIMAL(10,2) DEFAULT 0.00 CHECK (shipping_fee >= 0),
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    order_status ENUM('PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PLACED',
    payment_method ENUM('COD', 'CARD', 'UPI') NOT NULL,
    payment_status ENUM('PENDING', 'PAID', 'FAILED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE RESTRICT,
    CONSTRAINT fk_orders_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(order_status);

-- ---------------------------------------------------------------------
-- 9. Table: order_items
-- ---------------------------------------------------------------------
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL CHECK (price_per_unit >= 0),
    quantity INT NOT NULL CHECK (quantity >= 1),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_order_items_order_id ON order_items(order_id);

-- ---------------------------------------------------------------------
-- 10. Table: payments
-- ---------------------------------------------------------------------
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method ENUM('COD', 'CARD', 'UPI') NOT NULL,
    transaction_reference VARCHAR(100),
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    payment_status ENUM('PENDING', 'PAID', 'FAILED') NOT NULL DEFAULT 'PENDING',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_payments_order_id ON payments(order_id);

-- ---------------------------------------------------------------------
-- 11. Table: reviews
-- ---------------------------------------------------------------------
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_reviews_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

CREATE INDEX idx_reviews_product_id ON reviews(product_id);

-- ---------------------------------------------------------------------
-- 12. Table: coupon_usage
-- ---------------------------------------------------------------------
CREATE TABLE coupon_usage (
    id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_coupon_usage_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE RESTRICT,
    CONSTRAINT fk_coupon_usage_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_coupon_usage_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT uq_coupon_user UNIQUE (coupon_id, user_id)
) ENGINE=InnoDB;

-- =====================================================================
-- SEED DATA
-- Passwords:
-- Admin@123   -> Jd/rK9nG749FGNCoKsl44w==:Wcyd2fT7K17Qo4B7z7kWkHdMyVSNUwPsYPELB5C4i6I=
-- Customer@123-> XsHFlvwv8calj2CMMf+3ng==:8w4GBAPFnnx9muWmVVMBrZADQtrsbCs13i+cSvx+9Qo=
-- =====================================================================

-- 1. Users
INSERT INTO users (id, name, email, password, phone, role, status) VALUES
(1, 'System Admin', 'admin@shopsphere.com', 'Jd/rK9nG749FGNCoKsl44w==:Wcyd2fT7K17Qo4B7z7kWkHdMyVSNUwPsYPELB5C4i6I=', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'Customer One', 'customer@shopsphere.com', 'XsHFlvwv8calj2CMMf+3ng==:8w4GBAPFnnx9muWmVVMBrZADQtrsbCs13i+cSvx+9Qo=', '9876500001', 'CUSTOMER', 'ACTIVE'),
(3, 'Rahul Sharma', 'rahul@shopsphere.com', 'XsHFlvwv8calj2CMMf+3ng==:8w4GBAPFnnx9muWmVVMBrZADQtrsbCs13i+cSvx+9Qo=', '9876500002', 'CUSTOMER', 'ACTIVE'),
(4, 'Priya Verma', 'priya@shopsphere.com', 'XsHFlvwv8calj2CMMf+3ng==:8w4GBAPFnnx9muWmVVMBrZADQtrsbCs13i+cSvx+9Qo=', '9876500003', 'CUSTOMER', 'ACTIVE');

-- 2. Addresses
INSERT INTO addresses (id, user_id, full_name, phone, street_address, city, state, postal_code, is_default) VALUES
(1, 2, 'Customer One', '9876500001', 'Flat 402, Sunshine Heights, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', TRUE),
(2, 3, 'Rahul Sharma', '9876500002', 'Plot 18, Vaishali Nagar', 'Jaipur', 'Rajasthan', '302021', TRUE),
(3, 4, 'Priya Verma', '9876500003', '74 Civil Lines', 'Kota', 'Rajasthan', '324001', TRUE);

-- 3. Categories (6 categories)
INSERT INTO categories (id, name, description, image_url, status) VALUES
(1, 'Electronics & Audio', 'Smart headphones, speakers, wearables, and audio gear', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', 'ACTIVE'),
(2, 'Laptops & Computers', 'High performance gaming laptops, ultrabooks, and accessories', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500', 'ACTIVE'),
(3, 'Mobile Phones', 'Latest 5G smartphones, flagship devices, and tablets', 'https://images.unsplash.com/photo-1511707171634-5f897ff02560?w=500', 'ACTIVE'),
(4, 'Fashion & Apparel', 'Men and women designer clothing, footwear, and apparel', 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=500', 'ACTIVE'),
(5, 'Home & Kitchen', 'Modern appliances, kitchenware, and smart home essentials', 'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=500', 'ACTIVE'),
(6, 'Books & Stationery', 'Academic textbooks, bestsellers, and premium stationery', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500', 'ACTIVE');

-- 4. Products (32 products across 6 categories)
INSERT INTO products (id, category_id, name, brand, description, price, discount_percentage, stock, image_url, rating, status) VALUES
-- Electronics & Audio (Cat 1)
(1, 1, 'Sony WH-1000XM5 Wireless Headphones', 'Sony', 'Industry leading noise canceling wireless over-ear headphones with 30-hour battery life and crystal clear calling.', 29990.00, 10.00, 25, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', 4.80, 'ACTIVE'),
(2, 1, 'boAt Rockerz 450 Bluetooth Headset', 'boAt', 'Lightweight on-ear wireless headphones with up to 15 hours playback and 40mm dynamic drivers.', 1499.00, 25.00, 50, 'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=500', 4.30, 'ACTIVE'),
(3, 1, 'JBL Flip 6 Portable Bluetooth Speaker', 'JBL', 'Powerful 2-way speaker system delivering loud, crystal clear, powerful sound with IP67 waterproof rating.', 9999.00, 15.00, 30, 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=500', 4.60, 'ACTIVE'),
(4, 1, 'Apple AirPods Pro (2nd Gen)', 'Apple', 'Up to 2x more Active Noise Cancellation, Adaptive Audio, Transparency mode, and MagSafe USB-C case.', 24900.00, 8.00, 20, 'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=500', 4.90, 'ACTIVE'),
(5, 1, 'Samsung Galaxy Watch 6 Bluetooth', 'Samsung', 'Advanced sleep coaching, ECG heart monitoring, personalized HR zones, and sapphire crystal glass display.', 19999.00, 20.00, 15, 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=500', 4.50, 'ACTIVE'),
(6, 1, 'Noise ColorFit Pulse 3 Smartwatch', 'Noise', '1.96-inch TFT display, Bluetooth calling with Tru Sync, 100+ sports modes, and 7-day battery life.', 1799.00, 35.00, 60, 'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=500', 4.10, 'ACTIVE'),

-- Laptops & Computers (Cat 2)
(7, 2, 'MacBook Air M2 13.6-inch', 'Apple', 'Apple M2 chip, 8GB Unified Memory, 256GB SSD, Liquid Retina Display, 1080p FaceTime HD camera.', 99900.00, 12.00, 18, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500', 4.90, 'ACTIVE'),
(8, 2, 'Dell XPS 13 Plus Ultrabook', 'Dell', '13.4-inch OLED touch display, Intel Core i7 13th Gen, 16GB RAM, 512GB SSD, Windows 11 Home.', 134990.00, 10.00, 10, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500', 4.70, 'ACTIVE'),
(9, 2, 'ASUS ROG Zephyrus G14 Gaming Laptop', 'ASUS', 'AMD Ryzen 9, NVIDIA GeForce RTX 4060, 16GB DDR5, 1TB SSD, 165Hz QHD display.', 149990.00, 15.00, 8, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=500', 4.80, 'ACTIVE'),
(10, 2, 'HP Pavilion 15 Thin & Light Laptop', 'HP', 'Intel Core i5 12th Gen, 16GB DDR4, 512GB SSD, FHD Micro-Edge Anti-Glare Display, Backlit KB.', 58990.00, 18.00, 22, 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=500', 4.40, 'ACTIVE'),
(11, 2, 'Lenovo IdeaPad Slim 3 Chromebook', 'Lenovo', 'Intel Celeron N4020, 4GB RAM, 64GB eMMC, 14-inch HD display, fast boot ChromeOS, ultra portable.', 18990.00, 20.00, 4, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500', 3.90, 'ACTIVE'),
(12, 2, 'Logitech MX Master 3S Wireless Mouse', 'Logitech', 'Quiet clicks, 8K DPI any-surface tracking, electromagnetic MagSpeed scrolling, ergonomic design.', 8995.00, 5.00, 40, 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500', 4.90, 'ACTIVE'),

-- Mobile Phones (Cat 3)
(13, 3, 'Apple iPhone 15 Pro Max 256GB', 'Apple', 'Forged in titanium, A17 Pro chip, customizable Action button, 48MP main camera with 5x telephoto.', 159900.00, 5.00, 12, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=500', 4.90, 'ACTIVE'),
(14, 3, 'Samsung Galaxy S24 Ultra 5G', 'Samsung', 'Galaxy AI features, Snapdragon 8 Gen 3, 200MP camera, built-in S Pen, Titanium Gray.', 129999.00, 8.00, 14, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500', 4.80, 'ACTIVE'),
(15, 3, 'OnePlus 12 5G (16GB RAM, 512GB)', 'OnePlus', 'Snapdragon 8 Gen 3, 4th Gen Hasselblad Camera, 2K 120Hz ProXDR display, 100W SUPERVOOC charging.', 69999.00, 10.00, 20, 'https://images.unsplash.com/photo-1580910051074-3eb694886505?w=500', 4.70, 'ACTIVE'),
(16, 3, 'Google Pixel 8 128GB Hazel', 'Google', 'Google Tensor G3 chip, best-in-class computational photography, 6.2-inch Actua display, 7 years OS updates.', 75999.00, 15.00, 16, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500', 4.60, 'ACTIVE'),
(17, 3, 'Redmi Note 13 Pro+ 5G', 'Xiaomi', 'Curved AMOLED 1.5K display, 200MP camera with OIS, MediaTek Dimensity 7200-Ultra, IP68 water resistance.', 31999.00, 12.00, 3, 'https://images.unsplash.com/photo-1567581935884-3349723552ca?w=500', 4.30, 'ACTIVE'),
(18, 3, 'Realme Narzo 60x 5G', 'Realme', 'Stellar design, 50MP AI primary camera, 33W SUPERVOOC fast charge, 5000mAh massive battery.', 12999.00, 15.00, 35, 'https://images.unsplash.com/photo-1574944985070-8f3ebc6b79d2?w=500', 4.20, 'ACTIVE'),

-- Fashion & Apparel (Cat 4)
(19, 4, 'Levi\'s Men 511 Slim Fit Jeans', 'Levi\'s', 'Classic slim fit denim jeans crafted from stretch cotton for all-day comfort and timeless styling.', 3299.00, 20.00, 45, 'https://images.unsplash.com/photo-1542272604-780c96856592?w=500', 4.50, 'ACTIVE'),
(20, 4, 'Nike Air Max 270 Running Shoes', 'Nike', 'Legendary big Air unit in the heel delivers responsive cushioning and head-turning athletic aesthetics.', 12995.00, 10.00, 18, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500', 4.80, 'ACTIVE'),
(21, 4, 'Puma Men Graphic Crewneck T-Shirt', 'Puma', '100% breathable organic cotton regular fit casual tee with signature metallic Puma cat logo.', 999.00, 30.00, 80, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=500', 4.20, 'ACTIVE'),
(22, 4, 'Zara Oversized Wool Blend Coat', 'Zara', 'Lapel collar long sleeve coat with front flap pockets and matching tie belt in camel tone.', 8990.00, 15.00, 12, 'https://images.unsplash.com/photo-1539533018447-63fcce667823?w=500', 4.60, 'ACTIVE'),
(23, 4, 'Fossil Grant Chronograph Leather Watch', 'Fossil', 'Classic roman numeral dial, blue sunray face, 44mm stainless steel case with genuine brown leather strap.', 11995.00, 25.00, 15, 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=500', 4.70, 'ACTIVE'),
(24, 4, 'Ray-Ban Classic Aviator Sunglasses', 'Ray-Ban', 'Timeless teardrop shape, gold metal frame with G-15 green mineral glass lenses offering 100% UV protection.', 8590.00, 10.00, 25, 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=500', 4.70, 'ACTIVE'),

-- Home & Kitchen (Cat 5)
(25, 5, 'Philips Air Fryer HD9252/90', 'Philips', 'Rapid Air technology for healthy frying with up to 90% less fat, touch screen with 7 presets.', 8999.00, 20.00, 20, 'https://images.unsplash.com/photo-1585515320310-259814833e62?w=500', 4.60, 'ACTIVE'),
(26, 5, 'Prestige Iris 750 Watt Mixer Grinder', 'Prestige', 'Heavy duty motor, 3 stainless steel multipurpose jars plus 1 transparent juicer jar with safety lock.', 3499.00, 22.00, 35, 'https://images.unsplash.com/photo-1570222094114-d054a817e56b?w=500', 4.30, 'ACTIVE'),
(27, 5, 'Nespresso Inissia Espresso Maker', 'Nespresso', 'Compact and lightweight pod coffee machine with 19-bar high pressure pump and fast 25-second heat-up.', 14999.00, 15.00, 2, 'https://images.unsplash.com/photo-1517668808822-9ebb02ae2a0e?w=500', 4.70, 'ACTIVE'),
(28, 5, 'Instant Pot Duo 7-in-1 Pressure Cooker', 'Instant Pot', 'Electric pressure cooker, slow cooker, rice cooker, steamer, sauté pan, yogurt maker, and food warmer.', 9999.00, 18.00, 16, 'https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=500', 4.80, 'ACTIVE'),

-- Books & Stationery (Cat 6)
(29, 6, 'Clean Code: Agile Software Craftsmanship', 'Robert C. Martin', 'Handbook of agile software craft, best engineering practices, functions, classes, and code smells.', 1899.00, 15.00, 40, 'https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500', 4.90, 'ACTIVE'),
(30, 6, 'Design Patterns: Elements of Reusable OO Software', 'Gang of Four', 'The foundational classic cataloging 23 software design patterns across creational, structural, and behavioral domains.', 2499.00, 10.00, 30, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500', 4.80, 'ACTIVE'),
(31, 6, 'Effective Java (3rd Edition)', 'Joshua Bloch', 'Comprehensive guide to modern Java best practices, immutability, lambdas, generics, and concurrent programming.', 2199.00, 12.00, 25, 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500', 4.90, 'ACTIVE'),
(32, 6, 'Parker Vector Standard Rollerball Pen', 'Parker', 'Precision fine tip rollerball pen with stainless steel trim and ultra smooth black ink flow.', 349.00, 10.00, 100, 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=500', 4.40, 'ACTIVE');

-- 5. Coupons (3 coupons: percentage & flat with min and max)
INSERT INTO coupons (id, code, discount_type, discount_value, min_order_amount, max_discount_amount, start_date, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENTAGE', 10.00, 1000.00, 500.00, '2026-01-01', '2026-12-31', 'ACTIVE'),
(2, 'FESTIVE500', 'FLAT', 500.00, 2500.00, 500.00, '2026-01-01', '2026-12-31', 'ACTIVE'),
(3, 'SUPER20', 'PERCENTAGE', 20.00, 3000.00, 1500.00, '2026-01-01', '2026-12-31', 'ACTIVE');

-- 6. Sample Initial Orders & Order Items
-- Customer One (id: 2) placed order 1
INSERT INTO orders (id, user_id, address_id, coupon_id, subtotal, discount_amount, shipping_fee, total_amount, order_status, payment_method, payment_status, created_at) VALUES
(1, 2, 1, 1, 26991.00, 500.00, 0.00, 26491.00, 'DELIVERED', 'CARD', 'PAID', '2026-09-15 11:30:00'),
(2, 3, 2, 2, 3499.00, 500.00, 50.00, 3049.00, 'CONFIRMED', 'UPI', 'PAID', '2026-10-10 14:20:00');

-- Order 1 items (Sony headphones @ 29990 with 10% disc = 26991)
INSERT INTO order_items (id, order_id, product_id, price_per_unit, quantity, subtotal) VALUES
(1, 1, 1, 26991.00, 1, 26991.00);

-- Order 2 items (Prestige mixer grinder @ 3499)
INSERT INTO order_items (id, order_id, product_id, price_per_unit, quantity, subtotal) VALUES
(2, 2, 26, 3499.00, 1, 3499.00);

-- Payments for seeded orders
INSERT INTO payments (id, order_id, payment_method, transaction_reference, amount, payment_status, payment_date) VALUES
(1, 1, 'CARD', 'TXN_MOCK_CARD_98231', 26491.00, 'PAID', '2026-09-15 11:32:00'),
(2, 2, 'UPI', 'TXN_MOCK_UPI_54192', 3049.00, 'PAID', '2026-10-10 14:21:00');

-- Record coupon usage for order 1 & 2
INSERT INTO coupon_usage (coupon_id, user_id, order_id, used_at) VALUES
(1, 2, 1, '2026-09-15 11:30:00'),
(2, 3, 2, '2026-10-10 14:20:00');

-- 7. Reviews
INSERT INTO reviews (user_id, product_id, rating, comment, created_at) VALUES
(2, 1, 5, 'Unbelievable sound quality and active noise cancellation. Best headphones ever purchased!', '2026-09-18 10:15:00'),
(3, 26, 4, 'Very sturdy mixer grinder. Handles tough spices easily, slightly noisy on speed 3.', '2026-10-12 16:45:00');
