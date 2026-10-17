# ShopSphere — Screen Specifications & Wireframes

This document details the interface architecture, layout structure, data bindings, and wireframes for all views in the ShopSphere application.

---

## 1. Customer & Public Screens

### 1.1 Home Screen (`index.jsp`)
- **Route:** `/` or `/index.jsp`
- **Data Displayed:** Hero promotional banner, Featured categories carousel, Top-rated and new arrivals product grid, Recently viewed products (from client cookie), Customer testimonials.
- **Wireframe:**
```
+-----------------------------------------------------------------------+
| [Logo: ShopSphere]   [ Search Products... ]   [Cart (3)] [Login / User]|
+-----------------------------------------------------------------------+
|  HERO BANNER: Summer Electronics Sale - Up to 40% Off!  [Shop Now]    |
+-----------------------------------------------------------------------+
|  FEATURED CATEGORIES: [Electronics] [Fashion] [Home] [Books] [Sports] |
+-----------------------------------------------------------------------+
|  FEATURED PRODUCTS                                                    |
|  +----------------+  +----------------+  +----------------+           |
|  | [Product Img]  |  | [Product Img]  |  | [Product Img]  |           |
|  | Smartphone Pro |  | Wireless Buds  |  | Gaming Laptop  |           |
|  | Rs. 24,999     |  | Rs. 2,499      |  | Rs. 65,000     |           |
|  | [Add to Cart]  |  | [Add to Cart]  |  | [Out of Stock] |           |
|  +----------------+  +----------------+  +----------------+           |
+-----------------------------------------------------------------------+
|  RECENTLY VIEWED PRODUCTS (Cookie-backed)                             |
|  [Item 1] [Item 2] [Item 3]                                           |
+-----------------------------------------------------------------------+
| FOOTER: Links, RTU Project Info, Language Switcher (EN | HI)           |
+-----------------------------------------------------------------------+
```

### 1.2 Product Catalog Screen (`products.jsp`)
- **Route:** `/products?q=...&category=...&minPrice=...&maxPrice=...&sort=...&page=...`
- **Data Displayed:** Faceted filter sidebar (Category checkboxes, Price range slider/inputs), Sort dropdown, Search result count, Paginated product cards with discounts, ratings, and stock badges.
- **Wireframe:**
```
+-----------------------------------------------------------------------+
| FILTERS                   | CATALOG GRID (Showing 1-12 of 35 items)    |
| Category:                 | Sort by: [ Price: Low to High       v ]   |
| [ ] Laptops               |-------------------------------------------|
| [x] Mobiles               | +--------------+ +--------------+         |
| [ ] Audio                 | | [Img] -15%   | | [Img] -10%   |         |
| Price:                    | | Galaxy S21   | | iPhone 14    |         |
| [Rs. 1000] - [Rs. 50000]  | | Rs. 42,999   | | Rs. 69,999   |         |
| Rating:                   | | **** (4.5)   | | ***** (4.8)  |         |
| [x] 4 Stars & Above       | | [Add to Cart]| | [Add to Cart]|         |
| [Apply Filters]           | +--------------+ +--------------+         |
|                           | [Pagination: < Prev | 1 | 2 | 3 | Next >] |
+-----------------------------------------------------------------------+
```

### 1.3 Product Details Screen (`product-details.jsp`)
- **Route:** `/product?id=...`
- **Data Displayed:** High-res product image gallery, title, brand, category breadcrumb, rating summary, pricing with discount breakdown, stock status, quantity picker, "Add to Cart" and "Add to Wishlist" actions, detailed specifications, customer reviews list, review submission form (for verified buyers).
- **Wireframe:**
```
+-----------------------------------------------------------------------+
| Home > Mobiles > Smartphone Pro 5G                                    |
+-----------------------------------------------------------------------+
| [ Large Image ]        | Smartphone Pro 5G - 128GB Midnight Black      |
|                        | Brand: TechGiant | Rating: **** (4.2/5)       |
|                        | Price: Rs. 24,999 (MRP Rs. 29,999 - Save 17%)|
|                        | Status: IN STOCK (14 left)                    |
|                        | Quantity: [ - | 1 | + ]                       |
|                        | [ + Add to Cart ]   [ <3 Save to Wishlist ]   |
|                        |-----------------------------------------------|
|                        | Description: High-performance 5G smartphone...|
+-----------------------------------------------------------------------+
| CUSTOMER REVIEWS (3)                   | WRITE A REVIEW (Verified only)|
| ***** "Outstanding battery life!"      | Your Rating: [ * * * * * ]    |
| ****  "Camera quality is superb."      | Comment: [                 ]  |
|                                        | [ Submit Review ]             |
+-----------------------------------------------------------------------+
| RELATED PRODUCTS IN MOBILES: [Card 1] [Card 2] [Card 3]               |
+-----------------------------------------------------------------------+
```

