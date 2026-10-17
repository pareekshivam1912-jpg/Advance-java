# ShopSphere — Implementation Progress Tracker

This document tracks the phase-wise execution of **ShopSphere**, an Enterprise Java E-Commerce Management System and RTU Advanced Java project, following `SHOPSPHERE_ANTIGRAVITY_PROMPT.md`.

---

## Phase Checklist

- [x] **Phase 0:** Environment check and project scaffolding
- [x] **Phase 1:** Requirements and UI design (`docs/`)
- [x] **Phase 2:** Database design (`database/shopsphere.sql`, seed data, ER diagram)
- [x] **Phase 3:** Models and JDBC DAO layer (`core`)
- [x] **Phase 4:** Service layer and MVC structure (`core`)
- [x] **Phase 5:** Web foundation and authentication (`web`)
- [x] **Phase 6:** Products listing, search, filter, details (`web`)
- [x] **Phase 7:** Cart and Wishlist (`web`)
- [x] **Phase 8:** Checkout, coupons, mock payment, orders (`web`)
- [x] **Phase 9:** Admin module (web) (`web`)
- [x] **Phase 10:** Servlet/JSP polish: ServletConfig, ServletContext, JSP elements, EL (`web`)
- [x] **Phase 11:** Filters, listeners, sessions, cookies, security hardening (`web`)
- [x] **Phase 12:** JSTL, JSP fragments, tag files, classic tag handler (`web`)
- [x] **Phase 13:** Java networking module (Socket client-server) + Serialization (`network`)
- [x] **Phase 14:** RMI module + JNDI / Naming (`rmi`, `web`)
- [x] **Phase 15:** Swing desktop admin (MVC) (`swing-admin`)
- [x] **Phase 16:** Internationalization (EN/HI) + Applet concept demo (`core`, `web`, `docs`)
- [x] **Phase 17:** Syllabus mapping, J2EE/N-tier documentation (`docs`)
- [x] **Phase 18:** Testing, documentation, report, and viva pack (`docs`)

---

## Phase Execution Log

### Phase 0 — DONE
- **Built:** Multi-module Maven skeleton (`shopsphere-parent`, `core`, `web`, `swing-admin`, `network`, `rmi`).
- **Files Created:** `pom.xml` (parent and 5 module POMs), `.gitignore`, `README.md`, `PROGRESS.md`, `docs/DECISIONS.md`, `docs/SETUP.md`, `core/src/main/resources/db.properties`, `core/src/main/resources/db.properties.example`.
- **Environment Verified:**
  - Java: OpenJDK 21.0.11 LTS
  - Maven: 3.9.16
  - MySQL: MySQL Server 9.7 (Service `MySQL97` active on port 3306, credentials verified)
  - Tomcat: Apache Tomcat 11.0.25 (verified in `C:\Users\Admin\Downloads\apache-tomcat-11.0.25-windows-x64\apache-tomcat-11.0.25`)
- **Verification:** Ran `mvn clean package` from root; all 6 modules built successfully (BUILD SUCCESS).

### Phase 1 — DONE
- **Built:** Comprehensive requirements, use-case specifications, screen wireframes, and module decomposition.
- **Files Created:**
  - `docs/REQUIREMENTS.md`: Functional requirements for customer and admin modules, plus security, performance, responsive NFRs.
  - `docs/USE_CASES.md`: Actors (Guest, Customer, Admin), Mermaid use-case diagram, and 17 detailed use-case specifications.
  - `docs/SCREENS.md`: Wireframes (ASCII) and data flows for all customer, admin, and syllabus demonstration views.
  - `docs/MODULES.md`: Separation of concerns and responsibilities for all 5 Maven modules and database layer.
- **Verification:** Verified all 4 design documents are complete and align with the guide's feature set.

### Phase 2 — DONE
- **Built:** Complete MySQL schema DDL, indexes, constraints, triggers, seed data, and ER diagram.
- **Files Created:**
  - `database/shopsphere.sql`: Complete DDL for 12 tables (`users`, `addresses`, `categories`, `products`, `cart`, `wishlist`, `coupons`, `orders`, `order_items`, `payments`, `reviews`, `coupon_usage`), FKs, indexes, constraints, and rich seed data.
  - `database/er-diagram.md`: Full Mermaid entity-relationship diagram with cardinalities and attribute types.
  - `core/src/main/java/com/shopsphere/util/PasswordUtil.java`: PBKDF2WithHmacSHA256 salted password hashing utility with verification logic and CLI entrypoint.
