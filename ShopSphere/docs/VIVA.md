# ShopSphere — Comprehensive Viva Voce Guide & Demonstration Checklist

**Course:** RTU 6CS4-04 Advanced Java Laboratory & Capstone Project  
**Target Audience:** External Examiners, University Evaluators, and Students  
**Project:** ShopSphere Enterprise Java E-Commerce Management System  

---

## Part 1: 14-Step Live Viva Demonstration Checklist

Follow this exact sequence during your university practical examination to demonstrate 100% syllabus coverage in under 10 minutes:

| Step | Action / Feature | URL / Command | What to Point Out to Examiner |
|:---:|:---|:---|:---|
| **1** | **Server Environment & Database** | MySQL Workbench / CLI<br>`SELECT count(*) FROM products;` | Show 12 relational tables in 3NF, foreign key integrity, and PBKDF2 hashed passwords in `users` table. |
| **2** | **Storefront Catalog & Cookies** | `http://localhost:8080/shopsphere/` | Point out Bootstrap 5 responsive UI, categories grid, featured items, and the cookie-driven "Recently Viewed" section. |
| **3** | **Search, Filter & Pagination** | `http://localhost:8080/shopsphere/products?categoryId=1&sort=price_asc` | Show multi-criteria filtering (category, price slider, sorting) handled by `ProductServlet` via SQL `PreparedStatement`. |
| **4** | **Product Detail & Reviews** | `http://localhost:8080/shopsphere/product?id=1` | Show dynamic stock status, customer review aggregation, star ratings, and the `<shop:price>` custom JSP tag. |
| **5** | **Authentication & Security** | `http://localhost:8080/shopsphere/login` | Click demo auto-fill buttons. Point out `request.changeSessionId()` in `LoginServlet` preventing Session Fixation attacks. |
| **6** | **Cart & Dynamic Pricing** | `http://localhost:8080/shopsphere/cart` | Modify quantities; show line item recalculation, GST (18%) computation, and server-side total verification. |
| **7** | **Atomic Checkout Transaction** | `http://localhost:8080/shopsphere/checkout` | Apply coupon `WELCOME10`. Submit order. Explain `conn.setAutoCommit(false)` in `CheckoutService` ensuring ACID atomicity. |
| **8** | **Web Admin Management** | `http://localhost:8080/shopsphere/admin/dashboard` | Show admin analytics, product CRUD, and order status workflow (`PENDING` &rarr; `SHIPPED` &rarr; `DELIVERED`). |
| **9** | **Syllabus Academic Demo Hub** | `http://localhost:8080/shopsphere/demo/` | Show the centralized hub linking all academic demonstrations. |
| **10** | **JSP Elements & Scopes** | `http://localhost:8080/shopsphere/demo/jsp-elements.jsp`<br>`http://localhost:8080/shopsphere/demo/el-demo.jsp` | Show declarations (`<%! %>`), scriptlets (`<% %>`), expressions (`<%= %>`), 9 implicit objects, and 4-tier EL scope resolution. |
| **11** | **JSTL XML, SQL & JNDI** | `http://localhost:8080/shopsphere/demo/jstl-xml.jsp`<br>`http://localhost:8080/shopsphere/demo/jndi-demo.jsp` | Show XML XPath feed parsing (`<x:parse>`), JSTL direct SQL, and `InitialContext` DataSource lookup from Tomcat JNDI. |
| **12** | **Internationalization (i18n)** | `http://localhost:8080/shopsphere/lang?set=hi` | Click "हिंदी" in navbar. Show dynamic UI translation backed by `messages_hi.properties` and `fmt:message` JSTL tags. |
| **13** | **TCP Socket Networking** | Dual terminal:<br>`mvn -pl network exec:java "-Dexec.mainClass=...InventoryServer"`<br>`mvn -pl network exec:java "-Dexec.mainClass=...InventoryClient"` | Show multithreaded `ServerSocket` listening on port 5000, thread pool dispatch, and low-level stream message exchange. |
| **14** | **Distributed Java RMI & Swing** | `mvn -pl rmi exec:java "-Dexec.mainClass=...RmiServer"`<br>`java -jar swing-admin/target/shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar` | Show RMI Registry (port 1099) remote method calls, and Swing Desktop Admin with runtime Pluggable Look and Feel. |