### 1.4 Cart Screen (`cart.jsp`)
- **Route:** `/cart`
- **Data Displayed:** Line items with thumbnail, title, unit price, quantity increment/decrement controls, line subtotal, remove item action, Order Summary sidebar (Subtotal, Discount, Estimated Tax, Shipping, Grand Total), "Proceed to Checkout" button, "Continue Shopping" link.

### 1.5 Wishlist Screen (`wishlist.jsp`)
- **Route:** `/wishlist`
- **Data Displayed:** Grid of saved products, current price, stock status, "Move to Cart" button, "Remove" button.

### 1.6 Checkout Screen (`checkout.jsp`)
- **Route:** `/checkout`
- **Data Displayed:** Delivery address selector (or new address form), Coupon code input with "Apply" button and instant server-side discount feedback, Payment method options (Cash on Delivery, Mock Credit/Debit Card, Mock UPI), Itemized review summary with strictly server-calculated totals.

### 1.7 Order Success & Tracking (`order-success.jsp` & `order-details.jsp`)
- **Route:** `/order-success?orderId=...`, `/order-details?id=...`
- **Data Displayed:** Order ID, date placed, delivery address, payment method and reference, items table, and **Visual Order Progress Timeline**:
  `[x] Placed` $\longrightarrow$ `[x] Confirmed` $\longrightarrow$ `[ ] Shipped` $\longrightarrow$ `[ ] Delivered`.

### 1.8 Orders List & Profile (`orders.jsp` & `profile.jsp`)
- **Route:** `/orders`, `/profile`
- **Data Displayed:** Historical orders table with "View Invoice" and "Cancel" buttons; user profile details form, change password modal, and saved address book editor.

---

## 2. Administrator Screens

### 2.1 Admin Dashboard (`admin/dashboard.jsp`)
- **Route:** `/admin/dashboard`
- **Data Displayed:**
  - KPI Stat Cards: Total Revenue, Total Orders, Total Customers, Low-Stock Alerts, Active HTTP Sessions.
  - Quick action buttons: Add Product, Manage Orders, Create Coupon.
  - Recent orders table with instant status badge and action link.
  - Category-wise sales distribution breakdown.

### 2.2 Product Management (`admin/products.jsp`)
- **Route:** `/admin/products`
- **Data Displayed:** Searchable/filterable product table (ID, Image, Name, Category, Price, Stock, Status, Actions: Edit/Stock/Delete). Modal dialogs for "Add Product" and "Edit Product" with server-side validation.

### 2.3 Category Management (`admin/categories.jsp`)
- **Route:** `/admin/categories`
- **Data Displayed:** Category list, product count per category, Add/Edit category dialogs.

### 2.4 Order Management (`admin/orders.jsp`)
- **Route:** `/admin/orders`
- **Data Displayed:** All customer orders with status filters (ALL, PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED). Dropdown to update lifecycle status and payment status in real-time.

### 2.5 Customer & Coupon Management (`admin/users.jsp` & `admin/coupons.jsp`)
- **Route:** `/admin/users`, `/admin/coupons`
- **Data Displayed:** User table with active/inactive toggle switch; Coupon table with code, discount type, value, minimum order, validity dates, usage count, and active status.

---

## 3. Syllabus Demonstration Screens (`/demo/*`)
- **`/demo/index.jsp`**: Navigation hub to all syllabus live demos.
- **`/demo/jsp-elements.jsp`**: Demonstrates `<%! %>` declarations, `<% %>` scriptlets, `<%= %>` expressions, directives, comments, implicit objects, and page instance reuse proof.
- **`/demo/el-demo.jsp`**: Shows Expression Language operators, scopes (`pageScope`, `requestScope`, `sessionScope`, `applicationScope`), and JSTL EL functions.
- **`/demo/jstl-xml.jsp`**: Parses and renders product feeds using JSTL XML tags (`x:parse`, `x:forEach`, `x:out`).
- **`/demo/jstl-sql.jsp`**: Demonstrates `sql:query` and `sql:setDataSource` with clear disclaimer.
- **`/demo/jndi-demo.jsp`**: Demonstrates JNDI lookup of Tomcat DataSource (`java:comp/env/jdbc/shopsphereDS`).