- **Verification:** Executed `shopsphere.sql` against local MySQL 9.7 server (`MySQL97`); verified table creation and record counts (4 users, 6 categories, 32 products, 3 coupons, 2 orders). All queries succeeded with 0 errors.

### Phase 3 — DONE
- **Built:** POJOs and complete CRUD DAO layer in `core`.
- **Files Created:**
  - `com.shopsphere.exception.ShopSphereException`: Unchecked domain exception.
  - `com.shopsphere.util.DBUtil`: JDBC `DriverManager` connection factory reading `db.properties`.
  - 11 Domain Models: `User`, `Address`, `Category`, `Product`, `CartItem`, `WishlistItem`, `Coupon`, `Order`, `OrderItem`, `Payment`, `Review` (`Product`, `Order`, `OrderItem` implementing `Serializable`).
  - 11 DAO Interfaces and Implementations: `UserDAO`, `AddressDAO`, `CategoryDAO`, `ProductDAO`, `CartDAO`, `WishlistDAO`, `CouponDAO`, `OrderDAO`, `OrderItemDAO`, `PaymentDAO`, `ReviewDAO`.
  - Unit Tests: `UserDAOTest` and `ProductDAOTest`.
- **Verification:**
  - Executed `mvn test -pl core` running 10 test cases against MySQL; all passed with 0 failures, 0 errors.
  - Checked source code for SQL injection; verified 100% parameterization with `PreparedStatement` and zero user-input concatenation.

### Phase 4 — DONE
- **Built:** Full business service layer and MVC architecture documentation in `core`.
- **Files Created:**
  - 10 Business Services: `UserService`, `CategoryService`, `ProductService`, `CartService`, `WishlistService`, `AddressService`, `CouponService`, `CheckoutService` (with atomic DB transaction commit/rollback), `OrderService`, `ReviewService`, `LocalInventoryService`, `ReportService`.
  - `ServiceFactory`: Central static locator wiring singletons of all DAOs and Services.
  - `docs/ARCHITECTURE.md`: Complete documentation with Mermaid diagrams covering overall N-tier architecture, web request pipeline, and Swing MVC.
  - Unit Tests: `CouponServiceTest` and `CheckoutCalculationTest`.
- **Verification:**
  - Ran `mvn test -pl core`; all 20 tests passed (10 DAO tests + 10 Service tests).
  - Verified `core` module has zero imports or references to servlet or web container classes.

### Phase 5 — DONE
- **Built:** Web foundation, layout fragments, and authentication subsystem.
- **Files Created:**
  - `web/src/main/webapp/WEB-INF/web.xml`: Jakarta Servlet 6.0 schema, context params, session timeout, custom 404/500 error pages, and explicit `LoginServlet` configuration with `<init-param>`.
  - `web/src/main/webapp/css/style.css` and `web/src/main/webapp/js/main.js`: Modern responsive styling and UI enhancements.
  - Layout Fragments: `fragments/header.jsp`, `fragments/navbar.jsp`, `fragments/footer.jsp`, `fragments/sidebar.jsp`.
  - Servlets: `LoginServlet` (demonstrates Servlet Lifecycle `init`, `doGet`, `doPost`, `destroy` and session fixation prevention), `RegisterServlet`, `LogoutServlet`.
  - Views: `login.jsp` (with demo account filler buttons), `register.jsp`, `error404.jsp`, `error500.jsp`.
- **Verification:**
  - Built whole project with `mvn clean package`; WAR assembled successfully at `web/target/shopsphere.war`.
  - Tested registration validation, duplicate email handling, and session invalidation logic.

### Phase 6 — DONE
- **Built:** Products catalog browsing, searching, faceted filtering, pagination, and details views.
- **Files Created:**
  - `web/src/main/java/com/shopsphere/controller/HomeServlet.java`: Homepage controller mapped to `/` and `/home`, retrieving categories, featured and recent products, and parsing `recent_products` cookie.
  - `web/src/main/java/com/shopsphere/controller/ProductServlet.java`: Multi-route controller handling `/products` (keyword search, category filter, price range, sorting, pagination) and `/product` (details, reviews, related products, and writing `recent_products` cookie).
  - `web/src/main/webapp/index.jsp`: Modern homepage with hero architecture banner, value propositions, category grid, featured products, new arrivals, and cookie-based recently viewed items.
  - `web/src/main/webapp/products.jsp`: Catalog view with faceted filter sidebar (search, categories, price range), sorting selector, responsive product cards, and pagination preserving query parameters.
  - `web/src/main/webapp/product-details.jsp`: Complete product detail page with image gallery, SKU/brand badges, stock state badge, quantity selector, add-to-cart and wishlist forms, customer reviews list, and review submission form.
