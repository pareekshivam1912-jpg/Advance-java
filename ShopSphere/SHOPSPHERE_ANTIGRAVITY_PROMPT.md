# SHOPSPHERE — Phase-Wise Build Prompt for Antigravity

> **How to use this file**
> 1. Open an empty folder named `ShopSphere` in Antigravity.
> 2. Paste **everything from "PART A" down to the end of "PART B"** into the agent as the first message (or attach this file and say: *"Read SHOPSPHERE_ANTIGRAVITY_PROMPT.md fully and execute it phase by phase."*).
> 3. Default mode is **autonomous**: the agent runs Phase 0 → Phase 18, verifying each phase before moving on.
> 4. If you prefer manual control, say: *"Execute only Phase N, then stop and report."* and paste phases one at a time.

---

# PART A — MASTER CONTEXT (applies to every phase)

## A1. Your role
You are a senior Java enterprise engineer and technical mentor. Build **ShopSphere**, a complete Java E-Commerce Management System that doubles as an **RTU (Rajasthan Technical University) Advanced Java final practical project**. The project must be fully working, runnable from a clean machine, and structured so a student can explain every part in a viva.

## A2. What you are building
One coherent application that demonstrates the whole RTU Advanced Java syllabus:

- **Web customer + admin app** — Jakarta Servlet + JSP on Apache Tomcat 11 (the main product)
- **Swing desktop admin app** — Swing MVC with JDBC
- **Socket networking module** — `ServerSocket` / `Socket` inventory client-server with serialization
- **RMI module** — remote inventory service with RMI registry
- **Small demos** — JNDI lookup, URL/URLConnection, Internationalization, Applet concept, JSTL XML, JSTL SQL

### Syllabus topics that must be visibly covered
Swing, MVC, Applets (concept only), JDBC, java.net, Client-Server, RMI, RMI Registry, Naming/JNDI, Serialization, Internationalization, J2EE architecture, N-tier, HTTP, Web Containers, Servlets (lifecycle, ServletConfig, ServletContext), Sessions, Cookies, Event listeners, Filters, JSP (architecture, lifecycle, traditional elements), EL, Tag Extensions/Handlers, JSP Fragments, Tag Files, JSTL (Core, XML, SQL, Functions, and fmt for i18n).

## A3. Fixed technology stack (do not substitute)
| Layer | Technology |
|---|---|
| Language | Java 17 or higher (Tomcat 11 requires Java 17+) |
| Web | Jakarta Servlet 6.1 + Jakarta Pages (JSP) — **`jakarta.*` namespace only, never `javax.*`** |
| View | JSP + EL + JSTL + HTML5 + CSS3 + JavaScript + Bootstrap 5 |
| Database | MySQL 8 |
| DB access | JDBC with MySQL Connector/J (`PreparedStatement` only) |
| Build | Maven (multi-module) |
| Server | Apache Tomcat 11 |
| Desktop | Java Swing |
| Networking | `java.net` Socket / ServerSocket |
| Distributed | Java RMI |

## A4. Architecture rules (non-negotiable)
Web request flow must always be:

```
Browser → JSP/HTML/JS → Servlet Controller → Service Layer → DAO Layer → JDBC → MySQL
```

- **No SQL and no business logic inside JSP files.** JSPs only render (EL + JSTL).
- **Business rules live in the Service layer** (stock checks, coupon validation, total calculation, order placement).
- **Servlets** only parse the request, call services, set request/session attributes, and forward/redirect.
- Swing app and RMI server **reuse the same Service/DAO layer** — do not duplicate DAO code.
- Follow **Post/Redirect/Get** for all state-changing forms.

