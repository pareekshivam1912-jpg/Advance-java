# ShopSphere: An Enterprise-Grade Multi-Tier E-Commerce Platform & Distributed Inventory Management System

**Academic Degree:** Bachelor of Technology (B.Tech) in Computer Science & Engineering  
**University:** Rajasthan Technical University (RTU), Kota  
**Course Code & Subject:** 6CS4-04 — Advanced Java Laboratory / Capstone Major Project  
**Author / Candidate:** B.Tech Final Year Student  
**Academic Session:** 2025–2026  

---

## Table of Contents
1. [Introduction](#1-introduction)
2. [Problem Statement](#2-problem-statement)
3. [Project Objectives](#3-project-objectives)
4. [Software Requirements Specification (SRS)](#4-software-requirements-specification-srs)
5. [Technology Stack & Architectural Justifications](#5-technology-stack--architectural-justifications)
6. [System Architecture & Design](#6-system-architecture--design)
7. [Database Design & Normalization](#7-database-design--normalization)
8. [Module Decomposition & Detailed Design](#8-module-decomposition--detailed-design)
9. [Implementation Methodology & Code Analysis](#9-implementation-methodology--code-analysis)
10. [Testing Strategy, Verification Matrix & Results](#10-testing-strategy-verification-matrix--results)
11. [User Interface Screenshots & Visual Artifacts](#11-user-interface-screenshots--visual-artifacts)
12. [RTU Syllabus Mapping & Academic Alignment](#12-rtu-syllabus-mapping--academic-alignment)
13. [System Limitations & Future Scope](#13-system-limitations--future-scope)
14. [Conclusion](#14-conclusion)
15. [Academic References & Bibliography](#15-academic-references--bibliography)

---

## 1. Introduction

Electronic commerce has evolved from rudimentary online catalogs into highly distributed, mission-critical enterprise systems. Modern commerce engines require resilient transaction processing, responsive user interfaces across mobile and desktop environments, real-time inventory synchronization across geographically distributed warehouses, and stringent security against adversarial attacks.

**ShopSphere** is an enterprise-grade e-commerce management system designed and engineered to fulfill two synergistic goals:
1. Deliver a robust, feature-complete, modern retail management platform capable of processing complex shopping lifecycles, transactional checkouts, administrative oversight, and distributed warehouse synchronization.
2. Serve as the definitive reference implementation for the **Rajasthan Technical University (RTU) 6CS4-04 Advanced Java** curriculum, covering 100% of the prescribed academic syllabus across Enterprise Java (J2EE/Jakarta EE 10), Servlets, JSP, JSTL, Expression Language, Custom Tags, plain JDBC without ORM abstractions, TCP Socket Networking (`java.net`), Distributed Remote Method Invocation (Java RMI), Object Serialization, Java Swing Desktop MVC with Event Dispatch Thread (EDT) safety, Pluggable Look and Feel (PLAF), Internationalization (i18n), and JNDI Enterprise Naming.

Built upon Java 21 LTS and Apache Tomcat 11, ShopSphere demonstrates how enterprise software engineering principles—such as N-tier separation of concerns, Model-View-Controller (MVC), interface-driven programming, and transactional ACID boundaries—are implemented directly using core Java APIs.

---

## 2. Problem Statement

Many contemporary university capstone projects rely on high-level, opinionated web frameworks (e.g., Spring Boot, Hibernate, Django, or Next.js) that abstract away foundational computer science concepts:
- **Abstraction Hiding:** High-level ORMs like Hibernate obscure database locking, cursor management, connection lifecycles, and relational normalization.
- **Protocol Obscurity:** Automated REST libraries prevent students from understanding raw HTTP request-response pipelines, MIME handling, filter chains, and socket-level networking.
- **Architectural Disconnect:** Few projects bridge the gap between traditional enterprise web tiers, desktop administrative consoles, and low-level distributed RPC/TCP protocols in a single coherent codebase.
- **Academic Gaps:** Conventional e-commerce tutorials omit critical syllabus requirements such as Java RMI, custom network protocol handlers, multithreaded TCP servers, JNDI lookup, and thread-safe desktop GUI programming.

**ShopSphere solves this problem** by implementing a production-grade multi-module enterprise platform using **pure Java and Jakarta EE specifications**, avoiding third-party magic frameworks while maintaining immaculate clean-code architecture.

---

## 3. Project Objectives

The primary engineering and educational objectives of the ShopSphere project are:

1. **Architectural Purity (5-Tier Enterprise Pattern):** Enforce strict decoupling across the Presentation Tier (Web & Swing), Controller Tier (Jakarta Servlets & Action Listeners), Business Service Tier (Transaction Managers), Data Access Tier (JDBC DAOs), and Persistence Tier (MySQL InnoDB).
2. **ACID Transactional Integrity:** Implement atomic multi-step order checkout where inventory locking (`SELECT ... FOR UPDATE`), stock decrementing, order item creation, payment capture, and shopping cart purging occur within a single database transaction with explicit `commit()` and `rollback()` handling.
3. **Dual Client Architecture:** Provide both a responsive, mobile-first web storefront (HTML5, Bootstrap 5, JavaScript) and an administrative desktop GUI built in Java Swing following strict MVC patterns and Event Dispatch Thread (EDT) safety via `SwingWorker`.
4. **Distributed Systems Integration:** Engineer a multithreaded TCP Socket Server (`java.net.ServerSocket`, port 5000) with thread-pool worker dispatch for warehouse terminals, alongside a Java Remote Method Invocation (RMI) service (port 1099) for inter-JVM remote inventory management.
5. **Robust Web Security:** Defend against the OWASP Top 10 vulnerabilities, including 100% parameterized SQL queries via `PreparedStatement` (defeating SQL Injection), output entity escaping (defeating Cross-Site Scripting), session ID rotation via `request.changeSessionId()` (defeating Session Fixation), and PBKDF2 with HMAC-SHA256 salted password hashing.
6. **Academic Syllabus Completeness:** Provide live, testable demonstration interfaces for all 29 RTU Advanced Java curriculum topics, accessible through an integrated Syllabus Academic Demo Hub.

---

## 4. Software Requirements Specification (SRS)

### 4.1 Functional Requirements (FR)

- **FR-01: User Identity & Access Management (RBAC):** Self-service customer registration, credential authentication, session management, and role-based access control distinguishing `CUSTOMER` and `ADMIN` roles.
- **FR-02: Product Catalog & Faceted Search:** Dynamic category navigation, full-text keyword search across product titles and descriptions, multi-parameter filtering (category, price range, stock status), and multi-criteria sorting (price ascending/descending, newest arrivals).
- **FR-03: Cart & Wishlist Management:** Persistent shopping cart storage, real-time quantity increment/decrement, live subtotal recalculation, wishlist tracking, and atomic "Move to Cart" transitions.
- **FR-04: Coupon & Discount Engine:** Validation and application of promotional discount codes supporting both percentage discounts (e.g. 10% off) and flat cash discounts (e.g. ₹500 off), enforcing expiration dates, minimum cart value thresholds, and maximum usage counters.
- **FR-05: Transactional Order Processing:** Multi-step checkout with delivery address selection, payment gateway simulation (Credit/Debit Card, UPI, Net Banking, COD), GST (18%) tax computation, order number generation, and invoice viewing.
- **FR-06: Administrative Management Console:** Web and desktop interfaces for product catalog CRUD, category administration, real-time inventory restock actions, order status lifecycle management (`PENDING` &rarr; `CONFIRMED` &rarr; `SHIPPED` &rarr; `DELIVERED` &rarr; `CANCELLED`), and customer activation/deactivation.
- **FR-07: Distributed Network Services:** Low-level TCP socket client-server communication for automated stock auditing, custom URL protocol handler (`shop://`), and RMI registry binding for remote administrative method execution.
- **FR-08: Internationalization (i18n):** Runtime bilingual language switching between English (`en_IN`) and Hindi (`hi_IN`), localized currency symbols (₹ INR), and localized timestamps.

### 4.2 Non-Functional Requirements (NFR)

- **NFR-01: Performance & Concurrency:** The web container must handle concurrent user requests without state leakage across threads; the TCP socket server must employ an executor thread pool to cap resource consumption at 10 concurrent threads.
- **NFR-02: Security & Privacy:** Sensitive user passwords must never be stored in plain text or reversible formats; session identifiers must be regenerated upon authentication; administrative endpoints (`/admin/*`) must be inaccessible to standard customers.
- **NFR-03: Portability & Modularity:** The application must build and run on any POSIX or Windows operating system with a standard JDK 17+ and MySQL database without OS-specific bindings.
- **NFR-04: Data Integrity:** Foreign key constraints must enforce referential integrity across all database operations; order item historical prices must remain immutable even if the master product catalog price changes subsequently.

---

## 5. Technology Stack & Architectural Justifications

| Layer / Subsystem | Technology Selected | Version | Architectural Justification |
|:---|:---|:---|:---|
| **Programming Language** | Java (OpenJDK) | 21.0.11 LTS | Long-Term Support release offering modern language ergonomics (records, pattern matching, virtual threads) while maintaining full backward compatibility with standard Enterprise APIs. |
| **Enterprise Standard** | Jakarta EE | 10 (Servlet 6.0/6.1, JSP 3.1) | Industry standard for Java web containers. Replaces the legacy `javax.*` namespace with the modern `jakarta.*` package namespace. |
| **Web Container** | Apache Tomcat | 11.0.25 | Production-grade open-source servlet container implementing the Jakarta Servlet 6.1, JSP 3.1, and EL 6.0 specifications with high-performance NIO connectors. |
| **Relational Database** | MySQL Server | 9.7 / 8.0+ | Robust ACID-compliant relational DBMS with InnoDB storage engine, row-level locking, foreign key enforcement, and high-throughput connection handling. |
| **Persistence Driver** | MySQL Connector/J | 9.1.0 | Official Type-4 JDBC driver converting Java database calls directly into MySQL binary network protocol packets without native client libraries. |
| **Desktop GUI Engine** | Java Swing | Built-in (JDK 21) | Mature, lightweight GUI toolkit featuring MVC architecture, pluggable look and feel, and background worker concurrency via `SwingWorker`. |
| **Tag Libraries** | Glassfish JSTL | 3.0.1 | Reference implementation of Jakarta Standard Tag Library providing standardized core, formatting, XML, SQL, and function tags. |
| **Build & Dependency Tool** | Apache Maven | 3.9.16 | Multi-module reactor build system ensuring deterministic dependency resolution, automated compilation, testing, and WAR/JAR packaging. |
| **Testing Framework** | JUnit 5 (Jupiter) | 5.11.4 | Modern unit testing framework supporting parameterized tests, lifecycle hooks, and Maven Surefire integration. |
| **Frontend Styling** | Bootstrap 5 & Bootstrap Icons | 5.3.3 | Responsive CSS grid system and modern UI components ensuring cross-device compatibility without heavy frontend JavaScript frameworks. |

---

## 6. System Architecture & Design

### 6.1 Multi-Tier Architecture (N-Tier Pattern)

ShopSphere is organized into five strictly separated logical layers:

```
+-----------------------------------------------------------------------+
| 1. PRESENTATION TIER                                                  |
|    - Web Browser Client (HTML5, Bootstrap 5, Vanilla JS)              |
|    - Desktop Admin GUI (Java Swing MVC, Pluggable Look and Feel)       |
|    - TCP Socket Inventory Client (Command Line Terminal)             |
|    - Distributed Java RMI Client Application                         |
+-----------------------------------------------------------------------+
                                   |
                                   v
+-----------------------------------------------------------------------+
| 2. CONTROLLER / INTERCEPTOR TIER                                      |
|    - Filter Interceptor Chain: EncodingFilter -> SecurityFilter ->    |
|                                AuthenticationFilter -> AdminFilter    |
|    - Jakarta HTTP Servlets: HomeServlet, ProductServlet, CartServlet, |
|                             CheckoutServlet, AdminServlets, Auth      |
|    - Swing Action Controllers (SwingWorker off-EDT Concurrency)       |
|    - Distributed Dispatchers: InventoryServer, RmiServer Registry     |
+-----------------------------------------------------------------------+
                                   |
                                   v
+-----------------------------------------------------------------------+
| 3. BUSINESS SERVICE TIER                                              |
|    - UserService, ProductService, CartService, WishlistService        |
|    - CheckoutService (ACID Database Transaction Coordinator)          |
|    - OrderService, CouponService, LocalInventoryService               |
|    - ServiceFactory (Thread-Safe Dependency Provider)                 |
+-----------------------------------------------------------------------+
                                   |
                                   v
+-----------------------------------------------------------------------+
| 4. DATA ACCESS OBJECT (DAO) TIER                                      |
|    - Interfaces & Plain JDBC Implementations (*DAO, *DAOImpl)         |
|    - 100% Parameterized PreparedStatement Execution                  |
|    - DBUtil: Dual-Mode Connection Factory (DriverManager & JNDI DS)   |
+-----------------------------------------------------------------------+
                                   |
                                   v
+-----------------------------------------------------------------------+
| 5. PERSISTENCE TIER                                                   |
|    - MySQL 9.7 Database Server (InnoDB Storage Engine)                 |
|    - 12 Relational Tables in Third Normal Form (3NF)                  |
|    - Cascading Foreign Keys, Unique Indexes, Row-Level Locking        |
+-----------------------------------------------------------------------+
```

### 6.2 The Web Request-Response Pipeline

When an HTTP request arrives at Apache Tomcat 11:
1. **NIO Connector:** Tomcat receives the TCP packet on port 8080 and parses the raw HTTP stream into `jakarta.servlet.http.HttpServletRequest` and `HttpServletResponse` objects.
2. **Filter Interception:** The request traverses the configured filter chain:
   - `EncodingFilter`: Forces `UTF-8` encoding.
   - `SecurityFilter`: Injects browser defense headers (`X-Frame-Options`, `Content-Security-Policy`).
   - `AuthenticationFilter`: Intercepts protected routes (`/cart`, `/checkout`, `/orders`) and redirects unauthenticated users to `/login`.
   - `AdminFilter`: Validates that the active session holds `Role.ADMIN` for all `/admin/*` routes.
3. **Servlet Routing:** Tomcat matches the request URL path against servlet URL mappings declared via `@WebServlet` or `web.xml` and dispatches to the corresponding servlet's `service()` method.
4. **Service Execution:** The servlet extracts parameters, validates input, and delegates business operations to pure Java service classes acquired through `ServiceFactory`.
5. **Data Access:** Services call DAOs, which retrieve connections from `DBUtil`, execute parameterized SQL via `PreparedStatement`, and map `ResultSet` records into POJOs.
6. **View Forwarding:** The servlet binds result domain objects to `HttpServletRequest` attributes (`request.setAttribute("products", list)`) and forwards execution via `RequestDispatcher.forward()` to a JSP view.
7. **JSP Rendering:** The JSP engine evaluates JSTL tags and Expression Language (`${...}`) to render clean HTML, streaming the response back to the client browser.

---

## 7. Database Design & Normalization

The ShopSphere relational schema is designed in **Third Normal Form (3NF)** to eliminate data redundancy, prevent update anomalies, and ensure referential integrity.

### 7.1 Entity Relationship Overview
The schema contains 12 core tables:
1. `users`: Master user account credentials, roles (`CUSTOMER`, `ADMIN`), and activation flags.
2. `addresses`: Customer shipping and billing addresses with default address indicators.
3. `categories`: Product hierarchical classification with SEO-friendly slugs and display ordering.
4. `products`: Retail merchandise catalog with pricing, stock counts, SKU, and featured tags.
5. `cart`: Active customer shopping cart line items with real-time quantities.
6. `wishlist`: Customer bookmarked products for future purchase consideration.
7. `coupons`: Promotional discount rules (percentage or flat), validation windows, and usage caps.
8. `orders`: Master order records capturing order numbers, statuses, shipping fees, tax, and totals.
9. `order_items`: Immutable snapshot of purchased products, quantities, and historical unit prices.
10. `payments`: Payment transaction records, transaction references, methods, and gateway responses.
11. `reviews`: Customer ratings (1 to 5 stars), commentary, and moderation approval flags.
12. `coupon_usage`: Audit log linking coupons, users, and orders to prevent duplicate redemptions.

### 7.2 Normalization Proof (1NF to 3NF)
- **First Normal Form (1NF):** Every column contains atomic, indivisible values (e.g. addresses are atomized into street line, city, state, postal code, country). Primary keys uniquely identify every record.
- **Second Normal Form (2NF):** All tables have single-column surrogate primary keys (`id INT AUTO_INCREMENT`), ensuring that all non-key attributes are fully functionally dependent on the entire primary key.
- **Third Normal Form (3NF):** There are no transitive dependencies. In `orders`, customer address data is not duplicated; instead, an `address_id` foreign key references the `addresses` table. When an order is placed, historical product prices are copied to `order_items.price` because historical purchase prices represent an immutable financial record that must not change when the master `products.price` updates.

---

## 8. Module Decomposition & Detailed Design

The ShopSphere repository is structured as a Maven multi-module reactor project comprising 6 distinct modules:

```
shopsphere/
├── pom.xml                 (Parent BOM & Dependency Management)
├── core/                   (Domain Models, JDBC DAOs, Services, Utilities)
├── web/                    (Jakarta EE 10 WAR: Servlets, JSPs, JSTL, Tag Files, Filters)
├── swing-admin/            (Java Swing Desktop Admin GUI with MVC & SwingWorker)
├── network/                (Multithreaded TCP Socket Server, Client, Custom Protocol Handler)
├── rmi/                    (Remote Method Invocation Server, Client, Remote Interface)
└── docs/                   (Comprehensive Architectural, Viva & Setup Documentation)
```

### 8.1 Module: `shopsphere-core`
- **Domain Models (`com.shopsphere.model`):** 11 pure Java POJOs encapsulating enterprise entities (`User`, `Product`, `Order`, `OrderItem`, etc.). Models implement `java.io.Serializable` to support distributed networking and caching.
- **DAO Subsystem (`com.shopsphere.dao`):** Interface-driven data access layer (`UserDAO`, `ProductDAO`, `OrderDAO`, etc.) backed by robust JDBC implementations (`*DAOImpl`) utilizing `PreparedStatement` and `try-with-resources`.
- **Business Services (`com.shopsphere.service`):** 10 domain services encapsulating business validation, discount formulas, and inventory operations.
- **Transaction Coordinator (`CheckoutService`):** Manages multi-table database transactions with programmatic commit/rollback semantics.
- **Service Factory (`ServiceFactory`):** Centralized singleton registry wiring services and DAOs, eliminating hardcoded cross-dependencies.

### 8.2 Module: `shopsphere-web`
- **Authentication Subsystem:** `LoginServlet`, `RegisterServlet`, `LogoutServlet` managing session lifecycles and password validation.
- **Customer Storefront:** `HomeServlet` (featured products and cookie parsing), `ProductServlet` (search, category filtering, pagination, details view).
- **Cart & Order Flow:** `CartServlet`, `WishlistServlet`, `CheckoutServlet`, `OrderServlet`.
- **Administrative Portal:** `AdminDashboardServlet`, `AdminProductServlet`, `AdminOrderServlet`, `AdminCustomerServlet`, `AdminCategoryServlet`, `AdminCouponServlet`.
- **Syllabus Academic Demos:** Dedicated educational views demonstrating JSP traditional elements (`jsp-elements.jsp`), Expression Language scopes (`el-demo.jsp`), JSTL XML feed parsing (`jstl-xml.jsp`), JSTL SQL tag library (`jstl-sql.jsp`), Tomcat JNDI DataSource (`jndi-demo.jsp`), and `HttpSession` state inspection (`session-demo.jsp`).
- **Custom Tag Extensions:** Classic Java tag handler `<shop:price>` (`PriceTagHandler.java`) and reusable tag files (`productCard.tag`, `alert.tag`).

### 8.3 Module: `shopsphere-swing-admin`
- **MVC Architecture:** Separation of Models (`ProductTableModel`, `OrderTableModel` extending `AbstractTableModel`), Views (`LoginFrame`, `AdminDashboard`, `ProductPanel`), and Controllers (`ProductController`, `OrderController`).
- **EDT Concurrency Safety:** Asynchronous execution off the Event Dispatch Thread using `javax.swing.SwingWorker`, preventing GUI freezing during database calls.
- **Pluggable Look and Feel (PLAF):** Runtime theme switching between System Default, Nimbus, and Metal using `UIManager.setLookAndFeel()` and `SwingUtilities.updateComponentTreeUI()`.

### 8.4 Module: `shopsphere-network`
- **Multithreaded Socket Server:** `InventoryServer` listening on TCP port 5000, dispatching client connections to a fixed pool of 10 worker threads.
- **Inventory Client:** `InventoryClient` sending low-level protocol commands (`CHECK_STOCK:id`, `UPDATE_STOCK:id:qty`) over standard socket streams.
- **Serialization Demonstrator:** `SerializationDemo` illustrating object marshaling, `transient` password masking, and `serialVersionUID` validation.
- **Custom URL Protocol Handler:** `UrlDemo` and `ShopURLStreamHandlerFactory` registering custom `shop://` protocol handling.

### 8.5 Module: `shopsphere-rmi`
- **Remote Interface:** `InventoryService` extending `java.rmi.Remote` declaring remote inventory lookup and modification methods.
- **Server Implementation:** `InventoryServiceImpl` extending `UnicastRemoteObject` delegating to `LocalInventoryService`.
- **RMI Server:** `RmiServer` creating an in-process RMI Registry on port 1099 and rebinding the remote stub.
- **RMI Client:** `RmiClient` performing `Naming.lookup()` to acquire the remote stub and execute remote method calls across JVM boundaries.

---

## 9. Implementation Methodology & Code Analysis

### 9.1 Atomic Transaction Processing (`CheckoutService.java`)
E-commerce checkouts require guaranteed atomicity: either all database updates succeed, or the entire operation rolls back without leaving orphaned records.

```java
public Order processCheckout(int userId, int addressId, String couponCode, PaymentMethod method) {
    Connection conn = null;
    try {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false); // Begin ACID Transaction Boundary

        // 1. Fetch current cart items
        List<CartItem> cartItems = cartDAO.findByUserId(conn, userId);
        if (cartItems.isEmpty()) {
            throw new ShopSphereException("Cannot checkout with an empty cart.");
        }

        // 2. Validate and decrement stock with row-level locks
        for (CartItem item : cartItems) {
            Product product = productDAO.findByIdForUpdate(conn, item.getProductId());
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new ShopSphereException("Insufficient stock for: " + product.getName());
            }
            productDAO.updateStock(conn, item.getProductId(), -item.getQuantity());
        }

        // 3. Compute financial amounts (Server-Side Calculation)
        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal discount = calculateDiscount(couponCode, subtotal);
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal shipping = subtotal.compareTo(new BigDecimal("50000")) >= 0 ? BigDecimal.ZERO : new BigDecimal("500");
        BigDecimal totalAmount = subtotal.subtract(discount).add(tax).add(shipping);

        // 4. Create Order and OrderItem records
        Order order = new Order(...);
        order = orderDAO.create(conn, order);
        for (CartItem item : cartItems) {
            orderItemDAO.create(conn, new OrderItem(order.getId(), item.getProductId(), ...));
        }

        // 5. Create Payment and purge Cart
        paymentDAO.create(conn, new Payment(order.getId(), method, totalAmount, ...));
        cartDAO.clearCart(conn, userId);

        conn.commit(); // Commit Transaction
        return order;
    } catch (Exception e) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) { /* log */ }
        }
        throw new ShopSphereException("Checkout failed: " + e.getMessage(), e);
    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* log */ }
        }
    }
}
```

### 9.2 Thread-Safe Swing Background Execution (`SwingWorker`)
In `shopsphere-swing-admin`, long-running database queries are decoupled from the Event Dispatch Thread:

```java
public void loadProductsAsync() {
    view.setLoading(true);
    SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
        @Override
        protected List<Product> doInBackground() throws Exception {
            // Executed on background worker thread (off EDT)
            return productService.getAllProducts();
        }

        @Override
        protected void done() {
            // Executed back on Event Dispatch Thread (EDT)
            try {
                List<Product> products = get();
                tableModel.setProducts(products);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Failed to load products: " + e.getMessage());
            } finally {
                view.setLoading(false);
            }
        }
    };
    worker.execute();
}
```

---

## 10. Testing Strategy, Verification Matrix & Results

### 10.1 Testing Strategy
ShopSphere underwent multi-tiered verification:
1. **Automated Unit Testing:** JUnit 5 tests running under Maven Surefire validating isolated models, algorithms (discount formulas, tax calculations, password hashing), and DAOs.
2. **Integration Testing:** Verification of database connectivity, transactional rollback under error conditions, and multi-threaded socket communication.
3. **Manual Functional Testing:** Verification of end-to-end user journeys (registration &rarr; browsing &rarr; checkout &rarr; admin fulfillment).
4. **Security Vulnerability Assessment:** Penetration testing for SQL Injection, XSS, CSRF, and Privilege Escalation.

### 10.2 Automated Test Execution Summary
- **Total Unit & Integration Tests:** 32 Tests
- **Passed:** 32 (100%)
- **Failed:** 0
- **Errors:** 0
- **Code Coverage:** High across business service, utility, and DAO layers.

*(For the complete test matrix and automated execution log, refer to `docs/TEST_REPORT.md`.)*

---

## 11. User Interface Screenshots & Visual Artifacts

The application user interface has been cataloged into 18 primary visual artifacts representing every facet of the system.

| Figure | Image Identifier | Description & Screen Details |
|:---:|:---|:---|
| **Fig 11.1** | `01-homepage.png` | Storefront Homepage featuring hero architecture banner, category carousel, featured items, and cookie-based recently viewed items. |
| **Fig 11.2** | `02-catalog-filter.png` | Product Catalog faceted search with price slider, category filters, and sorting controls. |
| **Fig 11.3** | `03-product-details.png` | Product Details page with live stock badges, customer reviews, and `<shop:price>` custom tag. |
| **Fig 11.4** | `04-shopping-cart.png` | Interactive Shopping Cart showing real-time quantity adjustments, line subtotals, and GST breakdown. |
| **Fig 11.5** | `05-checkout-coupon.png` | Multi-step Checkout View with address selection, coupon discount application, and payment gateway options. |
| **Fig 11.6** | `06-order-confirmation.png` | Order Confirmation and Invoice view displaying generated order number, itemized breakdown, and delivery address. |
| **Fig 11.7** | `07-order-history.png` | Customer Order History and Status Tracking Console. |
| **Fig 11.8** | `08-user-login.png` | Secure User Authentication Interface with PBKDF2 credential verification and demo autofill helpers. |
| **Fig 11.9** | `09-admin-dashboard.png` | Web Administration Dashboard displaying key performance metrics, order volume, and inventory warnings. |
| **Fig 11.10** | `10-admin-products.png` | Product Inventory CRUD Management Console in Web Admin. |
| **Fig 11.11** | `11-admin-orders.png` | Order Processing Console allowing administrative status transitions and audit tracking. |
| **Fig 11.12** | `12-demo-hub.png` | RTU Syllabus Academic Demonstration Hub showcasing all Jakarta EE curriculum topics. |
| **Fig 11.13** | `13-jsp-elements-demo.png` | JSP Traditional Elements and Implicit Objects demonstration view. |
| **Fig 11.14** | `14-i18n-hindi.png` | Multi-language Internationalization (i18n) showing dynamic switching to Hindi via ResourceBundle. |
| **Fig 11.15** | `15-swing-admin-login.png` | Java Swing Desktop Administration Login Window executing background authentication off the EDT. |
| **Fig 11.16** | `16-swing-admin-dashboard.png`| Java Swing Desktop Admin Console implementing MVC with custom `AbstractTableModel` and runtime PLAF switching. |
| **Fig 11.17** | `17-socket-terminal.png` | Multithreaded TCP Socket Server and Client terminal session executing low-level inventory protocol commands. |
| **Fig 11.18** | `18-rmi-terminal.png` | Java Remote Method Invocation (RMI) distributed client-server terminal session. |

*(For instructions on capturing and saving these screenshots into `docs/screenshots/`, see `docs/screenshots/README.md`.)*

---

## 12. RTU Syllabus Mapping & Academic Alignment

The ShopSphere architecture comprehensively covers every syllabus topic specified in the **RTU 6CS4-04 Advanced Java** laboratory curriculum.

| # | RTU Syllabus Topic | Implementation Location in Project | Academic Concept Demonstrated |
|:---:|:---|:---|:---|
| **1** | **Java Swing & MVC** | `swing-admin/src/main/java/com/shopsphere/swing/` | Model (`ProductTableModel`), View (`AdminDashboard`), Controller (`ProductController`), off-EDT via `SwingWorker`. |
| **2** | **Pluggable Look & Feel** | `AdminDashboard.java` (Menu Bar) | Runtime PLAF switching between System Default, Nimbus, and Metal via `UIManager.setLookAndFeel`. |
| **3** | **Java Applets (Concept)** | `docs/APPLETS.md`, `docs/examples/AppletDemo.java` | 5 lifecycle methods (`init, start, paint, stop, destroy`), security sandbox, NPAPI deprecation, modern replacements. |
| **4** | **Plain JDBC (No ORM)** | `core/src/main/java/com/shopsphere/dao/` | Type-4 MySQL driver, `PreparedStatement`, positional parameterization, `ResultSet` cursor processing. |
| **5** | **ACID Transactions** | `core/src/main/java/com/shopsphere/service/CheckoutService.java` | `conn.setAutoCommit(false)`, `commit()`, `rollback()` coordinating inventory, orders, and payments. |
| **6** | **Java Sockets (`java.net`)**| `network/src/main/java/com/shopsphere/network/` | `ServerSocket`, `Socket`, multithreading with thread-pool worker dispatch, custom inventory protocol on port 5000. |
| **7** | **URL Protocol Handler** | `network/src/main/java/com/shopsphere/network/demo/UrlDemo.java` | Custom `shop://` protocol handler via `URLStreamHandler` and `URLConnection`. |
| **8** | **Java RMI** | `rmi/src/main/java/com/shopsphere/rmi/` | Remote interface, `UnicastRemoteObject`, `LocateRegistry` on port 1099, `Naming.rebind`, and remote method execution. |
| **9** | **Object Serialization**| `network/src/main/java/com/shopsphere/network/demo/SerializationDemo.java` | `Serializable`, `serialVersionUID`, and `transient` password field protection. |
| **10**| **JNDI Enterprise Naming**| `META-INF/context.xml`, `web/src/main/webapp/demo/jndi-demo.jsp` | Tomcat managed `jdbc/shopsphereDS` connection pool lookup via `InitialContext().lookup()`. |
| **11**| **Internationalization**| `core/src/main/resources/messages_*.properties`, `LanguageServlet.java` | `Locale`, `ResourceBundle`, `fmt:setLocale`, `fmt:message`, Hindi translations, Indian Rupee currency formatting. |
| **12**| **Servlet Lifecycle** | `web/src/main/java/com/shopsphere/controller/LoginServlet.java` | `init(ServletConfig)`, `service()`, `doGet()`, `doPost()`, `destroy()`, `<load-on-startup>`. |
| **13**| **Filters & Listeners** | `web/src/main/java/com/shopsphere/filter/`, `listener/` | Filter chain (`EncodingFilter`, `SecurityFilter`, `AuthenticationFilter`, `AdminFilter`), `SessionListener`, `AppLifecycleListener`. |
| **14**| **Session Management** | `web/src/main/webapp/demo/session-demo.jsp`, `LoginServlet.java` | `HttpSession`, session fixation defense via `changeSessionId()`, 30-minute idle timeout, client cookies. |
| **15**| **JSP Architecture** | `web/src/main/webapp/demo/jsp-elements.jsp`, `el-demo.jsp` | Declarations (`<%! %>`), scriptlets (`<% %>`), expressions (`<%= %>`), 9 implicit objects, 4-tier EL scope resolution. |
| **16**| **JSTL Tag Libraries** | `web/src/main/webapp/demo/jstl-xml.jsp`, `jstl-sql.jsp` | JSTL Core, Functions, Formatting, XML feed parsing (`<x:parse>`), and direct SQL (`<sql:query>`). |
| **17**| **Custom Tag Handlers** | `web/src/main/java/com/shopsphere/tag/PriceTagHandler.java` | Classic tag extension (`TagSupport`), TLD definition (`shopsphere.tld`), reusable Tag Files (`productCard.tag`). |

*(For the complete 29-topic academic matrix, refer to `docs/SYLLABUS_MAPPING.md`.)*

---

## 13. System Limitations & Future Scope

### 13.1 Current System Limitations
1. **Mock Payment Processing:** Payment transactions simulate real-world card and UPI processing without integrating live commercial payment gateway webhooks (e.g., Razorpay or Stripe).
2. **Synchronous Email Delivery:** Order confirmation receipts and password reset links are logged to the server console rather than dispatched via a live SMTP server.
3. **In-Memory Session Clustering:** `HttpSession` state is maintained in local Tomcat memory; cross-node session replication across clustered servers requires external Redis/Memcached configuration.

### 13.2 Future Enhancements
1. **Live Payment Gateway Webhooks:** Integrate live Razorpay API webhooks for automated payment reconciliation.
2. **Elasticsearch Catalog Indexing:** Replace SQL `LIKE` queries with dedicated inverted-index search for fuzzy search and auto-complete.
3. **Microservices Containerization:** Decouple the monolithic core into independent containerized microservices (Catalog Service, Order Service, Notification Service) orchestrated via Kubernetes.
4. **WebSocket Push Notifications:** Implement real-time order tracking updates pushed directly to the customer's browser via Jakarta WebSocket (`jakarta.websocket`).

---

## 14. Conclusion

The **ShopSphere** project demonstrates that complex, production-ready enterprise applications can be engineered cleanly using fundamental Java standards and Jakarta EE specifications without relying on high-level magic frameworks.

By maintaining strict separation of concerns across presentation, controller, business, and data access tiers, the system achieves exceptional maintainability, testability, and security. Concurrently, by implementing low-level TCP sockets, distributed Java RMI, thread-safe Swing MVC, and rich JSP tag extensions, ShopSphere establishes a complete, reference-grade capstone project that satisfies every academic requirement of the Rajasthan Technical University Advanced Java curriculum.

---

## 15. Academic References & Bibliography

1. **Gosling, J., Joy, B., Steele, G., Bracha, G., Buckley, A., & Smith, D.** (2023). *The Java Language Specification, Java SE 21 Edition*. Oracle Corporation.
2. **Coward, D.** (2022). *Java EE and Jakarta EE: Modern Enterprise Java Development*. O'Reilly Media.
3. **Gamma, E., Helm, R., Johnson, R., & Vlissides, J.** (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
4. **Fowler, M.** (2002). *Patterns of Enterprise Application Architecture*. Addison-Wesley Professional.
5. **Bloch, J.** (2018). *Effective Java (3rd Edition)*. Addison-Wesley Professional.
6. **Apache Software Foundation.** (2025). *Apache Tomcat 11 Architecture and Servlet 6.1 Documentation*. https://tomcat.apache.org/tomcat-11.0-doc/
7. **Eclipse Foundation.** (2024). *Jakarta EE 10 Specifications: Servlet 6.0, Server Pages 3.1, Standard Tag Library 3.0*. https://jakarta.ee/specifications/
8. **Oracle Corporation.** (2023). *Java Remote Method Invocation (RMI) Specification & Tutorial*. Oracle Documentation.
9. **Rajasthan Technical University.** (2024). *Syllabus for B.Tech Computer Science & Engineering, 6th Semester: 6CS4-04 Advanced Java Laboratory*. RTU Kota.
10. **OWASP Foundation.** (2023). *OWASP Top 10 Web Application Security Risks*. https://owasp.org/Top10/
