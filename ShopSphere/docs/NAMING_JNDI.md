# ShopSphere — Naming & Directory Services: JNDI & RMI Naming (`docs/NAMING_JNDI.md`)

This document provides the definitive academic and architectural guide to **Naming and Directory Services** in Java enterprise systems, fulfilling the RTU Advanced Java syllabus requirements for **JNDI (Java Naming and Directory Interface)** and **RMI Naming**.

---

## 1. Executive Conceptual Overview

In distributed and multi-tier enterprise systems, components (servlets, clients, services) must locate resources (databases, message queues, remote service stubs, environment properties) without hardcoding physical IP addresses, port numbers, or credentials.

Java provides two complementary naming paradigms:
1. **JNDI (`javax.naming.*`)**: An enterprise-grade, protocol-independent Service Provider Interface (SPI) enabling Java applications to discover heterogeneous directory objects, managed connection pools, and container-injected configurations.
2. **RMI Naming (`java.rmi.Naming` & `java.rmi.registry.Registry`)**: A specialized, lightweight remote object registry tailored specifically for publishing and looking up Java Remote Method Invocation (RMI) object stubs across JVM processes.

---

## 2. JNDI Architecture in ShopSphere

In the ShopSphere web tier (`shopsphere.war`), Apache Tomcat acts as the JNDI Container Context:

```
+-------------------------------------------------------------------------+
| Apache Tomcat 11 Web Container (JNDI Provider)                          |
|                                                                         |
|  [META-INF/context.xml]                                                 |
|    ├── <Resource name="jdbc/shopsphereDS" .../>  (Connection Pool)     |
|    ├── <Environment name="academicEnvironment" .../>                    |
|    └── <Environment name="maxUploadSizeMB" .../>                        |
+-------------------------------------------------------------------------+
                                    ▲
                                    │ JNDI SPI Lookup
                                    │ "java:comp/env/jdbc/shopsphereDS"
+-----------------------------------+-------------------------------------+
| ShopSphere Web Application                                              |
|                                                                         |
|   Context initCtx = new InitialContext();                               |
|   Context envCtx  = (Context) initCtx.lookup("java:comp/env");          |
|   DataSource ds   = (DataSource) envCtx.lookup("jdbc/shopsphereDS");    |
|   Connection conn = ds.getConnection(); // Borrowed from Pool           |
+-------------------------------------------------------------------------+
```

### Key Components:
- **`InitialContext`**: The starting point for naming operations. In Tomcat, it is backed by `org.apache.naming.java.javaURLContextFactory`.
- **`java:comp/env/`**: The standard Jakarta EE application environment subcontext prefix. Isolates each web app's namespace from sibling web applications.
- **Connection Pool (`javax.sql.DataSource`)**: Instead of creating costly physical TCP connections via `DriverManager.getConnection()`, `DataSource` dispenses pre-authenticated connections from a managed pool (`maxTotal=25`, `maxIdle=10`), drastically cutting query latency.

---

## 3. RMI Naming Architecture in ShopSphere

In the ShopSphere distributed inventory module (`rmi`), `java.rmi.Naming` is used to register and discover remote server stubs:

```
[Server JVM]                                 [Client JVM]
InventoryServiceImpl (UnicastRemoteObject)   RmiClient
        │                                         │
        ▼                                         ▼
 Naming.rebind("rmi://localhost:1099/            Naming.lookup("rmi://localhost:1099/
               InventoryService", impl)                         InventoryService")
        │                                         │
        └─────────────────► [RMI Registry] ◄──────┘
                            (Port 1099)
```

---

## 4. Architectural Comparison: JNDI vs RMI Naming

| Dimension | JNDI (`javax.naming.*`) | RMI Naming (`java.rmi.Naming`) |
|---|---|---|
| **Primary Scope** | Multi-tier Jakarta EE container resources (DataSources, JMS, Mail, Env) | Distributed remote procedure call (RMI) object stubs |
| **Backing Technology** | Pluggable SPI (Tomcat JNDI, LDAP, Active Directory, DNS, File System) | `rmiregistry` / in-process `LocateRegistry` |
| **Lookup Syntax** | `new InitialContext().lookup("java:comp/env/jdbc/shopsphereDS")` | `Naming.lookup("rmi://localhost:1099/InventoryService")` |
| **Protocol** | In-process container memory or LDAP / IIOP | TCP / Java Remote Method Protocol (JRMP) |
| **Objects Stored** | DataSources, EJBs, Strings, Integers, JMS ConnectionFactories | Remote Stubs (Dynamic Proxies implementing `Remote`) |
| **Security / Auth** | Container-managed authentication (`auth="Container"`) | JVM SecurityManager (historically) / TLS / Network firewall |

---

## 5. How to Run the Demonstrations

### A. JNDI Web Demonstration
1. Deploy `shopsphere.war` to Apache Tomcat 11.
2. Navigate to: `http://localhost:8080/shopsphere/demo/jndi-demo.jsp`
3. The page executes:
   - `lookup("java:comp/env/academicEnvironment")` &rarr; displays runtime environment string.
   - `lookup("java:comp/env/maxUploadSizeMB")` &rarr; displays integer limit.
   - `lookup("java:comp/env/jdbc/shopsphereDS")` &rarr; borrows connection, queries `SELECT COUNT(*) FROM products`, and reports pool status.

### B. RMI Naming Demonstration
1. Start RMI Server:
   ```powershell
   mvn exec:java -pl rmi -Dexec.mainClass="com.shopsphere.rmi.RmiServer"
   ```
2. In a second terminal, run RMI Client:
   ```powershell
   mvn exec:java -pl rmi -Dexec.mainClass="com.shopsphere.rmi.RmiClient"
   ```
   Verifies `Naming.lookup()` and `LocateRegistry.getRegistry().lookup()` resolving the remote service stub.

---

## 6. Key Viva Examination Questions & Answers

1. **What is the significance of the `java:comp/env` prefix in JNDI?**
   - It represents the Component Environment context defined by the Jakarta EE specification. It ensures that each web application deployed on a shared container has its own private, isolated naming hierarchy preventing naming collisions between applications.
2. **Why is a JNDI DataSource preferred over `DriverManager` in enterprise web applications?**
   - `DriverManager` opens a new physical connection for every request and closes it after, which has high latency (TCP handshake, SSL negotiation, DB authentication). A JNDI `DataSource` manages an active pool of reusable connections, validates connection health, and supports container-managed transactions.
3. **Can JNDI be used to look up RMI objects?**
   - Yes! JNDI includes an RMI Registry Service Provider (`com.sun.jndi.rmi.registry.RegistryContextFactory`), allowing lookups like `ctx.lookup("rmi://localhost:1099/InventoryService")`. However, `java.rmi.Naming` is a simpler, direct shortcut for pure RMI workflows.