## A5. Maven multi-module layout
Use a parent POM with these modules (this is the guide's structure plus one shared `core` module so DAO/Service code is not duplicated):

```
ShopSphere/
├── pom.xml                      (parent, packaging=pom)
├── README.md
├── database/
│   ├── shopsphere.sql           (schema + seed data)
│   └── er-diagram.md
├── docs/                        (diagrams, report, viva, syllabus mapping, setup)
├── core/                        (jar)  com.shopsphere.model / dao / service / util
├── web/                         (war)  com.shopsphere.controller / filter / listener + webapp
├── swing-admin/                 (jar)  model / view / controller (uses core DAO+service)
├── network/                     (jar)  server / client / model (Socket demo)
└── rmi/                         (jar)  remote / server / client
```

Base package: `com.shopsphere`. Sub-packages: `model`, `dao`, `service`, `util`, `controller`, `filter`, `listener`.

## A6. Coding standards
- Java 17, 4-space indent, meaningful names, Javadoc on every public class and a short comment on non-obvious logic.
- Every DAO method uses `try-with-resources` for `Connection`, `PreparedStatement`, `ResultSet`.
- Custom `ShopSphereException` (unchecked) for service-level errors; no swallowed exceptions.
- DB config read from `db.properties` (host, port, db, user, password) — never hard-code credentials in Java.
- Passwords: **salted hash** (PBKDF2WithHmacSHA256 from the JDK, or BCrypt). Never store plaintext; never put passwords in cookies.
- Money: use `BigDecimal` in DB/service math if possible; if the guide's `double` is used in models, round to 2 decimals consistently.
- Server-side validation for every form field. Order totals are **always recomputed on the server** — never trust browser-submitted totals.
- Escape output (`<c:out>` or `fn:escapeXml`) to prevent XSS. Add a CSRF token to state-changing forms.

## A7. Working protocol for the agent
1. **Before coding**, create `PROGRESS.md` with a checklist of all 19 phases and keep it updated.
2. **Check the environment first** (Phase 0): Java version, Maven, MySQL availability, Tomcat 11. If MySQL or Tomcat is missing, try Docker; if that is also unavailable, write exact install steps into `docs/SETUP.md`, still write all code, and mark runtime verification as "pending user environment" in `PROGRESS.md`. **Never fake test results.**
3. **After each phase**: build (`mvn -q clean package` for affected modules), run what can be run, fix errors, then append a short `Phase N — DONE` note (what was built, how verified, files created) to `PROGRESS.md`.
4. **Do not move to the next phase** until the current phase's *Acceptance criteria* pass.
5. **Do not skip syllabus items** — every row in the mapping table (Phase 17) must point to real code.
6. If a requirement is ambiguous, choose the simplest option consistent with the guide, and record the decision in `docs/DECISIONS.md`.
7. Keep the **Minimum Working Version first**: Phases 1–12 deliver the complete core shopping flow; Phases 13–17 add the extra syllabus modules.

---

# PART B — PHASES

---

## PHASE 0 — Environment check and project scaffolding

**Goal:** A compiling empty multi-module Maven skeleton.

**Tasks**
1. Verify `java -version` (≥17), `mvn -v`, and MySQL reachability. Check for Tomcat 11 (or plan Maven-based/Docker deployment). Record findings in `docs/SETUP.md`.
2. Create the folder structure from section A5, the parent `pom.xml` (modules: `core`, `web`, `swing-admin`, `network`, `rmi`), and a `pom.xml` per module.
   - `core`: dependency `mysql-connector-j`.
   - `web`: `<packaging>war</packaging>`; dependencies: `jakarta.servlet:jakarta.servlet-api:6.1.0` (scope `provided`), `jakarta.servlet.jsp:jakarta.servlet.jsp-api` (provided), JSTL API + implementation (`jakarta.servlet.jsp.jstl` 3.x API and its Glassfish implementation), and the `core` module.
   - `swing-admin`, `network`, `rmi`: depend on `core`.
3. Create `PROGRESS.md`, `README.md` (stub), `docs/DECISIONS.md`, `.gitignore`.
4. Create `core/src/main/resources/db.properties` (with a `db.properties.example` committed instead of real secrets).

**Acceptance criteria**
- `mvn -q clean package` succeeds from the root with all modules (empty is fine).
- `PROGRESS.md` exists with the 19-phase checklist.

---

## PHASE 1 — Requirements and UI design

**Goal:** Planning documents that guide all later work.

**Tasks** — create in `docs/`:
1. `REQUIREMENTS.md`: functional requirements for Customer module (registration, login/logout, browsing, search/filter/sort, details, cart, wishlist, address management, coupons, checkout, mock payment, order placement, order history/tracking, profile, reviews/ratings) and Admin module (login, dashboard, product CRUD, category CRUD, inventory, customer list, order management, coupon management, low-stock view, basic sales summary). Add non-functional requirements (security, responsiveness, performance).
2. `USE_CASES.md`: actors (Guest, Customer, Admin) with a **Mermaid** use-case style diagram and a table of use cases.
3. `SCREENS.md`: every page from the guide's page table with a wireframe description (ASCII or Mermaid) and the data each shows.
4. `MODULES.md`: module list and responsibilities.

**Acceptance criteria**
- All four files exist and cover every guide feature with no feature missing.

---

## PHASE 2 — Database design

**Goal:** A complete MySQL schema with realistic seed data.

**Tasks**
1. Write `database/shopsphere.sql`:
   - `CREATE DATABASE IF NOT EXISTS shopsphere;` `USE shopsphere;` utf8mb4.
   - Tables and columns **exactly as in the guide**: `users`, `addresses`, `categories`, `products`, `cart`, `wishlist`, `orders`, `order_items`, `payments`, `reviews`, `coupons`, `coupon_usage`.
   - Proper PKs (`AUTO_INCREMENT`), FKs with sensible `ON DELETE` behavior, `UNIQUE(email)` on users, `UNIQUE(user_id, product_id)` on cart and wishlist, `UNIQUE(code)` on coupons, `CHECK` for `rating BETWEEN 1 AND 5`, `stock >= 0`, `price >= 0`.
   - Indexes on foreign keys and on `products(name)`, `products(category_id)`, `orders(user_id)`.
   - `users.role` ∈ (`CUSTOMER`, `ADMIN`); `orders.order_status` ∈ (`PLACED`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`); `payment_status` ∈ (`PENDING`, `PAID`, `FAILED`).
2. Seed data: 1 admin, 3 customers, 6 categories, **at least 30 products** (with brand, price, discount, stock, image URLs using placeholder images or local `images/` paths), 3 coupons (percentage and flat, with minimum order and max discount), a few reviews and sample orders.
   - Generate real password hashes using the same hashing algorithm the app will use (implement `PasswordUtil` in `core` first if needed). Document default credentials in `docs/SETUP.md` (e.g. `admin@shopsphere.com` / `Admin@123`, `customer@shopsphere.com` / `Customer@123`).
3. `database/er-diagram.md`: ER diagram in Mermaid (`erDiagram`) matching the guide's relationships.
4. If MySQL is available, actually import the script and verify table/row counts.

**Acceptance criteria**
- Script runs on a fresh MySQL without errors (or is syntax-checked and marked pending).
- ER diagram renders and matches the tables.

---

## PHASE 3 — Models and JDBC (DAO layer)

**Goal:** POJOs and CRUD DAOs in `core`.

**Tasks**
1. `com.shopsphere.model`: `User, Address, Category, Product, CartItem, WishlistItem, Order, OrderItem, Payment, Review, Coupon` — private fields, constructors, getters/setters, `toString`. `Product` and `Order`/`OrderItem` **implement `Serializable`** with `serialVersionUID` (needed for Phase 14/15).
2. `com.shopsphere.util.DBUtil`: loads `db.properties`, `getConnection()` via `DriverManager` (this is the syllabus-visible JDBC driver usage).
3. DAO interfaces + JDBC implementations: `UserDAO, AddressDAO, CategoryDAO, ProductDAO, CartDAO, WishlistDAO, OrderDAO, OrderItemDAO, PaymentDAO, ReviewDAO, CouponDAO`.
   - Every query via `PreparedStatement` with placeholders. No string-concatenated SQL.
   - `ProductDAO`: `findById`, `findAll`, `search(keyword, categoryId, minPrice, maxPrice, sortBy, page, size)`, `insert`, `update`, `delete` (soft delete via `status`), `updateStock`, `findLowStock(threshold)`.
   - `OrderDAO`: methods that accept a `Connection` so the service layer can run **one transaction** for order placement (commit/rollback).
4. `PasswordUtil` (hash + verify with salt).
5. Add JUnit 5 tests for at least `UserDAO` and `ProductDAO` (use a test database or skip with a clear message if DB is unavailable).

**Acceptance criteria**
- `core` compiles; DAO CRUD verified against the DB (or clearly marked pending).
- grep confirms **no SQL string concatenation** with user input.

---

## PHASE 4 — Service layer and MVC structure

**Goal:** All business logic centralized in `core/service`.

**Tasks**
1. Services: `UserService` (register/authenticate), `ProductService`, `CategoryService`, `CartService`, `WishlistService`, `AddressService`, `CouponService`, `OrderService`, `CheckoutService`, `ReviewService`, `InventoryService` (local, not RMI), `ReportService` (sales summary).
2. Business rules:
   - Registration: unique email, password strength, email/mobile format.
   - Cart: quantity ≥ 1, cannot exceed stock.
   - Coupon: active, not expired, meets minimum order, respects maximum discount, one use per user per coupon (`coupon_usage`).
   - Checkout: recompute subtotal from DB prices (apply product discount), apply coupon, compute final total — **server-side only**.
   - Order placement in a **single DB transaction**: create order, order items, payment record (mock), decrement stock, record coupon usage, clear cart; rollback on any failure.
   - Reviews: only one review per user per product.
3. Provide a `ServiceFactory` (simple static wiring — no framework) so Servlets and Swing controllers get services easily.
4. Document the N-tier flow in `docs/ARCHITECTURE.md` with Mermaid diagrams (overall architecture, N-tier web flow, Swing MVC).
5. Unit tests (JUnit 5, mocks or in-memory fakes) for coupon calculation and checkout total logic.

**Acceptance criteria**
- Service tests pass.
- No servlet/JSP class is referenced from `core` (core stays UI-independent).

---

## PHASE 5 — Web foundation and authentication

**Goal:** Working registration/login/logout with sessions.

**Tasks**
1. Web module skeleton: `webapp/WEB-INF/web.xml` (welcome file, session timeout, error pages), `css/`, `js/`, `images/`.
2. Shared layout via fragments (preview of Phase 12): `fragments/header.jsp`, `navbar.jsp`, `footer.jsp`, `sidebar.jsp` using Bootstrap 5 (responsive navbar with search box, cart icon + counter, login/profile/logout links based on session).
3. `RegisterServlet`, `LoginServlet`, `LogoutServlet`; pages `register.jsp`, `login.jsp` (customer and admin can log in; redirect by role: ADMIN → `/admin/dashboard`, CUSTOMER → home).
4. Session handling: store `user` and `role` in `HttpSession`; **invalidate and recreate the session on login** (session-fixation protection); `LogoutServlet` invalidates the session.
5. Flash messages (success/error alerts) via session attribute removed after display.
6. "Remember me"/recently-viewed cookie is **not** stored here — cookies come in Phase 11. Never store passwords in cookies.
7. Show the **Servlet lifecycle** in `LoginServlet`: implement `init()` (log + read an init-param via `ServletConfig`), `doGet`, `doPost`, `destroy()` with clear log messages.

**Acceptance criteria**
- Deploy WAR on Tomcat 11; register a new user, log in, see name in navbar, log out.
- Duplicate email and wrong password show errors.

---

## PHASE 6 — Products (listing, search, filter, details)

**Goal:** Full catalogue browsing.

**Tasks**
1. `index.jsp` home: banner, featured categories, featured/new products.
2. `ProductServlet` (mapped to `/products` and `/product`): listing with **search keyword, category filter, price range, sort (price asc/desc, newest, rating), pagination**.
3. `products.jsp` and `product-details.jsp` using **EL + JSTL Core** only (`c:forEach`, `c:if`, `c:choose`, `c:set`, `c:url`, `c:out`) and `fmt` for prices/dates.
4. Product cards show image, name, brand, price, strike-through original price and discount badge, star rating, stock state ("Out of stock" disables Add to Cart).
5. Product details: description, reviews list, average rating, related products (same category).

**Acceptance criteria**
- Searching "phone" (or any seeded term) returns only matching products; filters/sort/pagination work and persist in query string.
- No Java code (`<% %>`) in these JSPs.

---

## PHASE 7 — Cart and Wishlist

**Goal:** Authenticated cart and wishlist.

**Tasks**
1. `CartServlet`: add, update quantity, remove, clear. Cart persisted in the `cart` table for logged-in users.
2. `cart.jsp`: line items, quantity controls, per-line and grand totals (discounts shown), empty-cart state, "Proceed to checkout".
3. Navbar cart counter updates from the server (session attribute or request-scoped service call).
4. `WishlistServlet` + `wishlist.jsp`: add/remove, "Move to cart".
5. Unauthenticated users clicking Add to Cart are redirected to login and returned to their intended page after login.

**Acceptance criteria**
- Add → change quantity → totals recalculate; quantity above stock is rejected with a message; wishlist works.

---

## PHASE 8 — Checkout, coupons, mock payment, orders

**Goal:** End-to-end purchase flow.

**Tasks**
1. `CheckoutServlet` + `checkout.jsp`: select/add address, apply coupon code, choose payment method (COD, Mock Card, Mock UPI), order summary.
2. Address management inside checkout and in `profile.jsp` (add/edit/delete).
3. Mock payment: simulate success for card/UPI (generate `transaction_reference`), COD stays `PENDING`. No real gateway.
4. `CheckoutService.placeOrder(...)` from Phase 4 is the only place orders get created.
5. `order-success.jsp` (order id, summary), `orders.jsp` (history), `order-details.jsp` (items, address, payment, **status timeline** PLACED → CONFIRMED → SHIPPED → DELIVERED, cancelled state).
6. `OrderServlet`: list, details, cancel (only if status allows; restores stock).
7. `ReviewServlet`: add a review/rating on products the user has purchased; update product average rating display.
8. `profile.jsp`: view/edit name, mobile, change password, manage addresses.

**Acceptance criteria**
- Full flow works: login → cart → checkout with coupon → order created → stock decreased → cart emptied → order visible in history.
- Tampering with a hidden "total" form field has **no effect** (verify and note in `PROGRESS.md`).

---

## PHASE 9 — Admin module (web)

**Goal:** Complete admin panel.

**Tasks**
1. `admin/dashboard.jsp`: cards for total sales, orders, customers, low-stock products; recent orders table; simple sales summary (by day/category).
2. `AdminProductServlet` + `admin/products.jsp`: list, add, edit, delete (soft), stock update, image URL/upload (upload optional; URL field is acceptable).
3. `AdminCategoryServlet` + `admin/categories.jsp`: CRUD.
4. `AdminOrderServlet` + `admin/orders.jsp`: filter by status, view details, **update order status** (customer sees the new status), payment status update.
5. `AdminUserServlet` + `admin/users.jsp`: customer list, activate/deactivate.
6. Coupon management page (`admin/coupons.jsp` + servlet): create/edit/disable coupons.
7. Low-stock view and inventory adjustment page.
8. `AdminLoginServlet`: optional dedicated admin login that reuses the same authentication service.

**Acceptance criteria**
- Admin can add a product and it immediately appears in the customer catalogue; changing an order's status is reflected on the customer's order page.

---

## PHASE 10 — Servlet/JSP polish: ServletConfig, ServletContext, JSP elements, EL

**Goal:** Make the syllabus-specific servlet/JSP concepts explicit (without hurting the main app).

**Tasks**
1. `ServletContext`: set `appName`, `appVersion`, and shared config attributes (e.g. `context.setAttribute("appName","ShopSphere")`) in the context listener; read them in JSP via `${applicationScope.appName}`.
2. `ServletConfig`: at least two servlets read `<init-param>` values from `web.xml` (e.g. page size, support email).
3. Request/Response demos used in real features: `getParameter`, `getParameterValues`, `getHeader`, `setContentType`, `sendRedirect` vs `RequestDispatcher.forward`, `setStatus`/`sendError` for 404/500 custom error pages (`error404.jsp`, `error500.jsp`).
4. `webapp/demo/jsp-elements.jsp`: a **separate syllabus demo page** showing declaration `<%! %>`, scriptlet `<% %>`, expression `<%= %>`, directive (`page`, `include`, `taglib`), JSP comment `<%-- --%>`, implicit objects (`request, response, session, application, out, config, pageContext`), plus a visible explanation of JSP lifecycle (translation → compilation → `_jspInit` → `_jspService` → `_jspDestroy`) and a counter that proves the page instance is reused.
5. `webapp/demo/el-demo.jsp`: EL examples — `${product.name}`, `${sessionScope.user.name}`, `${param.q}`, `${empty cart}`, `${fn:length(...)}`, scopes `pageScope/requestScope/sessionScope/applicationScope`.

**Acceptance criteria**
- Demo pages render correctly and are linked from a "Syllabus Demos" index page (`/demo/index.jsp`).

---

## PHASE 11 — Filters, listeners, sessions and cookies

**Goal:** Security and event handling.

**Tasks**
1. `AuthenticationFilter`: protects customer pages (`/cart`, `/checkout`, `/orders`, `/profile`, `/wishlist` etc.); unauthenticated requests redirect to `login.jsp?redirect=<original URL>`.
2. `AdminFilter`: protects `/admin/*`; only `ADMIN` role allowed; others get 403 with a friendly page.
3. Extra filters for syllabus depth: `EncodingFilter` (UTF-8), `LoggingFilter` (logs method, URL, time taken). Show **filter chaining order** configured in `web.xml` or annotations and explain in comments.
4. Listeners (use `@WebListener`):
   - `AppContextListener` implements `ServletContextListener` (startup: load config, set context attributes; shutdown: log).
   - `SessionListener` implements `HttpSessionListener` (+ `HttpSessionAttributeListener` optionally): maintain an **active-sessions counter** shown on the admin dashboard.
   - Optional `RequestListener`.
5. Cookies: `recently viewed products` cookie (IDs only, `HttpOnly`, reasonable max-age), shown on the home page; optional `language` cookie used in Phase 16. **No sensitive data in cookies.**
6. Security hardening: session cookie `HttpOnly`, `SameSite`, CSRF token on POST forms, server-side validation helper class, output escaping everywhere.

**Acceptance criteria**
- Customer hitting `/admin/dashboard` is denied; anonymous hitting `/cart` is redirected to login and sent back after login.
- Active-session count changes when sessions start/end.

---

## PHASE 12 — JSTL, JSP fragments, tag files, tag handlers

**Goal:** Reusable JSP components and full JSTL coverage.

**Tasks**
1. **Fragments** (`webapp/fragments/`): `header.jsp`, `navbar.jsp`, `footer.jsp`, `sidebar.jsp`; included with `<%@ include file="..." %>` (static) in some pages and `<jsp:include page="..."/>` (dynamic) in others — add a comment explaining the difference.
2. **Tag files** (`WEB-INF/tags/`): `productCard.tag`, `rating.tag`, `alert.tag`, `orderStatusBadge.tag`; used across catalogue, wishlist, admin pages.
3. **Classic tag handler (Tag Extension)**: a Java `SimpleTagSupport` (or `TagSupport`) class, e.g. `PriceTag` or `RatingTag`, with a `.tld` file in `WEB-INF/`, used in at least one JSP, plus a note on tag-handler lifecycle.
4. **JSP fragment attributes**: at least one tag file using `<jsp:invoke fragment="...">` / `<jsp:doBody/>` (e.g., a `card.tag` wrapper).
5. **JSTL coverage**:
   - Core: `c:forEach, c:if, c:choose/when/otherwise, c:set, c:out, c:url, c:redirect, c:import` (all used somewhere real).
   - Functions (`fn:`): `length, contains, toUpperCase, substring, join/split, escapeXml` used in product/search pages.
   - fmt: `fmt:formatNumber` (₹ currency), `fmt:formatDate`, `fmt:setLocale`, `fmt:setBundle`, `fmt:message` (used in Phase 16).
   - **XML (separate demo)** `demo/jstl-xml.jsp`: parse `webapp/data/products-feed.xml` with `x:parse`, `x:out`, `x:forEach`, `x:if`, `x:choose`.
   - **SQL (separate demo)** `demo/jstl-sql.jsp`: `sql:setDataSource`, `sql:query`, `sql:update` against a **demo table or read-only product query**, with a clearly visible banner: *"Syllabus demo only — the real application uses Servlet → Service → DAO → JDBC."*
   - Use the correct Jakarta JSTL URIs, e.g. `jakarta.tags.core`, `jakarta.tags.fmt`, `jakarta.tags.functions`, `jakarta.tags.xml`, `jakarta.tags.sql`.

**Acceptance criteria**
- All demo pages render on Tomcat 11 without taglib errors; catalogue pages use tag files; no scriptlets in main pages.

---

## PHASE 13 — Java networking module (Socket client-server) + Serialization

**Goal:** Inventory client-server with `java.net` and object serialization.

**Tasks** (module `network`)
1. `InventoryServer`: `ServerSocket` on a configurable port (default 5000), accepts clients in a loop, **handles each client in its own thread** (`ExecutorService`), uses `ObjectInputStream/ObjectOutputStream` for transferring objects.
2. Protocol (text command in, object/text out): `GET_PRODUCT <id>` → returns serialized `Product`; `GET_STOCK <id>` → int; `SEARCH_PRODUCT <keyword>` → `ArrayList<Product>`; `PING`; `EXIT`. Return clear error responses for unknown commands.
3. `InventoryClient`: console client with a menu, plus a `main` that runs scripted commands for demo.
4. **Serialization demo** (`SerializationDemo.java`): write a `Product` to `product.ser` with `ObjectOutputStream`, read it back, demonstrate `transient` field (e.g., a cached value) and `serialVersionUID`; explain what happens when a class changes.
5. **URL/URLConnection + content/protocol handler demo** (`UrlDemo.java`): use `URL`, `URLConnection`/`HttpURLConnection` to call the running Tomcat app's `/products?...` and print headers + a snippet of the body; also demonstrate `URLEncoder/URLDecoder` and a tiny custom `URLStreamHandler` or `ContentHandler` (e.g. `shop://` protocol) clearly commented as a syllabus demo.
6. `README` section in `docs/` on how to run server and client (`mvn exec:java` or runnable jars).

**Acceptance criteria**
- Start server, run client, receive correct product/stock data from MySQL, multiple clients work concurrently; serialization demo prints identical object after round trip.

---

## PHASE 14 — RMI module (+ JNDI/Naming)

**Goal:** Remote inventory service via RMI Registry.

**Tasks** (module `rmi`)
1. `InventoryRemote extends Remote` (name it `InventoryService` per the guide, if it does not clash with the local service — otherwise name the local one `LocalInventoryService`):
   - `Product getProduct(int id) throws RemoteException;`
   - `int getStock(int productId) throws RemoteException;`
   - `boolean updateStock(int productId, int quantity) throws RemoteException;`
   - plus `List<Product> getLowStockProducts(int threshold)`.
2. `InventoryServiceImpl extends UnicastRemoteObject implements InventoryService` — delegates to core DAO/Service and MySQL.
3. `RmiServer`: creates registry with `LocateRegistry.createRegistry(1099)` (or connects to an existing one), `Naming.rebind("rmi://localhost:1099/InventoryService", impl)`. Print startup info.
4. `RmiClient`: `Naming.lookup(...)` (and also the `Registry.lookup` variant), invokes all remote methods, demonstrates a `RemoteException` handling case.
5. Explain **RMI architecture** in `docs/RMI.md` (stub, skeleton/dispatcher, registry, marshalling, `Serializable` parameters) with a Mermaid sequence diagram. Mention that modern Java generates stubs dynamically (no `rmic`).
6. **JNDI/Naming demonstration** (put in `web` module so Tomcat provides the context):
   - Configure a `jdbc/shopsphereDS` DataSource resource in `META-INF/context.xml` (and `<resource-ref>` in `web.xml`).
   - `demo/jndi-demo.jsp` (or a servlet) performs `new InitialContext().lookup("java:comp/env/jdbc/shopsphereDS")`, runs a test query, and prints the result. Also show an `env-entry` lookup.
   - Document how this relates to RMI `Naming` in `docs/NAMING_JNDI.md`.
7. Optional: web admin dashboard widget that calls the RMI service (guarded so the page works even when the RMI server is down).

**Acceptance criteria**
- Start RMI server, run client: remote calls return correct data; JNDI demo page shows a successful DataSource lookup on Tomcat.

---

## PHASE 15 — Swing desktop admin (MVC)

**Goal:** A real Swing admin app using MVC and the shared DAO/Service layer.

**Tasks** (module `swing-admin`)
1. `LoginFrame` (`JTextField`, `JPasswordField`, `JButton`, `JCheckBox` "show password", `JLabel`, `JOptionPane` for errors) → only `ADMIN` role may enter.
2. `AdminDashboard` (`JFrame` with menu bar/`JTabbedPane` or buttons) → `ProductFrame`, `CategoryFrame`, `CustomerFrame`, `OrderFrame`, `InventoryFrame`.
3. Use **all** of: `JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, JCheckBox, JRadioButton (ButtonGroup for status/active), JComboBox (category/status), JTable + custom `AbstractTableModel`, JScrollPane, JOptionPane, JMenuBar, JTextArea`; layout managers (`BorderLayout`, `GridBagLayout`/`GridLayout`).
4. **MVC**: Model (`Product`, `User`, `Order` and table models), View (`LoginFrame`, `ProductFrame`, …), Controller (`LoginController`, `ProductController`, …) which uses core services/DAO. Views must contain **no** DB code.
5. Pluggable Look and Feel: `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` with a menu option to switch to Nimbus/Metal at runtime (`SwingUtilities.updateComponentTreeUI`).
6. Run all long DB calls off the Event Dispatch Thread (`SwingWorker`); start UI with `SwingUtilities.invokeLater`.
7. Product CRUD, order status update, customer activate/deactivate, low-stock display work end-to-end against MySQL.
8. Package a runnable jar (maven-shade or assembly) or `mvn exec:java` instructions.

**Acceptance criteria**
- Desktop admin logs in, performs product CRUD, and changes are visible in the web app.

---

## PHASE 16 — Internationalization + Applet concept demo

**Goal:** i18n in the web app and a syllabus-correct Applet explanation.

**Tasks**
1. **i18n**: `messages_en.properties`, `messages_hi.properties` (real Hindi translations for navbar, buttons, login/register labels, cart/checkout headings, validation messages). Use `ResourceBundle` in Java demo and `fmt:setBundle`/`fmt:message` in JSP.
2. Language switcher in the navbar (EN | हिंदी) stored in session/cookie; `fmt:setLocale`. Format prices with `fmt:formatNumber` using locale (`en_IN`) and dates with `fmt:formatDate`.
3. `core` Java demo `I18nDemo.java`: `Locale`, `ResourceBundle.getBundle`, `NumberFormat.getCurrencyInstance(new Locale("en","IN"))`, `DateFormat`/`DateTimeFormatter`, `MessageFormat`.
4. Ensure UTF-8 for properties and JSP (`pageEncoding`, `EncodingFilter`).
5. **Applets (concept only)**: write `docs/APPLETS.md` explaining applet lifecycle (`init, start, stop, destroy, paint`), why applets are obsolete/removed from browsers and deprecated in modern Java, and how this project replaces the idea (Swing desktop + JS browser UI). Optionally include `AppletDemo.java` in a separate, **non-built** `docs/examples/` folder (so it never breaks the Maven build). Note that the modern app does not depend on applets.

**Acceptance criteria**
- Switching to Hindi changes all key labels and keeps correct currency/date formatting; applet doc is clear and accurate.

---

## PHASE 17 — Syllabus mapping, J2EE/N-tier documentation

**Goal:** Prove syllabus coverage with pointers into real code.

**Tasks**
1. `docs/SYLLABUS_MAPPING.md`: table with columns **RTU Topic | Where implemented (file path / class) | How to demo in viva**. Must include every row of the guide's mapping table: Swing, Swing components, MVC, Applets, JDBC, JDBC Driver, java.net, Client & Server, Content/Protocol Handlers, RMI Architecture, RMI Registry, Naming/JNDI, Serialization, Internationalization, J2EE architecture, N-tier architecture, HTTP/Web application, Web container, Servlet, Servlet lifecycle, ServletConfig/ServletContext, Request/Response, Session handling, Event handling, Filters, JSP architecture/lifecycle, JSP elements, EL, Tag Extensions/Handlers, JSP Fragments, Tag Files, JSTL Core/XML/SQL/Functions.
2. `docs/J2EE_NTIER.md`: explain J2EE/Jakarta EE architecture, web container (Tomcat), HTTP request-response cycle, and how the project maps each layer (Presentation → Controller → Service → DAO → Database). Include Mermaid diagrams.
3. Add a `/demo/index.jsp` "Syllabus Demo Hub" page linking every demo (JSP elements, EL, JSTL XML/SQL, JNDI, i18n, session/cookie demo, listener/session count, filter log view).
4. Verify each mapping row points to an existing file; fix any gaps by adding the missing demo.

**Acceptance criteria**
- Every syllabus topic has a working code location and a one-line viva demo instruction.

---

## PHASE 18 — Testing, documentation, report and viva pack

**Goal:** Submission-ready deliverables.

**Tasks**
1. **Testing**
   - Run the full manual/automated checklist and record actual results in `docs/TEST_REPORT.md`:

     | Test | Expected result |
     |---|---|
     | Valid registration | User created |
     | Duplicate email | Validation error |
     | Valid login | Session created |
     | Invalid login | Error shown |
     | Unauthenticated cart access | Redirect to login |
     | Search product | Matching products |
     | Add to cart | Cart updated |
     | Change quantity | Totals recalculated |
     | Place order | Order and items created |
     | Admin login | Dashboard opened |
     | Customer opens /admin | Access denied |
     | Admin updates order | Customer sees new status |
     | Logout | Session invalidated |
     | RMI call | Remote response |
     | Socket request | Server response |

   - Add extra tests: coupon edge cases, stock exhaustion, XSS attempt, SQL-injection attempt, tampered total, expired session.
   - Automated: JUnit tests for DAO/service; optionally HTTP smoke tests (e.g. `curl` script `scripts/smoke-test.sh`).
2. **Diagrams** in `docs/diagrams/` (Mermaid `.md` files): ER, system architecture, use-case, class diagram (core models + services), **login sequence**, **order placement sequence**, RMI sequence, socket flow.
3. **README.md** (final): features, tech stack, prerequisites, quick start (DB import → `mvn clean package` → deploy WAR → run Swing/RMI/Socket modules), default credentials, troubleshooting (port conflicts, `jakarta` vs `javax`, MySQL timezone/SSL errors).
4. **`docs/SETUP.md`**: step-by-step on Windows and Linux/macOS, including Tomcat 11 deployment and optional Docker Compose (`docker-compose.yml` for MySQL + Tomcat) if you created one.
5. **`docs/REPORT.md`** following this outline, with real content (not placeholders): 1 Introduction, 2 Problem Statement, 3 Objectives, 4 Requirements, 5 Technology Stack, 6 System Architecture, 7 Database Design, 8 Module Design, 9 Implementation, 10 Testing, 11 Screenshots (list of screenshot filenames to capture + captions; add `docs/screenshots/README.md` telling the student what to capture), 12 RTU Syllabus Mapping, 13 Limitations and Future Scope, 14 Conclusion, References.
6. **`docs/VIVA.md`**: 40+ likely viva questions with concise model answers covering Servlet lifecycle, session vs cookie, forward vs redirect, filters vs listeners, JSP lifecycle, include directive vs action, EL, JSTL, tag files vs tag handlers, JDBC steps, PreparedStatement vs Statement, RMI architecture, serialization, socket vs RMI, N-tier, web container, JNDI, i18n; plus a **viva demonstration checklist** (the 14-step list from the guide) with exact commands/URLs.
7. Final cleanup: remove dead code, ensure consistent formatting, confirm `.gitignore`, confirm `mvn clean package` for all modules from a clean clone.

**Acceptance criteria**
- All docs exist and are consistent with the code.
- `PROGRESS.md` shows every phase DONE with honest verification notes.

---

# PART C — GLOBAL DEFINITION OF DONE

The build is complete only when **all** are true:

- [ ] `mvn clean package` succeeds for every module from the repo root.
- [ ] `database/shopsphere.sql` imports on a clean MySQL 8 instance; seed credentials documented.
- [ ] WAR deploys on **Tomcat 11** with no `javax.*` imports or taglib errors.
- [ ] Complete customer flow works: register → login → search → cart → coupon → checkout → order → history → review.
- [ ] Complete admin flow works (web): dashboard, product/category/coupon CRUD, order status updates, customer management, low-stock view.
- [ ] `AuthenticationFilter` and `AdminFilter` enforce access; listeners and cookies demonstrated.
- [ ] No SQL or business logic in any JSP; no SQL string concatenation anywhere.
- [ ] Passwords hashed; totals computed server-side; CSRF + XSS protections in place.
- [ ] Socket server/client, RMI server/client, Swing admin, JNDI demo, URL demo, serialization demo, i18n (EN/HI) all run.
- [ ] JSP elements, EL, JSTL (Core, Functions, fmt, XML, SQL), fragments, tag files and a tag handler are demonstrated.
- [ ] `docs/SYLLABUS_MAPPING.md` covers every RTU topic with real file references.
- [ ] README, SETUP, REPORT, VIVA, TEST_REPORT, diagrams, and `PROGRESS.md` are complete and honest about anything that could not be verified in the agent's environment.

# PART D — FINAL INSTRUCTION TO THE AGENT

Start now with **Phase 0**. Work through the phases **in order**, keeping `PROGRESS.md` current. After finishing Phase 18, print a final summary containing: (1) how to run each module, (2) default login credentials, (3) the list of any items that could not be verified in this environment and what the student must do to verify them.
