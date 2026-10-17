# ShopSphere — N-Tier Enterprise Architecture Diagram

This diagram visualizes the multi-tier J2EE/Jakarta EE architecture of **ShopSphere**, detailing the client presentation layer, servlet controllers, business services, DAO data access, persistence, and external distributed interfaces (Java RMI and TCP Sockets).

```mermaid
flowchart TD
    subgraph Clients["Presentation Tier (Clients)"]
        Browser["Web Browser (HTML5 / Bootstrap 5 / JS)"]
        SwingAdmin["Desktop Admin (Java Swing GUI / MVC)"]
        NetClient["Socket Inventory Client (TCP CLI)"]
        RmiClientApp["Distributed RMI Client"]
    end

    subgraph Container["Web Container & Distributed Middleware"]
        direction TB
        subgraph Filters["Filter Interceptor Chain"]
            EF["EncodingFilter (UTF-8)"]
            SF["SecurityFilter (Headers/CSRF)"]
            AF["AuthenticationFilter (Session Guard)"]
            ADMF["AdminFilter (Role RBAC Guard)"]
            EF --> SF --> AF --> ADMF
        end

        subgraph Controllers["Controller Tier (Jakarta Servlets & Endpoints)"]
            AuthServ["Login / Register / Logout Servlets"]
            ProdServ["ProductServlet / HomeServlet"]
            CartServ["CartServlet / WishlistServlet"]
            OrderServ["CheckoutServlet / OrderServlet"]
            AdminServ["AdminServlets (Dashboard, Products, Orders)"]
            DemoServ["Syllabus Demos (JSP, EL, JSTL, JNDI)"]
        end

        subgraph Sockets["Networking Subsystem"]
            SocketServer["InventoryServer (TCP Port 5000 / ThreadPool)"]
        end

        subgraph RmiTier["Remote Method Invocation"]
            RmiRegistry["RMI Registry (Port 1099)"]
            RmiService["InventoryServiceImpl (UnicastRemoteObject)"]
        end
    end

    subgraph CoreLayer["Business & Data Layer (Core Shared Library)"]
        direction TB
        subgraph Services["Service Tier (Business Logic & Transactions)"]
            UserService["UserService"]
            ProductService["ProductService"]
            CartService["CartService"]
            CheckoutService["CheckoutService (@Transactional)"]
            OrderService["OrderService"]
            CouponService["CouponService"]
            LocalInvService["LocalInventoryService"]
        end

        subgraph DAOs["Data Access Object (DAO) Tier"]
            UserDAO["UserDAO / UserDAOImpl"]
            ProductDAO["ProductDAO / ProductDAOImpl"]
            CartDAO["CartDAO / CartDAOImpl"]
            OrderDAO["OrderDAO / OrderDAOImpl"]
            PaymentDAO["PaymentDAO / PaymentDAOImpl"]
            CouponDAO["CouponDAO / CouponDAOImpl"]
        end

        subgraph DBUtilGroup["Connection Management"]
            DBUtilPool["DBUtil (JDBC DriverManager / JNDI DataSource)"]
        end
    end

    subgraph Storage["Enterprise Persistence Tier"]
        MySQL[("MySQL 9.7 Database (InnoDB 3NF)")]
    end

    %% Wiring Clients
    Browser -->|HTTP / HTTPS Port 8080| EF
    ADMF --> Controllers
    SwingAdmin -->|In-Process Method Calls (off EDT)| Services
    NetClient -->|TCP Socket Connection (Port 5000)| SocketServer
    RmiClientApp -->|RMI Protocol / Naming Lookup| RmiRegistry
    RmiRegistry -.->|Resolves Stub| RmiService

    %% Wiring Middleware to Services
    Controllers --> Services
    SocketServer --> LocalInvService
    RmiService --> LocalInvService

    %% Wiring Services to DAOs
    Services --> DAOs
    DAOs --> DBUtilPool

    %% Wiring DAOs to MySQL
    DBUtilPool -->|PreparedStatement / Plain JDBC| MySQL
```