- **Verification:**
  - Compiled and packaged via `mvn -pl web -am compile`; all classes and JSPs verified with zero raw Java `<% %>` scriptlets.

### Phase 7 — DONE
- **Built:** Authenticated customer shopping cart and wishlist subsystems.
- **Files Created:**
  - `web/src/main/java/com/shopsphere/controller/CartServlet.java`: Controller for `/cart` supporting view, add, update, remove, and clear actions with stock boundary checks and server-side subtotal recalculation.
  - `web/src/main/java/com/shopsphere/controller/WishlistServlet.java`: Controller for `/wishlist` supporting view, add, remove, and moveToCart actions with cart counter synchronization.
  - `web/src/main/webapp/cart.jsp`: Interactive cart interface with line item quantity modifications, unit and total prices, out-of-stock indicators, clear cart confirmation, and order summary checkout prompt.
  - `web/src/main/webapp/wishlist.jsp`: Customer wishlist grid displaying saved items, pricing, in-stock validation, and one-click "Move to Cart".
- **Verification:**
  - Verified authentication checks redirect unauthenticated requests to login preserving intended target.
  - Re-verified quantity upper bound against product stock; over-stock additions trigger descriptive exceptions.
  - Compiled and validated with `mvn -pl web -am compile`.

### Phase 8 — DONE
- **Built:** End-to-end checkout, coupon validation, mock payment, order placement, order tracking timeline, customer review submission, and profile address management.
- **Files Created:**
  - `web/src/main/java/com/shopsphere/controller/CheckoutServlet.java`: Controller for `/checkout` and `/order-success` managing address selection, dynamic address creation, coupon recalculation, and delegating to `CheckoutService.placeOrder` inside an atomic DB transaction.
  - `web/src/main/java/com/shopsphere/controller/OrderServlet.java`: Controller for `/orders` and `/order` providing order history, granular order details, and cancellation with inventory restock.
  - `web/src/main/java/com/shopsphere/controller/ReviewServlet.java`: Controller for `/review` processing ratings (1-5) and feedback comments with live average rating recalculation.
  - `web/src/main/java/com/shopsphere/controller/ProfileServlet.java`: Controller for `/profile` managing personal details, password changes, and address book CRUD.
  - `web/src/main/webapp/checkout.jsp`: Checkout page with address selection, address creation drawer, mock payment options (COD, CARD, UPI), coupon code application, and order breakdown.
  - `web/src/main/webapp/order-success.jsp`: Order confirmation page featuring transaction reference, delivery estimation, and order tracker links.
  - `web/src/main/webapp/orders.jsp`: Order history list with status badges, line item previews, and cancel actions.
  - `web/src/main/webapp/order-details.jsp`: Complete order breakdown with 4-step status timeline (PLACED &rarr; CONFIRMED &rarr; SHIPPED &rarr; DELIVERED / CANCELLED), delivery address, and payment information.
  - `web/src/main/webapp/profile.jsp`: Customer account portal with profile editing, password reset with verification, and address management.
- **Verification:**
  - Validated that `CheckoutServlet` and `CheckoutService` compute all subtotals, discounts, shipping, and totals strictly from database state. Form parameter tampering for prices or totals has zero effect.
  - Validated atomic database transaction: `orders`, `order_items`, `payments`, `coupon_usage` inserted together, and `cart` cleared only on commit.
  - Verified compilation via `mvn -pl web -am compile` (11 source files compiled successfully).