---

## Part 2: 45 Core Viva Voce Questions & Model Answers

### Category A: Jakarta Servlets & Web Container Architecture

#### Q1: What is a Servlet, and what is its role in J2EE/Jakarta EE?
**Answer:** A Servlet is a server-side Java class executing inside a Web Container (e.g., Apache Tomcat) that receives client requests (typically HTTP), processes business logic, and dynamically generates responses (HTML, JSON, XML). In ShopSphere, servlets act as **Controllers** in the MVC architectural pattern.

#### Q2: Explain the complete lifecycle of a Servlet with method signatures.
**Answer:** The servlet lifecycle is managed entirely by the servlet container in three distinct phases:
1. **Initialization (`init(ServletConfig config)`):** Invoked once when the servlet is first loaded (or at startup if `<load-on-startup>` is configured). Initializes database pools or configuration parameters.
2. **Request Servicing (`service(ServletRequest req, ServletResponse resp)`):** Dispatches requests to HTTP-specific handlers (`doGet`, `doPost`, `doPut`, `doDelete`) on each client request. Serviced concurrently across multiple threads.
3. **Destruction (`destroy()`):** Invoked once when the servlet container shuts down or the application is undeployed. Releases resources, closes connections, or flushes logs.

#### Q3: What is the difference between `ServletConfig` and `ServletContext`?
**Answer:**
- `ServletConfig`: Unique to an individual servlet instance. Holds initialization parameters defined inside that specific `<servlet>` tag in `web.xml`. Accessible via `getServletConfig().getInitParameter("name")`.
- `ServletContext`: Shared across the entire web application (all servlets, filters, and JSPs within the same WAR). Stores application-wide resources, context parameters, and shared attributes. Accessible via `getServletContext()`.

#### Q4: What is the difference between `RequestDispatcher.forward()` and `HttpServletResponse.sendRedirect()`?
**Answer:**
- `forward()`: **Server-side** transfer. The request and response objects are preserved. URL in the browser address bar does not change. Faster (only 1 round-trip) and can share request-scoped attributes.
- `sendRedirect()`: **Client-side** redirection. Sends an HTTP `302 Found` response containing a `Location` header back to the browser. The browser issues a completely new `GET` request. URL in the address bar changes. Request attributes are lost.

