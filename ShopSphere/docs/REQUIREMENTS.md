# ShopSphere — System Requirements Specification (SRS)

## 1. Introduction & Overview
ShopSphere is an end-to-end Enterprise Java E-Commerce Management System built using Jakarta Servlet 6.1, JSP 3.1, JSTL, MySQL 8+, Java Swing, and distributed Java technologies (Socket & RMI). It serves as a production-grade blueprint and an academic capstone project demonstrating the complete RTU Advanced Java syllabus.

---

## 2. Functional Requirements

### 2.1 Customer Module
- **FR-C01: User Registration:**
  - Prospective customers can register with full name, email, mobile number, and password.
  - Email uniqueness must be strictly enforced on both application and database layers.
  - Password strength validation: minimum 6 characters.
- **FR-C02: Authentication & Session Management:**
  - Secure login with email and salted hashed password.
  - Session creation upon successful authentication; session invalidation on logout.
  - Session fixation protection (session regenerated on login).
- **FR-C03: Product Catalog & Browsing:**
  - Public browsing of categorized products on home and catalog pages.
  - Display product cards with title, brand, image, actual price, discount percentage, final price, star rating, and stock availability.
- **FR-C04: Search, Filter & Sort:**
  - Keyword search across product title and description.
  - Filter by category and price range (min/max price).
  - Sort by Price: Low to High, Price: High to Low, Rating: High to Low, and Newest Arrivals.
  - Server-side pagination with configurable page size.
- **FR-C05: Product Details & Reviews:**
  - Detailed product page with comprehensive description, stock state, category hierarchy, and verified customer reviews.
  - Ability for authenticated customers who purchased the item to leave ratings (1 to 5 stars) and comments.
- **FR-C06: Shopping Cart Management:**
  - Authenticated shopping cart persistent across user sessions in database.
  - Add item, update item quantity (with stock boundary validation: $1 \le qty \le stock$), remove line item, clear entire cart.
  - Dynamic navbar cart item count.
- **FR-C07: Wishlist Management:**
  - Add to wishlist, view saved items, delete item, and direct "Move to Cart".
- **FR-C08: Address Book:**
  - Manage multiple delivery addresses (Full Name, Street, City, State, Postal Code, Phone Number).
  - Set default shipping address.
- **FR-C09: Coupon System:**
  - Apply coupon code at checkout (supports Flat discount or Percentage discount).
  - Validation: active status, expiry date, minimum cart order value, maximum discount cap, and single-use per customer limit (`coupon_usage`).
- **FR-C10: Checkout & Order Placement:**
  - Address selection/addition.
  - Payment method selection: Cash on Delivery (COD), Mock Card, Mock UPI.
  - Server-side recalculation of subtotal, product discount, coupon discount, delivery fee, and net payable.
  - Atomic transaction: order created, order items recorded, stock decremented, coupon marked used, cart cleared.
- **FR-C11: Order Tracking & History:**
  - Customer order list with order ID, date, total amount, payment status, and order status.
  - Order details view with itemized invoice and visual status timeline: `PLACED` $\to$ `CONFIRMED` $\to$ `SHIPPED` $\to$ `DELIVERED` (or `CANCELLED`).
  - Order cancellation option available if order has not yet shipped, triggering automatic stock restoration.
- **FR-C12: User Profile Management:**
  - View and update personal profile details (name, phone) and change password.

### 2.2 Administrator Module (Web & Swing)
- **FR-A01: Admin Authentication:**
  - Role-based authorization (`ADMIN` role check). Non-admins redirected or denied access.
- **FR-A02: Dashboard & Analytics:**
  - Key business metrics: Total Sales Revenue, Total Orders Placed, Registered Customers count, Low-Stock alerts count, and Active HTTP Sessions count.
  - Recent orders overview and breakdown by category.
- **FR-A03: Product Management (CRUD):**
  - Add new product (title, category, brand, price, discount, stock, image URL, description).
  - Edit existing product details and pricing.
  - Soft delete products (`status = 'INACTIVE'`).
  - Dedicated stock adjustment view.
- **FR-A04: Category Management (CRUD):**
  - Create, view, update, and manage categories.
- **FR-A05: Order Management:**
  - Comprehensive order list with filtering by status.
  - View order details and update order lifecycle status (`PLACED` $\to$ `CONFIRMED` $\to$ `SHIPPED` $\to$ `DELIVERED` $\to$ `CANCELLED`).
  - Update payment status (`PENDING` $\to$ `PAID` $\to$ `FAILED`).
- **FR-A06: Customer Management:**
  - View all registered customers, registration dates, and toggle customer active/inactive status.
- **FR-A07: Coupon Management:**
  - Create and manage coupons: coupon code, discount type (`PERCENTAGE`/`FLAT`), discount value, min order amount, max discount cap, start date, expiry date, and status.
- **FR-A08: Inventory & Low-Stock Alerts:**
  - Dedicated low-stock filter for products with stock $\le$ threshold (default 5 units).

---

## 3. Non-Functional Requirements (NFR)

- **NFR-01: Security:**
  - Password Hashing: PBKDF2WithHmacSHA256 with cryptographically random salt. No plain passwords stored.
  - SQL Injection Prevention: 100% parameterization via `java.sql.PreparedStatement`. No string concatenation in queries.
  - Cross-Site Scripting (XSS) Prevention: All user-supplied inputs escaped via `<c:out>` and JSTL functions.
  - Session Security: `HttpOnly` and `SameSite` cookies; session invalidation on login/logout.
  - Access Control: Role-based filters (`AuthenticationFilter`, `AdminFilter`) guarding protected URLs.
  - State Tampering Prevention: Prices and order totals strictly recalculated on server side during checkout.
- **NFR-02: Responsiveness & UI/UX:**
  - Clean responsive web design using Bootstrap 5 and modern CSS3, compatible with desktop and mobile viewports.
  - Rich desktop UI with Swing MVC, pluggable look-and-feel (System, Metal, Nimbus).
- **NFR-03: Performance & Scalability:**
  - Database indexes on foreign keys and high-frequency search fields (`products.name`, `products.category_id`, `orders.user_id`).
  - Connection management using clean `try-with-resources`.
  - Background task execution in Swing via `SwingWorker` to prevent freezing Event Dispatch Thread (EDT).
- **NFR-04: Maintainability & N-Tier Architecture:**
  - Strict separation: View (JSP/Swing) $\to$ Controller (Servlet/SwingController) $\to$ Service $\to$ DAO $\to$ Database.
  - Clean multi-module Maven structure with shared `core` business code.