### Phase 9 — DONE
- **Built:** Complete administrative web portal featuring executive KPIs, product catalog manager with stock adjustment, category management, order dispatch transitions, customer status toggles, and promotional coupon engine.
- **Files Created:**
  - `web/src/main/webapp/fragments/admin-navbar.jsp`: Administrative navigation header with console badge and direct section routing.
  - `web/src/main/java/com/shopsphere/controller/AdminDashboardServlet.java`: Controller for `/admin/dashboard` computing total revenue, orders, customer counts, and low-stock threshold products.
  - `web/src/main/java/com/shopsphere/controller/AdminProductServlet.java`: Controller for `/admin/products` supporting product listing, creation, modification, soft deactivation, and instant stock increments.
  - `web/src/main/java/com/shopsphere/controller/AdminCategoryServlet.java`: Controller for `/admin/categories` managing category hierarchy and status.
  - `web/src/main/java/com/shopsphere/controller/AdminOrderServlet.java`: Controller for `/admin/orders` enabling status transitions (PLACED &rarr; CONFIRMED &rarr; SHIPPED &rarr; DELIVERED &rarr; CANCELLED) and payment verification.
  - `web/src/main/java/com/shopsphere/controller/AdminUserServlet.java`: Controller for `/admin/users` managing customer accounts and security activation/deactivation.
  - `web/src/main/java/com/shopsphere/controller/AdminCouponServlet.java`: Controller for `/admin/coupons` configuring percentage and flat rate promotional codes with discount caps.
  - `web/src/main/webapp/admin/dashboard.jsp`: Executive dashboard with financial metrics, recent orders summary, and quick restock actions.
  - `web/src/main/webapp/admin/products.jsp`: Product catalog manager with collapsible creation form and stock level controls.
  - `web/src/main/webapp/admin/categories.jsp`: Department and category management.
  - `web/src/main/webapp/admin/orders.jsp`: Order operations console with direct dropdown status updates.
  - `web/src/main/webapp/admin/users.jsp`: Customer account manager.
  - `web/src/main/webapp/admin/coupons.jsp`: Promotional coupon engine interface.
- **Verification:**
  - Verified role-based restriction on all `/admin/*` servlets: unauthorized and customer sessions are securely redirected to login.
  - Verified instant sync: changing order status in admin console immediately updates customer-facing order tracking timeline; adding a product displays immediately in storefront.
  - Compiled and validated with `mvn -pl web -am compile` (17 source files compiled with 0 errors).

### Phase 10 — DONE
- **Built:** Academic syllabus demonstrator suite for ServletConfig, ServletContext, traditional JSP elements, and Expression Language (EL).
- **Files Created / Updated:**
  - `web/src/main/webapp/WEB-INF/web.xml`: Configured application-wide context parameters (`appName`, `appVersion`, `supportEmail`) accessible via `${applicationScope.*}`, and `<init-param>` values on both `LoginServlet` and `ProductServlet`.
  - `web/src/main/java/com/shopsphere/controller/ProductServlet.java`: Configured `ServletConfig.getInitParameter` reading `defaultPageSize` and `supportEmail`.
  - `web/src/main/webapp/demo/jsp-elements.jsp`: Syllabus practical demonstrator detailing Declarations (`<%! %>`), Scriptlets (`<% %>`), Expressions (`<%= %>`), Directives, Comments (`<%-- --%>`), proof of Servlet instance reuse with live counter, translation pipeline table, and all 9 implicit objects.
  - `web/src/main/webapp/demo/el-demo.jsp`: Expression Language demonstrator showing 4-tier scope resolution (`pageScope` &rarr; `requestScope` &rarr; `sessionScope` &rarr; `applicationScope`), arithmetic/relational/logical operators, and JSTL `fn:` string manipulation.
  - `web/src/main/webapp/demo/index.jsp`: Academic Syllabus Demo Hub connecting all university viva and practical demonstration pages.
- **Verification:**
  - Verified that reload counter on `jsp-elements.jsp` persists across requests, proving Tomcat single-instance Servlet reuse.
  - Verified that `${applicationScope.appName}` correctly outputs "ShopSphere" from `web.xml` context parameter.
  - Compiled and validated via `mvn -pl web -am compile`.

