# ShopSphere — Project Report Screenshots Guide

This guide provides the exact list of screenshots required for your **Rajasthan Technical University (RTU) Advanced Java Project Report / Submission Dossier**, along with recommended filenames, capturing instructions, and descriptive captions.

---

## 1. Screenshot Capture Guidelines
- **Resolution:** Capture at `1920x1080` (or `1440x900` minimum) using standard browser zoom (100%).
- **Browser:** Google Chrome, Microsoft Edge, or Mozilla Firefox.
- **Image Format:** Save all images as PNG (`.png`) for maximum text clarity.
- **Folder Location:** Save all captured image files in this directory: `docs/screenshots/`.
- **Naming Convention:** Use lowercase hyphen-separated filenames matching the table below (e.g., `01-homepage.png`).

---

## 2. Screenshot Checklist & Captions

| # | Filename | Application URL / Screen | Key Visual Elements to Include | Caption for Project Report |
|---|---|---|---|---|
| 1 | `01-homepage.png` | `http://localhost:8080/shopsphere/` | Hero banner ("Enterprise Architecture Meets Modern Retail"), category cards, featured products grid, recently viewed items. | **Figure 11.1:** ShopSphere Storefront Homepage showing dynamic category browsing, featured products, and cookie-based recently viewed items. |
| 2 | `02-catalog-filter.png` | `http://localhost:8080/shopsphere/products?categoryId=1&sort=price_asc` | Faceted sidebar (category selector, price range filter, stock availability), sorted product cards, result counter. | **Figure 11.2:** Product Catalog Browse and Faceted Search interface with price slider, category filters, and sorting. |
| 3 | `03-product-details.png` | `http://localhost:8080/shopsphere/product?id=1` | Product image, SKU, live stock badge, quantity selector, "Add to Cart", "Add to Wishlist", customer ratings, and reviews section. | **Figure 11.3:** Product Details Page displaying high-resolution imagery, real-time stock levels, and user reviews. |
| 4 | `04-shopping-cart.png` | `http://localhost:8080/shopsphere/cart` | Cart line items table, quantity increment/decrement buttons, item subtotals, order summary box (Subtotal, GST 18%, Shipping), "Proceed to Checkout" button. | **Figure 11.4:** Interactive Shopping Cart showing real-time quantity adjustments, line subtotals, and tax calculations. |
| 5 | `05-checkout-coupon.png` | `http://localhost:8080/shopsphere/checkout` | Shipping address selection, promo coupon input (`WELCOME10`), applied discount badge, payment method selector (COD, Card, UPI), "Place Order" button. | **Figure 11.5:** Multi-step Checkout View with address selection, coupon discount application, and payment gateway options. |
| 6 | `06-order-confirmation.png` | `http://localhost:8080/shopsphere/order/details?id=...` | Order number (e.g. `ORD-20261017-...`), status badge (`CONFIRMED`), ordered items table, shipping details, payment receipt. | **Figure 11.6:** Order Confirmation and Invoice view displaying generated order number, itemized breakdown, and delivery address. |
| 7 | `07-order-history.png` | `http://localhost:8080/shopsphere/orders` | Customer order history table, date placed, total amount, status badges (`PENDING`, `CONFIRMED`, `SHIPPED`), and "View Details" links. | **Figure 11.7:** Customer Order History and Status Tracking Console. |
| 8 | `08-user-login.png` | `http://localhost:8080/shopsphere/login` | Login form, demo account quick-fill buttons ("Fill Admin Credentials", "Fill Customer Credentials"), "Remember Me" checkbox, registration link. | **Figure 11.8:** Secure User Authentication Interface with PBKDF2 credential verification and demo autofill helpers. |
| 9 | `09-admin-dashboard.png` | `http://localhost:8080/shopsphere/admin/dashboard` | Metric cards (Total Revenue, Total Orders, Active Products, Registered Customers), recent orders table, low-stock alerts panel. | **Figure 11.9:** Web Administration Dashboard displaying key performance metrics, order volume, and inventory warnings. |
| 10 | `10-admin-products.png` | `http://localhost:8080/shopsphere/admin/products` | Admin product management table, SKU, price, stock, active switch, "Add New Product" modal/form, edit and deactivate buttons. | **Figure 11.10:** Product Inventory CRUD Management Console in Web Admin. |
| 11 | `11-admin-orders.png` | `http://localhost:8080/shopsphere/admin/orders` | Order management table, customer names, status dropdown (`PENDING` -> `SHIPPED` -> `DELIVERED`), action buttons. | **Figure 11.11:** Order Processing Console allowing administrative status transitions and audit tracking. |
| 12 | `12-demo-hub.png` | `http://localhost:8080/shopsphere/demo/` | Syllabus Demo Hub card grid (JSP Elements, EL Scopes, JSTL XML, JSTL SQL, JNDI DataSource, i18n English/Hindi, Session Inspector). | **Figure 11.12:** RTU Syllabus Academic Demonstration Hub showcasing all Jakarta EE curriculum topics. |
| 13 | `13-jsp-elements-demo.png` | `http://localhost:8080/shopsphere/demo/jsp-elements.jsp` | Page displaying declarations (`<%! %>`), scriptlets (`<% %>`), expressions (`<%= %>`), page directives, and 9 implicit objects table. | **Figure 11.13:** JSP Traditional Elements and Implicit Objects demonstration view. |
| 14 | `14-i18n-hindi.png` | `http://localhost:8080/shopsphere/lang?set=hi` | Storefront navbar and headings rendered in Hindi (हिंदी): "होम", "उत्पाद", "कार्ट", "साइन इन", formatted Indian Rupee (₹) prices. | **Figure 11.14:** Multi-language Internationalization (i18n) showing dynamic switching to Hindi via ResourceBundle. |
| 15 | `15-swing-admin-login.png` | Swing Desktop App | `LoginFrame` window with Username, Password, "Show Password" checkbox, "Login" button, and Pluggable Look and Feel. | **Figure 11.15:** Java Swing Desktop Administration Login Window executing background authentication off the EDT. |
| 16 | `16-swing-admin-dashboard.png`| Swing Desktop App | `AdminDashboard` with `JMenuBar` (File, Look & Feel, Help), `JTabbedPane` tabs (Products, Categories, Customers, Orders, Inventory), and `JTable` data. | **Figure 11.16:** Java Swing Desktop Admin Console implementing MVC with custom `AbstractTableModel` and runtime PLAF switching. |
| 17 | `17-socket-terminal.png` | Command Prompt / PowerShell | Dual terminal view showing `InventoryServer` listening on TCP port 5000 and `InventoryClient` sending commands and receiving responses. | **Figure 11.17:** Multithreaded TCP Socket Server and Client terminal session executing low-level inventory protocol commands. |
| 18 | `18-rmi-terminal.png` | Command Prompt / PowerShell | Dual terminal view showing `RmiServer` binding to port 1099 and `RmiClient` invoking remote methods over RMI. | **Figure 11.18:** Java Remote Method Invocation (RMI) distributed client-server terminal session. |

---

## 3. How to Capture & Embed in LaTeX / Word
When compiling your final project report:
1. Place the generated images in `docs/screenshots/` (e.g. `docs/screenshots/01-homepage.png`).
2. If using LaTeX:
   ```latex
   \begin{figure}[H]
       \centering
       \includegraphics[width=0.9\textwidth]{screenshots/01-homepage.png}
       \caption{ShopSphere Storefront Homepage}
       \label{fig:homepage}
   \end{figure}
   ```
3. If using Microsoft Word / Google Docs:
   - Insert picture &rarr; select PNG file from `docs/screenshots/`
   - Right-click picture &rarr; Add Caption &rarr; Paste the corresponding caption from the table above.