#### Q5: Are Servlets thread-safe? How does ShopSphere maintain thread safety?
**Answer:** By default, servlets are **NOT** thread-safe because the web container instantiates a single servlet instance and spawns a separate worker thread for each incoming client request. In ShopSphere:
- We never store request-specific state in servlet instance variables.
- All per-request data is encapsulated within local method variables (allocated on each thread's private stack) or stored in `HttpServletRequest`.
- Service and DAO classes are immutable or thread-safe singletons.

---

### Category B: Session Management & Security

#### Q6: How does `HttpSession` work under the hood?
**Answer:** When `request.getSession()` is invoked, the container checks for a unique session identifier sent by the client (normally stored in a cookie named `JSESSIONID`). If present and valid in the container's session table, that `HttpSession` object is returned; otherwise, a new session with a cryptographically secure 128-bit ID is generated, stored in server memory, and transmitted back via `Set-Cookie: JSESSIONID=...; Path=/shopsphere; HttpOnly`.

#### Q7: What is Session Fixation, and how does ShopSphere prevent it?
**Answer:** Session Fixation occurs when an attacker forces a known session ID onto a victim (e.g., via a phishing link). When the victim logs in, the attacker can hijack the authenticated session. ShopSphere prevents this by calling `request.changeSessionId()` in `LoginServlet` immediately upon successful credential validation, rotating the session ID while preserving valid session attributes.

#### Q8: What is the difference between an `HttpSession` attribute and a Cookie?
**Answer:**
- `HttpSession` attribute: Stored strictly on the **server** in memory. Capable of storing arbitrary Java objects (POJOs, Collections). Never exposed over the network.
- Cookie: Stored on the **client browser** as key-value string pairs (maximum 4KB each). Sent with every subsequent HTTP request to matching domains. Subject to client-side tampering unless signed/encrypted.

#### Q9: How does ShopSphere defend against SQL Injection (SQLi)?
**Answer:** ShopSphere guarantees 100% parameterization by using `java.sql.PreparedStatement` with positional placeholder tokens (`?`) for every database query across all 11 DAOs. User input is never concatenated into raw SQL strings. The database driver compiles the query blueprint first and treats parameters strictly as literal data values, completely neutralizing SQL meta-character payloads (e.g. `' OR 1=1 --`).

#### Q10: How does ShopSphere defend against Cross-Site Scripting (XSS)?
**Answer:**
1. All user-supplied output rendered in JSP views is escaped through standard JSTL `<c:out value="${param}"/>` or JSP EL auto-escaping, which converts HTML delimiters (`<`, `>`, `&`, `"`) into safe HTML entities (`&lt;`, `&gt;`, `&amp;`, `&quot;`).
2. `SecurityFilter` sets HTTP security headers: `X-XSS-Protection: 1; mode=block`, `X-Content-Type-Options: nosniff`, and `Content-Security-Policy`.

#### Q11: What hashing algorithm is used for user passwords in ShopSphere?
**Answer:** ShopSphere utilizes **PBKDF2 with HMAC-SHA256** (`PBKDF2WithHmacSHA256`) with 65,536 iterations and a 128-bit cryptographically secure pseudorandom salt generated via `java.security.SecureRandom`. Passwords are saved as `salt_hex$hash_hex` in MySQL, preventing dictionary and rainbow-table attacks.

---

### Category C: Servlet Filters & Event Listeners

#### Q12: What is a Servlet Filter, and how does the Filter Chain operate?
**Answer:** A Filter is a pluggable interceptor (`jakarta.servlet.Filter`) that intercepts requests before they reach servlets and inspects responses before they return to the client. The `FilterChain.doFilter()` method passes control to the next filter in the configured chain (ordered in `web.xml`) or finally to the target servlet.

#### Q13: What filters exist in ShopSphere?
**Answer:**
1. `EncodingFilter`: Forces UTF-8 character encoding on all requests and responses.
2. `SecurityFilter`: Injects browser defense headers and sets cache-control.
3. `AuthenticationFilter`: Intercepts protected routes (`/cart`, `/checkout`, `/orders`, `/wishlist`, `/profile`) and redirects unauthenticated users to `/login`.
4. `AdminFilter`: Role-based access control (RBAC) guard intercepting `/admin/*` and ensuring only users with `Role.ADMIN` enter.

#### Q14: What is a Servlet Context Listener vs an HTTP Session Listener?
**Answer:**
- `ServletContextListener`: Lifecycle listener that executes callback hooks when the web application is initialized (`contextInitialized`) or stopped (`contextDestroyed`). Used in ShopSphere for database connection verification and global startup logging.
- `HttpSessionListener`: Listens for session creation (`sessionCreated`) and expiration (`sessionDestroyed`). Used in `SessionListener` to maintain real-time active user counts.

---

### Category D: JavaServer Pages (JSP), EL & JSTL

#### Q15: Explain the JSP compilation lifecycle.
**Answer:**
1. **Translation:** When first requested, Tomcat's Jasper JSP compiler translates the `.jsp` text file into a Java source file (`_jsp.java` extending `HttpJspBase`, a subclass of `HttpServlet`).
2. **Compilation:** The `.java` file is compiled into a bytecode `.class` file.
3. **Loading & Initialization:** The container loads the servlet class, instantiates it, and invokes `_jspInit()`.
4. **Execution:** On each request, the container calls `_jspService(HttpServletRequest, HttpServletResponse)`.
5. **Cleanup:** When unloaded, `_jspDestroy()` is called.

#### Q16: What are the traditional JSP scripting elements?
**Answer:**
- **Directive (`<%@ ... %>`):** Gives instructions to the container (e.g. `<%@ page contentType="text/html" %>`, `<%@ taglib ... %>`, `<%@ include ... %>`).
- **Declaration (`<%! ... %>`):** Declares instance variables and helper methods within the generated servlet class.
- **Scriptlet (`<% ... %>`):** Raw Java statements inserted directly into the `_jspService()` method body.
- **Expression (`<%= ... %>`):** Evaluates a Java expression, converts the result to a string, and outputs it via `out.print(...)`.

#### Q17: Why are scriptlets discouraged in modern Java enterprise applications?
**Answer:** Scriptlets mix Java business logic with HTML presentation, making code difficult to read, maintain, and unit test. They violate MVC separation of concerns. In ShopSphere, business JSPs contain zero scriptlets, relying solely on Expression Language (EL) and JSTL.

#### Q18: What are the 9 JSP Implicit Objects?
**Answer:**
1. `request` (`HttpServletRequest`)
2. `response` (`HttpServletResponse`)
3. `out` (`JspWriter`)
4. `session` (`HttpSession`)
5. `application` (`ServletContext`)
6. `config` (`ServletConfig`)
7. `pageContext` (`PageContext`)
8. `page` (`Object` / `this`)
9. `exception` (`Throwable` — only available in error pages with `<%@ page isErrorPage="true" %>`)

#### Q19: Explain Expression Language (EL) scope resolution precedence.
**Answer:** When an expression like `${user}` is evaluated, EL searches the four scopes in order from narrowest to widest:
1. `pageScope` (`PageContext`)
2. `requestScope` (`HttpServletRequest`)
3. `sessionScope` (`HttpSession`)
4. `applicationScope` (`ServletContext`)
If not found in any scope, EL returns `null` (rendered as an empty string without throwing `NullPointerException`).

#### Q20: What are the 5 standard JSTL tag libraries?
**Answer:**
1. **Core (`c:`):** Flow control, loops, conditional branching (`<c:if>`, `<c:choose>`, `<c:forEach>`, `<c:set>`, `<c:out>`).
2. **Formatting & i18n (`fmt:`):** Locale-sensitive number, currency, and date formatting, resource bundle lookup.
3. **Functions (`fn:`):** String manipulation functions in EL (`fn:length`, `fn:contains`, `fn:toLowerCase`).
4. **XML (`x:`):** Parsing, evaluating, and transforming XML documents via XPath expressions.
5. **SQL (`sql:`):** Direct database querying and updates from JSP (demonstrated for academic purposes).

#### Q21: What is the difference between a Tag File and a Classic Tag Handler?
**Answer:**
- **Tag File (`.tag`):** Authored in JSP syntax within `/WEB-INF/tags/`. Rapid to develop, requires no Java compilation, and supports EL and HTML markup directly (e.g., `productCard.tag`, `alert.tag`).
- **Classic Tag Handler:** A compiled Java class extending `TagSupport` or `SimpleTagSupport` overriding `doStartTag()`, `doEndTag()`, or `doTag()`, configured via a Tag Library Descriptor (`.tld`). Used in ShopSphere for `<shop:price>` (`PriceTagHandler.java`).

---

### Category E: JDBC & Database Transactions

#### Q22: List the standard steps in plain JDBC execution.
**Answer:**
1. Load driver class (or rely on JDBC 4.0 SPI `DriverManager.getConnection()`).
2. Establish database `Connection` via `DriverManager.getConnection(url, user, pass)`.
3. Create `PreparedStatement` with SQL query.
4. Set input parameters using typed setters (`setString(1, ...)`, `setInt(2, ...)`).
5. Execute query (`executeQuery()` for SELECT, `executeUpdate()` for INSERT/UPDATE/DELETE).
6. Process the `ResultSet` cursor in a `while(rs.next())` loop.
7. Close `ResultSet`, `PreparedStatement`, and `Connection` in reverse order inside `try-with-resources`.

#### Q23: What is the difference between `Statement` and `PreparedStatement`?
**Answer:**
- `Statement`: Executes static SQL strings. Compiled by the database engine on every execution. Vulnerable to SQL injection when user parameters are concatenated.
- `PreparedStatement`: Pre-compiled by the database. Parameter values are sent separately, enabling query plan caching, faster batch execution, and immunity to SQL injection.

#### Q24: How are ACID transactions implemented in ShopSphere?
**Answer:** In `CheckoutService.processCheckout()`:
1. Disable automatic commit: `conn.setAutoCommit(false)`.
2. Execute inventory stock checks and decrements with row-level locks (`SELECT ... FOR UPDATE`).
3. Insert order header into `orders` and line items into `order_items`.
4. Insert payment record and update coupon usage.
5. Delete purchased items from `cart`.
6. If all operations succeed: `conn.commit()`.
7. If any exception occurs: `conn.rollback()` inside `catch (SQLException e)`.
8. In `finally` block: restore `conn.setAutoCommit(true)` and close connection.

---

### Category F: Java Networking & Sockets

#### Q25: What is the difference between TCP and UDP sockets in Java?
**Answer:**
- **TCP (`java.net.Socket`, `java.net.ServerSocket`):** Connection-oriented, reliable, byte-stream protocol. Implements 3-way handshake, packet sequencing, error checking, and retransmission. Used in ShopSphere's `InventoryServer`.
- **UDP (`java.net.DatagramSocket`, `java.net.DatagramPacket`):** Connectionless, unreliable, datagram protocol. No handshake or guaranteed delivery. Faster; used for video streaming or gaming.

#### Q26: How does ShopSphere handle multiple concurrent socket clients?
**Answer:** `InventoryServer` creates a fixed worker thread pool using `Executors.newFixedThreadPool(10)`. When `serverSocket.accept()` accepts an incoming client connection, the resulting `Socket` is wrapped inside a `ClientHandler` runnable and dispatched to the thread pool, ensuring non-blocking concurrent request handling without spawning unbounded threads.

#### Q27: How does a custom URL protocol handler work in Java?
**Answer:** Java's `java.net.URL` architecture allows registering custom protocols (e.g. `shop://`) by implementing `URLStreamHandler` and `URLConnection`. In `shopsphere-network`, `ShopURLStreamHandlerFactory` registers `shop://catalog/item/1`, parsing custom resource URLs into localized inventory lookups.

---

### Category G: Java RMI & Serialization

#### Q28: Explain the complete RMI Architecture.
**Answer:** RMI (Remote Method Invocation) enables an object running in one JVM to invoke methods on an object in another JVM.
- **Client Tier:** Calls methods on the **Remote Stub** (client proxy).
- **Remote Reference Layer (RRL):** Manages connection semantics (unicast).
- **Transport Layer:** TCP/IP socket connection handling byte streaming.
- **RMI Registry:** Naming directory service (port 1099) mapping service names to remote stubs.
- **Server Tier:** `Remote Skeleton` / dispatcher invokes actual method on the server implementation (`UnicastRemoteObject`).

#### Q29: What interfaces and classes must be extended to create an RMI service?
**Answer:**
1. The remote interface must extend `java.rmi.Remote` and all remote methods must declare `throws java.rmi.RemoteException`.
2. The server implementation class must implement the remote interface and typically extends `java.rmi.server.UnicastRemoteObject`.

#### Q30: What is the difference between TCP Sockets and RMI?
**Answer:**
- **Sockets:** Low-level byte-oriented communication. Developers must define custom text/binary protocols, serialize data manually, and parse strings.
- **RMI:** High-level object-oriented RPC. Allows calling remote Java methods directly with typed parameters and return values; parameter serialization is handled automatically by the RMI runtime.

#### Q31: What is Java Serialization, and what is the role of `serialVersionUID`?
**Answer:** Serialization converts the in-memory state of an object into a portable sequence of bytes (`ObjectOutputStream`), while deserialization reconstructs the object (`ObjectInputStream`). The `serialVersionUID` is a unique version identifier that ensures sender and receiver JVMs share compatible class definitions. If omitted, the compiler auto-generates one based on class structure, which can cause `InvalidClassException` upon small class changes.

#### Q32: What does the `transient` keyword do?
**Answer:** Marking a field as `transient` prevents it from being serialized during `ObjectOutputStream.writeObject()`. During deserialization, the field is restored to its default type value (`null`, `0`, or `false`). In ShopSphere, sensitive credentials and transient caches are marked `transient`.

---

### Category H: Java Swing & Desktop GUI MVC

#### Q33: What is the difference between AWT and Swing?
**Answer:**
- **AWT (Abstract Window Toolkit):** Heavyweight components backed by native OS GUI peers. Platform-dependent look and feel, limited set of widgets.
- **Swing:** Lightweight, pure Java components drawn directly onto canvases. Platform-independent, highly customizable, supports Pluggable Look and Feel (PLAF) and advanced components (`JTable`, `JTree`, `JTabbedPane`).

#### Q34: What is the Event Dispatch Thread (EDT) and why is it critical?
**Answer:** Swing components are **NOT** thread-safe. All UI creation, updates, and event listeners must run on a single dedicated background thread called the **Event Dispatch Thread (EDT)**. Updating UI components from background threads leads to race conditions and screen flickering.

#### Q35: How does `SwingWorker` work in ShopSphere?
**Answer:** `javax.swing.SwingWorker<T, V>` executes long-running background tasks (such as database queries or RMI calls) off the EDT:
1. `doInBackground()`: Runs on a background worker thread. Performs DB fetch.
2. `done()`: Automatically executes back on the EDT once `doInBackground()` finishes, allowing safe UI updates (e.g. populating `JTable` data or displaying dialogs).

#### Q36: How is MVC implemented in `shopsphere-swing-admin`?
**Answer:**
- **Model:** Domain classes (`Product`, `Order`) and custom `AbstractTableModel` implementations (`ProductTableModel`, etc.). Contains zero UI code.
- **View:** GUI frames and panels (`ProductPanel`, `AdminDashboard`) containing Swing controls. Contains zero database code.
- **Controller:** Action controllers (`ProductController`, `OrderController`) that listen to UI events, invoke background `SwingWorker` threads calling `ProductService`, and update the Models upon completion.

---

### Category I: JNDI & Internationalization (i18n)

#### Q37: What is JNDI, and why is it used for database connections in enterprise apps?
**Answer:** Java Naming and Directory Interface (JNDI) provides a unified API to access naming and directory services. In Tomcat, database connection pools are managed by the server container (`META-INF/context.xml`) and bound to `java:comp/env/jdbc/shopsphereDS`. Applications look up the DataSource via `new InitialContext().lookup(...)`. This decouples database credentials and pool configuration from application code.

#### Q38: How does Internationalization (i18n) work in Java?
**Answer:**
1. Text strings are extracted into properties resource bundles named `baseName_language_country.properties` (e.g. `messages_en.properties`, `messages_hi.properties`).
2. At runtime, Java loads the bundle matching the user's `Locale`: `ResourceBundle.getBundle("messages", locale)`.
3. In JSP, `<fmt:setLocale value="${sessionScope.lang}"/>` and `<fmt:message key="keyName"/>` render localized text dynamically.
4. Numbers, currency, and dates are formatted using `NumberFormat.getCurrencyInstance(locale)` and `DateTimeFormatter`.

---

### Category J: Java Applets & Modern Web Transition

#### Q39: What was the Applet lifecycle, and why are Applets obsolete today?
**Answer:**
- The classic Applet lifecycle consists of 5 methods: `init()`, `start()`, `paint(Graphics g)`, `stop()`, and `destroy()`.
- Applets are obsolete because web browsers dropped support for NPAPI plugins due to severe security vulnerabilities, high memory overhead, and sandboxing flaws. Java 9 deprecated Applets, and Java 17 removed the `Applet` API.
- Modern solutions: Single-Page Applications (HTML5/CSS3/JavaScript) on the web and standalone Swing/JavaFX desktop applications.

#### Q40: What is N-Tier Architecture, and how does ShopSphere embody it?
**Answer:** N-tier architecture separates an enterprise application into distinct physical and logical layers:
1. **Presentation Tier:** Browser (HTML5, Bootstrap 5) & Desktop Swing Client.
2. **Controller Tier:** Jakarta Servlets (`LoginServlet`, `ProductServlet`, `CheckoutServlet`).
3. **Business Logic Tier:** Pure Java Services (`UserService`, `ProductService`, `CheckoutService`).
4. **Data Access Tier:** Data Access Objects (`UserDAO`, `ProductDAO`, `OrderDAO`).
5. **Database / Persistence Tier:** MySQL 9.7 Database.
6. **Distributed Systems Tier:** TCP Socket Server (Port 5000) & Java RMI Registry (Port 1099).
Each tier communicates only with its adjacent tiers via well-defined contracts and interfaces.