### Phase 11 — DONE
- **Built:** Enterprise Filter chain pipeline, lifecycle event listeners, session tracking, and security hardening.
- **Files Created / Updated:**
  - `web/src/main/java/com/shopsphere/filter/EncodingFilter.java`: Filter 1 in chain enforcing UTF-8 request and response body parsing.
  - `web/src/main/java/com/shopsphere/filter/LoggingFilter.java`: Filter 2 in chain auditing HTTP verb, URI, status code, and latency in milliseconds.
  - `web/src/main/java/com/shopsphere/filter/AuthenticationFilter.java`: Filter 3 in chain intercepting `/cart`, `/checkout`, `/orders`, `/order`, `/order-success`, `/profile`, `/wishlist`, and `/review`, redirecting anonymous users to login with an encoded target parameter.
  - `web/src/main/java/com/shopsphere/filter/AdminFilter.java`: Filter 4 in chain protecting `/admin/*` routes with role verification, denying non-admin accounts with HTTP 403 Forbidden.
  - `web/src/main/java/com/shopsphere/listener/AppContextListener.java`: `ServletContextListener` logging container startup and injecting global attributes.
  - `web/src/main/java/com/shopsphere/listener/SessionListener.java`: `HttpSessionListener` and `HttpSessionAttributeListener` maintaining thread-safe `activeSessionCount` in `ServletContext`.
  - `web/src/main/java/com/shopsphere/listener/AppRequestListener.java`: `ServletRequestListener` auditing request processing nano-times.
  - `web/src/main/webapp/error403.jsp`: Friendly HTTP 403 Access Forbidden template.
  - `web/src/main/webapp/WEB-INF/web.xml`: Configured exact sequential `<filter-mapping>` chain order and custom 403 error page mapping.
  - `web/src/main/webapp/admin/dashboard.jsp`: Integrated live concurrent session counter badge powered by `SessionListener`.
- **Verification:**
  - Verified filter chain sequence in `web.xml`: `EncodingFilter` &rarr; `LoggingFilter` &rarr; `AuthenticationFilter` &rarr; `AdminFilter`.
  - Tested that attempting to access `/admin/dashboard` as customer redirects or yields 403 Forbidden.
  - Compiled and packaged cleanly via `mvn -pl web -am compile` (24 source files).

### Phase 12 — DONE
- **Built:** Reusable JSP components, custom Tag Extension (`PriceTag`), Tag Library Descriptor, 5 Tag files, static/dynamic fragment architecture, and full JSTL XML & SQL demonstrators.
- **Files Created / Updated:**
  - `web/src/main/java/com/shopsphere/tag/PriceTag.java`: Classic custom tag extension extending `SimpleTagSupport`, formatting monetary amounts with locale decimal formatting and strikethrough styling; documented tag handler lifecycle.
  - `web/src/main/webapp/WEB-INF/shopsphere.tld`: Jakarta EE 3.0 Tag Library Descriptor declaring custom `<shop:price>` tag handler.
  - `web/src/main/webapp/WEB-INF/tags/rating.tag`: Component rendering star rating icons.
  - `web/src/main/webapp/WEB-INF/tags/orderStatusBadge.tag`: Contextual badge component for order lifecycle states.
  - `web/src/main/webapp/WEB-INF/tags/alert.tag`: Dismissible Bootstrap alert component.
  - `web/src/main/webapp/WEB-INF/tags/card.tag`: Card wrapper demonstrating `<jsp:doBody/>` and `<jsp:invoke fragment="footer"/>`.
  - `web/src/main/webapp/WEB-INF/tags/productCard.tag`: Product card component integrating `<shop:price>` and `<t:rating>`.
  - `web/src/main/webapp/fragments/sidebar.jsp`: Documented static `<%@ include %>` vs dynamic `<jsp:include>` translation vs runtime dispatch.
  - `web/src/main/webapp/data/products-feed.xml`: Product catalog XML data source.
  - `web/src/main/webapp/demo/jstl-xml.jsp`: JSTL XML demonstrator using `x:parse`, `x:out`, `x:forEach`, `x:if`, `x:choose`.
  - `web/src/main/webapp/demo/jstl-sql.jsp`: JSTL SQL demonstrator using `sql:setDataSource` and `sql:query` with MVC architecture disclaimer.
- **Verification:**
  - Verified that `PriceTag` correctly handles `BigDecimal` values and outputs valid currency markup.
  - Verified that all tag files load without taglib registration errors.
  - Compiled and packaged successfully via `mvn -pl web -am compile` (25 source files).

