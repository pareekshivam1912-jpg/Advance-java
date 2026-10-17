# ShopSphere — Enterprise Java E-Commerce Management System

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-11.0.25-yellow.svg)](https://tomcat.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-9.7%20%2F%208.0+-teal.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Apache%20Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)]()

> **ShopSphere** is a production-grade, multi-tier Enterprise Java E-Commerce Management System doubling as a reference capstone project for **Rajasthan Technical University (RTU) — 6CS4-04 Advanced Java**. It covers 100% of the university syllabus using pure Java and modern Jakarta EE 10 specifications without third-party web or ORM frameworks.

---

## 1. Architectural Highlights

- **N-Tier Architecture:** Complete physical and logical decoupling: Presentation &rarr; Controller &rarr; Business Services &rarr; Data Access (DAO) &rarr; Persistence (MySQL).
- **Zero ORM / Pure JDBC:** Plain JDBC using `java.sql.PreparedStatement` with 100% parameterization across 11 DAOs (zero SQL injection vulnerabilities).
- **ACID Transaction Boundaries:** Atomic multi-table checkout transactions in `CheckoutService` with programmatic row-level locking (`SELECT ... FOR UPDATE`), `commit()`, and `rollback()`.
- **Dual Clients:**
  - **Web Client:** Responsive HTML5 / Bootstrap 5 / Vanilla JS on Apache Tomcat 11 (`jakarta.*` namespace).
  - **Desktop Client:** Standalone Java Swing Admin application implementing strict MVC with background `SwingWorker` threads (EDT safety) and runtime Pluggable Look and Feel (PLAF).
- **Distributed Subsystems:**
  - **TCP Networking (`shopsphere-network`):** Multithreaded `ServerSocket` listening on port 5000 with a thread-pool worker dispatch, custom inventory command protocol, object serialization (`transient` fields, `serialVersionUID`), and custom URL protocol handler (`shop://`).
  - **Remote Method Invocation (`shopsphere-rmi`):** RMI remote interface, `UnicastRemoteObject`, RMI Registry (port 1099), and remote stub dispatch.
- **Enterprise Web Infrastructure:**
  - 4-Tier Filter Chain: `EncodingFilter` (UTF-8) &rarr; `SecurityFilter` (CSP, X-Frame) &rarr; `AuthenticationFilter` (Session Guard) &rarr; `AdminFilter` (RBAC).
  - Lifecycle Event Listeners: `SessionListener` (active session tracking) and `AppLifecycleListener`.
  - Session Fixation defense via `request.changeSessionId()` on login.
  - Salted PBKDF2 with HMAC-SHA256 password hashing.
  - Custom JSP Tag extensions: `<shop:price>` tag handler and reusable Tag Files (`productCard.tag`, `alert.tag`).
  - Internationalization (i18n): Runtime bilingual switching between English and Hindi (`messages_hi.properties`).
  - JNDI Naming: Container-managed DataSource lookup (`java:comp/env/jdbc/shopsphereDS`).

---

## 2. Multi-Module Project Structure

```
shopsphere/
├── pom.xml                 # Parent POM (BOM, compiler 21, dependency management)
├── database/
│   ├── shopsphere.sql      # Complete DDL (12 tables in 3NF) + rich seed data
│   └── er-diagram.md       # Entity-Relationship diagram & schema documentation
├── core/                   # Shared domain models, DAOs, services, and utilities
├── web/                    # Jakarta EE 10 WAR: Servlets, JSPs, JSTL, Tag Files, Filters
├── swing-admin/            # Java Swing Desktop Admin GUI (MVC + SwingWorker)
├── network/                # Multithreaded TCP Socket Server, Client & Serialization
├── rmi/                    # Java Remote Method Invocation Server, Client & Registry
├── docs/                   # Academic report, syllabus mapping, viva guide & diagrams
└── scripts/                # Automated PowerShell & Bash smoke test suites
```

---

## 3. Prerequisites & Environment

| Component | Required Version | Verified Local Environment |
|---|---|---|
| **Operating System** | Windows 10/11, Linux, or macOS | Windows 11 x64 |
| **Java Development Kit** | OpenJDK 17 LTS or higher | Microsoft OpenJDK 21.0.11 LTS |
| **Build Tool** | Apache Maven 3.8+ | Maven 3.9.16 |
| **Relational Database** | MySQL Server 8.0+ / 9.x | MySQL Server 9.7 (Port 3306) |
| **Application Server** | Apache Tomcat 10.1+ / 11.0+ | Apache Tomcat 11.0.25 (Port 8080) |

---

## 4. Quick Start Guide

### Step 1: Database Setup
Import the database schema and seed data into your MySQL server:

```bash
# Windows / Linux / macOS
mysql -u root -p < database/shopsphere.sql
```

Ensure `core/src/main/resources/db.properties` reflects your local MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=YourPasswordHere#
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 2: Build the Multi-Module Project
From the repository root directory:
```bash
mvn clean package
```
*This compiles all 6 modules, runs all 32 automated tests, and generates the web WAR and executable JARs.*

### Step 3: Deploy & Run the Web Application
1. Copy `web/target/shopsphere.war` to Tomcat's `webapps/` directory:
   ```cmd
   copy web\target\shopsphere.war "%CATALINA_HOME%\webapps\"
   ```
2. Start Apache Tomcat:
   ```cmd
   "%CATALINA_HOME%\bin\startup.bat"    # Windows
   $CATALINA_HOME/bin/startup.sh        # Linux / macOS
   ```
3. Open your browser and navigate to:
   - **Storefront:** [http://localhost:8080/shopsphere/](http://localhost:8080/shopsphere/)
   - **Admin Management:** [http://localhost:8080/shopsphere/admin/dashboard](http://localhost:8080/shopsphere/admin/dashboard)
   - **Syllabus Academic Demo Hub:** [http://localhost:8080/shopsphere/demo/](http://localhost:8080/shopsphere/demo/)

---

## 5. Running Desktop & Distributed Modules

### 5.1 Java Swing Desktop Admin (MVC)
Launch the standalone executable fat JAR:
```bash
java -jar swing-admin/target/shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar
```
*(Or run via Maven: `mvn -pl swing-admin exec:java "-Dexec.mainClass=com.shopsphere.swing.SwingAdminApp"`)*

### 5.2 Multithreaded TCP Socket Server & Client
Open two terminal windows:
```bash
# Terminal 1: Start Socket Server (Port 5000)
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.server.InventoryServer"

# Terminal 2: Run Inventory Client
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.client.InventoryClient"
```

### 5.3 Java Remote Method Invocation (RMI)
Open two terminal windows:
```bash
# Terminal 1: Start RMI Server & Registry (Port 1099)
mvn -pl rmi exec:java "-Dexec.mainClass=com.shopsphere.rmi.server.RmiServer"

# Terminal 2: Run RMI Client
mvn -pl rmi exec:java "-Dexec.mainClass=com.shopsphere.rmi.client.RmiClient"
```

---

## 6. Default Login Credentials

| Role | Email Address | Password | Permissions & Scope |
|---|---|---|---|
| **Administrator** | `admin@shopsphere.com` | `Admin@123` | Full access to Web Admin, Swing Desktop Admin, Catalog CRUD, Order fulfillment |
| **Customer** | `customer@shopsphere.com` | `Customer@123` | Storefront browsing, Shopping cart, Wishlist, Checkout, Order history, Reviews |
| **Customer (Jaipur)** | `rahul@shopsphere.com` | `Customer@123` | Sample orders and pre-configured shipping address |
| **Customer (Udaipur)** | `priya@shopsphere.com` | `Customer@123` | Sample orders and pre-configured shipping address |

---

## 7. RTU Academic Syllabus Demo Hub

Navigate to `http://localhost:8080/shopsphere/demo/` to access live, interactive demonstrations of all university syllabus requirements:

1. **JSP Traditional Elements:** Scriptlets, expressions, declarations, directives, and 9 implicit objects.
2. **Expression Language (EL):** Property access, operators, and 4-tier scope resolution (`pageScope` &rarr; `requestScope` &rarr; `sessionScope` &rarr; `applicationScope`).
3. **JSTL XML Processing:** Dynamic parsing of XML feeds via `<x:parse>`, `<x:forEach>`, and XPath expressions.
4. **JSTL SQL Tag Library:** Direct database queries via `<sql:query>` with educational comparison against MVC.
5. **Tomcat JNDI DataSource:** `InitialContext.lookup("java:comp/env/jdbc/shopsphereDS")` pooled connection verification.
6. **Internationalization (i18n):** Real-time bilingual switching (English / हिंदी) via `ResourceBundle` and `fmt:message`.
7. **Session & Cookie Inspector:** Live inspection of `HttpSession` ID, creation/access timestamps, timeouts, and client cookies.

---

## 8. Documentation Directory Map

Detailed academic and technical documentation is available in `docs/`:

- [docs/REPORT.md](file:///d:/Advnace%20java/ShopSphere/docs/REPORT.md) — Complete 15-section RTU Capstone University Project Report.
- [docs/VIVA.md](file:///d:/Advnace%20java/ShopSphere/docs/VIVA.md) — 45+ comprehensive viva voce questions with model answers + 14-step live demonstration checklist.
- [docs/SYLLABUS_MAPPING.md](file:///d:/Advnace%20java/ShopSphere/docs/SYLLABUS_MAPPING.md) — 29-topic RTU syllabus mapping matrix with exact file pointers.
- [docs/TEST_REPORT.md](file:///d:/Advnace%20java/ShopSphere/docs/TEST_REPORT.md) — Full automated and manual test matrix, security attack analysis, and results.
- [docs/SETUP.md](file:///d:/Advnace%20java/ShopSphere/docs/SETUP.md) — Comprehensive cross-platform installation and deployment manual.
- [docs/J2EE_NTIER.md](file:///d:/Advnace%20java/ShopSphere/docs/J2EE_NTIER.md) — Deep architectural guide to J2EE/Jakarta EE and Tomcat pipelines.
- [docs/diagrams/](file:///d:/Advnace%20java/ShopSphere/docs/diagrams/) — 8 detailed Mermaid diagrams (ER, Architecture, Use Case, Class, Login Seq, Order Seq, RMI Seq, Socket Flow).
- [docs/screenshots/README.md](file:///d:/Advnace%20java/ShopSphere/docs/screenshots/README.md) — Screenshot capture instructions and captions for project submission.

---

## 9. Troubleshooting & Common Questions

- **Port Conflicts:**
  - *Port 8080 (Tomcat):* If occupied, modify port in `conf/server.xml`.
  - *Port 3306 (MySQL):* Ensure local MySQL service (`MySQL97` or `MySQL80`) is running.
  - *Port 5000 (Sockets) / 1099 (RMI):* Ensure no lingering background process holds these ports.
- **`jakarta.*` vs `javax.*` ClassNotFoundException:**
  - ShopSphere requires **Tomcat 10.1+ or 11.0+**. Deploying on Tomcat 9 or older will fail with `ClassNotFoundException: jakarta.servlet.http.HttpServlet`.
- **MySQL Public Key Retrieval:**
  - If MySQL reports `Public Key Retrieval is not allowed`, verify `allowPublicKeyRetrieval=true` in `db.properties`.
- **PowerShell Parameter Syntax:**
  - In Windows PowerShell, wrap JVM parameters in double quotes: `mvn -pl rmi exec:java "-Dexec.mainClass=..."`.

---

## 10. License & Academic Attribution
Developed as an open academic reference project for computer science engineering students. All original code is freely usable for learning, academic coursework, and university project submissions.
