# ShopSphere — Architecture Modules & Responsibilities

This document defines the modular layout and separation of concerns across the ShopSphere project.

```
ShopSphere/
├── pom.xml                      (Maven Root Parent Project)
├── database/                    (SQL Schema, Seed Data, ER Diagrams)
├── docs/                        (Architecture, Requirements, Viva, Syllabus Mapping)
├── core/                        (Reusable Domain Models, DAOs, Services, Utilities)
├── web/                         (Jakarta EE 10 Servlet/JSP Web Application WAR)
├── swing-admin/                 (Java Swing Desktop Admin GUI with MVC)
├── network/                     (Socket Client-Server & Serialization Engine)
└── rmi/                         (RMI Distributed Remote Service & Client)
```

---

## 1. Module Specifications

### 1.1 `core` (JAR Module)
- **Role:** Pure Java business core and database persistence layer. UI-agnostic and container-agnostic.
- **Packages:**
  - `com.shopsphere.model`: Serializable POJO entities (`User`, `Address`, `Category`, `Product`, `CartItem`, `WishlistItem`, `Order`, `OrderItem`, `Payment`, `Review`, `Coupon`).
  - `com.shopsphere.dao`: Data Access Object interfaces and JDBC implementations with `try-with-resources` and `PreparedStatement`.
  - `com.shopsphere.service`: Business rules execution (authentication, stock verification, coupon evaluation, atomic order placement transaction, sales reports).
  - `com.shopsphere.util`: `DBUtil` (JDBC connection provider), `PasswordUtil` (PBKDF2 salted hash), `ValidationUtil`.
  - `com.shopsphere.exception`: `ShopSphereException` and custom domain exceptions.
- **Dependencies:** `mysql-connector-j`, `junit-jupiter` (test). Zero servlet or UI dependencies.

### 1.2 `web` (WAR Module)
- **Role:** Customer storefront and Web Admin portal running on Apache Tomcat 11.
- **Packages & Assets:**
  - `com.shopsphere.controller`: Jakarta Servlets handling HTTP requests, orchestrating services, and directing views.
  - `com.shopsphere.filter`: `AuthenticationFilter`, `AdminFilter`, `EncodingFilter`, `LoggingFilter`.
  - `com.shopsphere.listener`: `AppContextListener` (`ServletContextListener`), `SessionListener` (`HttpSessionListener`).
  - `com.shopsphere.tag`: Classic JSP Tag Extension handlers (e.g., `PriceTag`).
  - `webapp/WEB-INF/`: `web.xml`, tag library descriptors (`.tld`), reusable JSP tag files (`WEB-INF/tags/`).
  - `webapp/fragments/`: Reusable layout components (`header.jsp`, `navbar.jsp`, `footer.jsp`, `sidebar.jsp`).
  - `webapp/demo/`: Academic syllabus demonstration pages (JSP lifecycle & elements, EL scopes, JSTL XML/SQL, JNDI lookup).
- **Dependencies:** `core`, `jakarta.servlet-api` (provided), `jakarta.servlet.jsp-api` (provided), `jakarta.servlet.jsp.jstl-api`, `org.glassfish.web:jakarta.servlet.jsp.jstl`.

### 1.3 `swing-admin` (JAR Module)
- **Role:** Standalone desktop administration suite built on Java Swing.
- **Architecture:** Classic MVC Pattern.
  - **Model:** Core models and custom Swing `AbstractTableModel` implementations (`ProductTableModel`, `OrderTableModel`, etc.).
  - **View:** `LoginFrame`, `AdminDashboardFrame`, `ProductDialog`, `OrderDialog` using Java Swing components (`JFrame`, `JTable`, `JTabbedPane`, `JMenuBar`, `JComboBox`, `GridBagLayout`, etc.).
  - **Controller:** `LoginController`, `ProductController`, `OrderController` wiring user actions to Core services.
  - **Concurrency:** Long-running database operations isolated on worker threads using `javax.swing.SwingWorker` to ensure responsive UI.
- **Dependencies:** `core`.

### 1.4 `network` (JAR Module)
- **Role:** Low-level networking and Java object serialization demonstration.
- **Components:**
  - `InventoryServer`: Multithreaded TCP server listening on port 5000 via `java.net.ServerSocket` using `ExecutorService` thread pool.
  - `InventoryClient`: Interactive console TCP client sending commands (`GET_PRODUCT`, `GET_STOCK`, `SEARCH_PRODUCT`, `PING`).
  - `SerializationDemo`: Demonstrates `ObjectOutputStream` and `ObjectInputStream`, `Serializable` interface, `transient` attributes, and `serialVersionUID` validation.
  - `UrlDemo`: Demonstrates `java.net.URL`, `URLConnection`, HTTP header inspection, query encoding, and custom protocol stream handler concepts.
- **Dependencies:** `core`.

### 1.5 `rmi` (JAR Module)
- **Role:** Distributed computing and Remote Method Invocation.
- **Components:**
  - `InventoryRemote`: Remote interface extending `java.rmi.Remote` with `RemoteException`.
  - `InventoryServiceImpl`: Remote object implementation extending `UnicastRemoteObject` delegating to core DAOs.
  - `RmiServer`: Hosts RMI Registry on port 1099 (`LocateRegistry.createRegistry`) and binds remote service with `Naming.rebind`.
  - `RmiClient`: Performs `Naming.lookup` and invokes remote methods transparently across JVM boundaries.
- **Dependencies:** `core`.

### 1.6 `database`
- **Role:** Persistence definitions and data scripts.
- **Files:** `shopsphere.sql` (schema, indexes, foreign keys, triggers/constraints, rich seed data), `er-diagram.md`.