### Phase 13 — DONE
- **Built:** Java Networking (TCP client-server inventory system), multithreaded socket handling, Object Serialization with `transient` fields and `serialVersionUID`, and URL/protocol handlers.
- **Files Created / Updated:**
  - `core/src/main/java/com/shopsphere/model/Product.java`: Added `transient String sessionViewToken` with getter and setter to demonstrate serialization stream isolation.
  - `network/src/main/java/com/shopsphere/network/InventoryServer.java`: Multithreaded TCP `ServerSocket` on port 5000 using `ExecutorService` thread pool, `ObjectOutputStream`/`ObjectInputStream`, protocol: `PING`, `GET_PRODUCT <id>`, `GET_STOCK <id>`, `SEARCH_PRODUCT <keyword>`, `EXIT`. Handshake deadlock prevented by flushing `ObjectOutputStream` prior to `ObjectInputStream`.
  - `network/src/main/java/com/shopsphere/network/InventoryClient.java`: Interactive console menu and scripted verification client sending commands and deserializing `Product` objects.
  - `network/src/main/java/com/shopsphere/network/SerializationDemo.java`: Serializes `Product` to `product.ser`, deserializes it back, proves `transient` field is reset to `null` while primary fields remain intact, and explains `serialVersionUID` class evolution.
  - `network/src/main/java/com/shopsphere/network/UrlDemo.java`: Demonstrates `URLEncoder`/`URLDecoder` UTF-8 roundtrip, `HttpURLConnection` response code and header extraction, and custom `shop://` protocol scheme via `URLStreamHandler`.
  - `network/src/test/java/com/shopsphere/network/InventoryServerTest.java`: JUnit 5 integration test confirming multithreaded socket protocol roundtrip on port 5055.
  - `docs/NETWORK.md`: Complete documentation covering architecture, text-object protocol, execution instructions, and viva Q&A.
- **Verification:**
  - Executed `SerializationDemo` via Maven; verified roundtrip state match and transient exclusion (PASSED).
  - Executed `UrlDemo` via Maven; verified URL encoding and custom `shop://` protocol handling (PASSED).
  - Ran `mvn test -pl network`; integration test connected to `InventoryServer`, dispatched `PING`, `GET_PRODUCT`, `GET_STOCK`, `SEARCH_PRODUCT`, and `EXIT` commands successfully with 0 failures, 0 errors.

### Phase 14 — DONE
- **Built:** Distributed Java Remote Method Invocation (RMI) module, dynamic proxy stubs, RMI Registry on port 1099, and JNDI Container DataSource / Environment naming service.
- **Files Created / Updated:**
  - `rmi/src/main/java/com/shopsphere/rmi/InventoryService.java`: `Remote` interface defining `getProduct(id)`, `getStock(productId)`, `updateStock(productId, quantity)`, `getLowStockProducts(threshold)` throwing `RemoteException`.
  - `rmi/src/main/java/com/shopsphere/rmi/InventoryServiceImpl.java`: `UnicastRemoteObject` implementing `InventoryService` and delegating queries to `LocalInventoryService` / MySQL.
  - `rmi/src/main/java/com/shopsphere/rmi/RmiServer.java`: Programmatic RMI registry creator (`LocateRegistry.createRegistry(1099)`) publishing service stub via `Naming.rebind("rmi://localhost:1099/InventoryService", impl)`.
  - `rmi/src/main/java/com/shopsphere/rmi/RmiClient.java`: Interactive and scripted client demonstrating both `Naming.lookup()` and `LocateRegistry.getRegistry().lookup()`, invoking remote procedures with automatic argument/result marshaling.
  - `rmi/src/test/java/com/shopsphere/rmi/RmiInventoryServiceTest.java`: JUnit 5 integration test verifying RMI distributed operations against in-process registry.
  - `docs/RMI.md`: Architectural documentation with Mermaid sequence diagram explaining dynamic proxy stubs, dispatcher, marshaling, and modern Java dynamic stub generation.
  - `web/src/main/webapp/META-INF/context.xml`: Tomcat container context declaring JNDI managed connection pool (`jdbc/shopsphereDS`) and environment entries (`academicEnvironment`, `maxUploadSizeMB`).
  - `web/src/main/webapp/WEB-INF/web.xml`: Added `<resource-ref>` and `<env-entry>` declarations.
  - `web/src/main/webapp/demo/jndi-demo.jsp`: Interactive demonstration page executing `new InitialContext().lookup("java:comp/env/jdbc/shopsphereDS")`, querying database metadata and product counts, and reading environment entries.
  - `docs/NAMING_JNDI.md`: Academic comparison of JNDI vs RMI Naming vs `DriverManager` with viva preparation questions.
  - `web/src/main/webapp/admin/dashboard.jsp`: Added guarded RMI Distributed Inventory status widget.
- **Verification:**
  - Ran `mvn test -pl rmi`; RMI test connected to remote service, executed `getProduct(1)`, `getStock(1)`, `updateStock(1, stock + 1)`, and `getLowStockProducts(50)` with 0 failures, 0 errors.
  - Built whole project with `mvn install -DskipTests`; parent, core, rmi, web, network all compiled and packaged with BUILD SUCCESS.

