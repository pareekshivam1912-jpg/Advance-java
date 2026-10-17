# ShopSphere — N-Tier Architecture & System Flow

## 1. Overall System Architecture

ShopSphere adheres to a pure **N-Tier Enterprise Architecture** separating presentation, control, domain logic, and data persistence without framework lock-in.

```mermaid
flowchart TD
    subgraph Clients["Clients Layer"]
        B["Web Browser (HTML5 / Bootstrap 5 / JS)"]
        SW["Desktop Admin GUI (Java Swing MVC)"]
        CLI["Socket / RMI Inventory Clients"]
    end

    subgraph Presentation["Presentation & Controller Layer (Tomcat 11)"]
        JSP["Jakarta Pages (JSP + EL + JSTL Tags)"]
        SRV["Jakarta Servlet Controllers"]
        FLT["Filters (Auth, Admin, Encoding, Logging)"]
        LST["Listeners (AppContext, SessionListener)"]
    end

    subgraph Business["Service Layer (core/service)"]
        SF["ServiceFactory"]
        US["UserService"]
        PS["ProductService"]
        CS["CartService"]
        CK["CheckoutService (Atomic Transaction)"]
        CP["CouponService"]
        OS["OrderService"]
        RS["ReviewService"]
        LIS["LocalInventoryService"]
    end

    subgraph DataAccess["Data Access Layer (core/dao)"]
        UDAO["UserDAO"]
        PDAO["ProductDAO"]
        CDAO["CartDAO"]
        ODAO["OrderDAO"]
        CPDAO["CouponDAO"]
        DB["DBUtil (DriverManager)"]
    end

    subgraph Database["Database Layer"]
        MYSQL[(MySQL 8.0+ / 9.x Database)]
    end

    B -->|HTTP Requests| FLT
    FLT --> SRV
    SRV -->|Forward / Redirect| JSP
    JSP -->|Response HTML| B

    SRV --> SF
    SW --> SF
    CLI --> SF

    SF --> US & PS & CS & CK & CP & OS & RS & LIS

    US & PS & CS & CK & CP & OS & RS & LIS --> UDAO & PDAO & CDAO & ODAO & CPDAO
    UDAO & PDAO & CDAO & ODAO & CPDAO --> DB
    DB -->|PreparedStatement JDBC| MYSQL
```

---

## 2. Web Request Flow (N-Tier Pipeline)

The strict web lifecycle follows:

```
Browser  ───▶  Servlet Filter (Auth / Logging / Encoding)
                      │
                      ▼
               Servlet Controller (doGet / doPost)
                      │ (Extracts input, validates format)
                      ▼
               Service Layer (Business rules, stock, totals)
                      │
                      ▼
               DAO Layer (PreparedStatement only)
                      │
                      ▼
               MySQL Database (Atomic Commit / Rollback)
                      │
       (Returns Domain Model / DTO to Controller)
                      ▼
   Servlet sets request / session attributes
   Follows Post-Redirect-Get (PRG) for state-changing forms
   Forwards (RequestDispatcher.forward) to JSP
                      ▼
   JSP renders response via EL and JSTL (Zero Java code <% %>)
                      ▼
                 HTML Browser
```

```mermaid
sequenceDiagram
    autonumber
    actor Customer as Browser
    participant Filter as AuthenticationFilter
    participant Servlet as CheckoutServlet
    participant Service as CheckoutService
    participant DAO as OrderDAO / ProductDAO
    participant DB as MySQL Database
    participant View as checkout.jsp / order-success.jsp

    Customer->>Filter: POST /checkout (place order)
    Filter->>Servlet: doFilter() -> chain
    Servlet->>Service: placeOrder(userId, addressId, coupon, payment)
    Service->>DAO: recomputeSummary() from DB prices
    DAO->>DB: SELECT products / cart items
    Service->>DAO: insert(conn, order), updateStock(conn)
    DAO->>DB: ATOMIC COMMIT
    Service-->>Servlet: Order created (#101)
    Servlet->>Customer: 302 Redirect to /order-success?id=101
    Customer->>Servlet: GET /order-success?id=101
    Servlet->>View: forward (order attribute)
    View-->>Customer: Render Order Confirmation (HTML)
```

---

## 3. Swing Desktop Admin MVC Pattern

The desktop administration tool mirrors the same N-Tier boundaries:

```mermaid
flowchart LR
    subgraph View["View (swing-admin/view)"]
        LF["LoginFrame"]
        DF["AdminDashboardFrame"]
        PT["ProductTable (JTable)"]
    end

    subgraph Controller["Controller (swing-admin/controller)"]
        LC["LoginController"]
        PC["ProductController"]
        SWK["SwingWorker (Background Worker)"]
    end

    subgraph Model["Model & Core Services"]
        TM["ProductTableModel (AbstractTableModel)"]
        SF["ServiceFactory (ProductService, UserService)"]
        DAO["ProductDAOImpl (JDBC)"]
    end

    DF -->|Button Click| PC
    PC -->|Offloads from EDT| SWK
    SWK -->|Calls| SF
    SF --> DAO
    DAO -->|Data rows| TM
    TM -->|Fires fireTableDataChanged| PT
```

- **EDT Safety:** Any network or database call runs on a separate worker thread (`SwingWorker`).
- **Separation:** Views contain zero SQL or JDBC calls; controllers interact only with `core` services.