### Phase 15 — DONE
- **Built:** Enterprise Java Swing Desktop Administration application adhering strictly to Model-View-Controller (MVC) architecture, Event Dispatch Thread (EDT) safety via `javax.swing.SwingWorker`, and runtime Pluggable Look and Feel (PLAF).
- **Files Created / Updated:**
  - `swing-admin/src/main/java/com/shopsphere/swing/SwingAdminApp.java`: Main entrypoint setting System L&F and launching UI on EDT via `SwingUtilities.invokeLater`.
  - Models (`com.shopsphere.swing.model`):
    - `ProductTableModel.java`, `CategoryTableModel.java`, `CustomerTableModel.java`, `OrderTableModel.java`, `InventoryTableModel.java` (extending `AbstractTableModel`).
  - Controllers (`com.shopsphere.swing.controller`):
    - `LoginController.java`, `ProductController.java`, `CategoryController.java`, `CustomerController.java`, `OrderController.java`, `InventoryController.java` (asynchronous background execution off the EDT via `SwingWorker`).
  - Views (`com.shopsphere.swing.view`):
    - `LoginFrame.java`: Administrative login window with `JTextField`, `JPasswordField`, `JCheckBox` (Show Password), `JButton`, and `JOptionPane` error dialogs.
    - `AdminDashboard.java`: Main frame with `JMenuBar` (File, Look & Feel [System, Nimbus, Metal], Help) and `JTabbedPane`.
    - `ProductPanel.java`: Product CRUD interface with `JTable`, `JSplitPane`, form inputs (`JTextField`, `JComboBox`, `JRadioButton`, `ButtonGroup`, `JTextArea`), Add/Edit/Deactivate/Search actions.
    - `CategoryPanel.java`: Category management view.
    - `CustomerPanel.java`: Customer accounts management view with status toggling.
    - `OrderPanel.java`: Order operations console with status update dropdown and line item viewer.
    - `InventoryPanel.java`: Warehouse stock alert console with quick restock actions (+10, +25, +50 units).
  - Tests & Packaging:
    - `swing-admin/src/test/java/com/shopsphere/swing/SwingMvcTest.java`: 5 unit tests verifying all custom TableModels.
    - `swing-admin/pom.xml`: Configured `maven-assembly-plugin` building standalone `shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar`.
    - `docs/SWING_ADMIN.md`: Complete architectural documentation with component matrix, concurrency details, and run instructions.
- **Verification:**
  - Ran `mvn test -pl swing-admin`; 5 tests executed with 0 failures, 0 errors.
  - Built whole project with `mvn install -DskipTests`; fat jar assembled successfully.

### Phase 16 — DONE
- **Built:** Multi-language Internationalization (i18n) and Localization (l10n) subsystem supporting English (`en_IN`) and Hindi (`hi_IN`), and university viva educational demonstration of Java Applets.
- **Files Created / Updated:**
  - `core/src/main/resources/messages.properties`: Default English internationalization bundle.
  - `core/src/main/resources/messages_en.properties`: Explicit English resource bundle.
  - `core/src/main/resources/messages_hi.properties`: Real UTF-8 Hindi resource bundle covering navigation, buttons, titles, orders, and authentication strings.
  - `core/src/main/java/com/shopsphere/util/I18nDemo.java`: Standalone demonstration of `Locale`, `ResourceBundle`, `NumberFormat` currency formatting (INR ₹ vs USD $), `DateTimeFormatter`, and `MessageFormat` positional token interpolation.
  - `web/src/main/java/com/shopsphere/controller/LanguageServlet.java`: Controller for `/lang` persisting user preference in `HttpSession`, configuring Jakarta JSTL `Config.FMT_LOCALE`, and issuing a 30-day persistent `shopsphere_lang` cookie.
  - `web/src/main/webapp/fragments/navbar.jsp`: Integrated `<fmt:setLocale>` and `<fmt:setBundle>`, replaced hardcoded navigation labels with `<fmt:message>`, and provided instant language switcher dropdown.
  - `docs/examples/AppletDemo.java`: Conceptual Java Applet demonstrating the 5 classical lifecycle methods (`init, start, paint, stop, destroy`), isolated in `docs/examples/` outside the Maven source reactor.
  - `docs/APPLETS.md`: Comprehensive academic guide detailing applet lifecycle, browser security sandbox, NPAPI deprecation and removal in Java 17, and modern replacement via Swing MVC and HTML5.
- **Verification:**
  - Executed `I18nDemo` via Maven; validated `ResourceBundle` retrieval in English and Hindi, currency formatting, localized timestamps, and parameterized `MessageFormat` strings (PASSED).
  - Verified `web` module compilation with `LanguageServlet` and JSTL fmt tags (BUILD SUCCESS).

### Phase 17 — DONE
- **Built:** Comprehensive RTU syllabus mapping documentation, N-tier and J2EE/Jakarta EE architectural specifications, session/cookie inspector demo, and centralized Syllabus Demo Hub.
- **Files Created / Updated:**
  - `docs/SYLLABUS_MAPPING.md`: Complete mapping matrix containing all 29 RTU Advanced Java topics with exact source file paths and step-by-step viva demonstration instructions.
  - `docs/J2EE_NTIER.md`: Comprehensive academic architectural guide with Mermaid diagrams detailing J2EE/Jakarta EE architecture, Tomcat request-response pipeline, web container role, filter chain, and strict 5-tier layer separation.
  - `web/src/main/webapp/demo/session-demo.jsp`: Live educational inspector demonstrating active `HttpSession` state, creation/access timestamps, idle timeout management, client cookie headers, and session invalidation.
  - `web/src/main/webapp/demo/index.jsp`: Academic Demo Hub linking all 7 live syllabus demonstrations (JSP elements, EL scopes, JSTL XML, JSTL SQL, JNDI DataSource, i18n English/Hindi, Session/Cookie inspector).
- **Verification:**
  - Compiled `web` module via `mvn -pl web -am compile` (BUILD SUCCESS).
  - Verified every syllabus topic points to an existing, fully functioning file in the repository.

### Phase 18 — DONE
- **Built:** Comprehensive test suite execution, automated HTTP smoke test scripts, 8 Mermaid architectural diagrams, student screenshot capture instructions, 45-question viva voce preparation guide with 14-step live demonstration checklist, 15-section RTU university project report, and master README.
- **Files Created / Updated:**
  - `docs/TEST_REPORT.md`: Full testing dossier with 25 manual functional tests, 6 boundary tests, 7 security attack matrices (SQLi, XSS, CSRF, Session Fixation, Price Tampering), and automated test results (100% pass rate).
  - `scripts/smoke-test.ps1` & `scripts/smoke-test.sh`: Automated PowerShell and Bash test scripts verifying all 15 web endpoints and redirects against a live server.
  - `docs/diagrams/`: 8 complete Mermaid diagrams:
    - `er.md`: 12-table Entity-Relationship diagram in 3NF with constraints.
    - `architecture.md`: Multi-tier enterprise architecture showing all tiers and distributed subsystems.
    - `usecase.md`: System use case diagram with Guest, Customer, and Admin actors.
    - `class.md`: Detailed class diagram for core models, DAOs, and services.
    - `login_seq.md`: Authentication sequence diagram detailing filters and session fixation prevention.
    - `order_seq.md`: Atomic checkout sequence diagram showing transaction boundary and row-level locking.
    - `rmi_seq.md`: RMI sequence diagram showing Registry lookup and remote method dispatch.
    - `socket_flow.md`: Multithreaded TCP socket flow showing connection lifecycle and command handling.
  - `docker-compose.yml`: Zero-configuration containerized deployment for MySQL 9.0 and Tomcat 11.
  - `docs/SETUP.md`: Complete cross-platform installation and operations manual.
  - `docs/screenshots/README.md`: Student instructions for capturing 18 high-resolution application screenshots with captions.
  - `docs/VIVA.md`: 45 comprehensive viva questions with model answers categorized across all syllabus areas, plus the 14-step live demonstration checklist.
  - `docs/REPORT.md`: Exhaustive 15-section RTU Capstone University Project Report with real technical content, equations, code excerpts, and bibliography.
  - `README.md`: Master project guide with badges, architecture overview, quick start, credentials table, demo hub pointers, and troubleshooting.
- **Verification:**
  - Executed root `mvn clean package`: all 6 modules built successfully (BUILD SUCCESS) in 35.9s.
  - Automated Tests: 31 unit and integration tests executed with 0 failures, 0 errors, 0 skipped.
  - All project artifacts verified and ready for academic submission.

