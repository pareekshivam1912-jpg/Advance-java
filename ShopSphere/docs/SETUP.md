# ShopSphere — Installation, Deployment & Operations Guide

**Project:** ShopSphere Enterprise Java E-Commerce Management System  
**Academic Alignment:** Rajasthan Technical University (RTU) — 6CS4-04 Advanced Java  
**Target Environment:** Cross-Platform (Windows 10/11, Ubuntu/Debian/RHEL Linux, macOS)  

---

## 1. System Requirements & Prerequisites

Before running ShopSphere, verify that the following runtime tools are installed:

| Component | Minimum Version | Recommended Version | Verification Command |
|---|---|---|---|
| **Java Development Kit (JDK)** | OpenJDK 17 LTS | OpenJDK 21 LTS | `java -version` |
| **Apache Maven** | 3.8.0+ | 3.9.6+ | `mvn -version` |
| **MySQL Server** | 8.0+ | 9.0+ / 9.7 Server | `mysql --version` |
| **Apache Tomcat** | 10.1.x (Jakarta EE 10) | 11.0.25 (Servlet 6.1) | `catalina.bat version` / `catalina.sh version` |
| **Docker (Optional)** | 24.0+ | Latest Desktop / Engine | `docker compose version` |

> [!IMPORTANT]
> **Jakarta EE 10 Namespace Constraint**: ShopSphere uses Tomcat 11 with the modern `jakarta.*` namespace (`jakarta.servlet`, `jakarta.servlet.http`, `jakarta.servlet.jsp`). It will **not** deploy on legacy Tomcat 9 or older containers that expect `javax.*`.

---

## 2. Database Setup & Initialization

### Step 2.1: Initialize MySQL Schema & Seed Data
ShopSphere comes with a complete DDL script `database/shopsphere.sql` that defines 12 relational tables, indexes, cascading foreign keys, check constraints, and seed data.

#### On Windows (PowerShell / Command Prompt):
```powershell
# Log into MySQL CLI
mysql -u root -p

# Within MySQL prompt, execute:
source d:/Advnace java/ShopSphere/database/shopsphere.sql;
exit;
```

#### On Linux / macOS (Terminal):
```bash
mysql -u root -p < database/shopsphere.sql
```

### Step 2.2: Configure JDBC Connection Properties
Verify and update `core/src/main/resources/db.properties` to match your local MySQL configuration:

```properties
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=YourPasswordHere#
db.driver=com.mysql.cj.jdbc.Driver
```

---

## 3. Building the Multi-Module Project

From the project root directory (`d:\Advnace java\ShopSphere` or project clone location):

```bash
# Clean build and package all modules (core, web, swing-admin, network, rmi)
mvn clean package
```

### Build Artifacts Produced:
- `core/target/shopsphere-core-1.0.0.jar` — Shared models, DAOs, and service singletons.
- `web/target/shopsphere.war` — Complete Jakarta EE web application archive.
- `swing-admin/target/shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar` — Standalone desktop admin fat JAR.
- `network/target/shopsphere-network-1.0.0.jar` — Multithreaded TCP inventory server and client.
- `rmi/target/shopsphere-rmi-1.0.0.jar` — RMI remote service interface, implementation, registry, and client.

---

## 4. Deploying the Web Application on Apache Tomcat 11

### Step 4.1: Manual WAR Deployment
1. Ensure Apache Tomcat 11 is installed. (e.g., `C:\Users\Admin\Downloads\apache-tomcat-11.0.25-windows-x64\apache-tomcat-11.0.25`).
2. Copy `web/target/shopsphere.war` into Tomcat's `webapps/` folder:
   - **Windows:**
     ```cmd
     copy web\target\shopsphere.war "C:\path\to\apache-tomcat-11.0.25\webapps\"
     ```
   - **Linux / macOS:**
     ```bash
     cp web/target/shopsphere.war /opt/tomcat/webapps/
     ```

### Step 4.2: Start Tomcat
- **Windows:**
  ```cmd
  "C:\path\to\apache-tomcat-11.0.25\bin\startup.bat"
  ```
- **Linux / macOS:**
  ```bash
  /opt/tomcat/bin/startup.sh
  ```

### Step 4.3: Access Web Application
Open your web browser and navigate to:
```
http://localhost:8080/shopsphere/
```

- **Customer Storefront:** `http://localhost:8080/shopsphere/`
- **Admin Management Portal:** `http://localhost:8080/shopsphere/admin/dashboard`
- **RTU Syllabus Academic Demo Hub:** `http://localhost:8080/shopsphere/demo/`

---

## 5. Running Desktop & Distributed Modules

### 5.1 Java Swing Desktop Admin (MVC)

#### Option A: Running the Standalone Executable Fat JAR (Recommended)
```bash
java -jar swing-admin/target/shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar
```

#### Option B: Running via Maven
```bash
# Windows PowerShell (note quotes around JVM argument)
mvn -pl swing-admin exec:java "-Dexec.mainClass=com.shopsphere.swing.SwingAdminApp"

# Linux / macOS Bash
mvn -pl swing-admin exec:java -Dexec.mainClass="com.shopsphere.swing.SwingAdminApp"
```

---

### 5.2 Java TCP Socket Networking (`shopsphere-network`)

Demonstrates `java.net.ServerSocket`, `java.net.Socket`, multi-threading with thread pools, and object stream serialization.

#### Terminal 1 — Start Multithreaded Inventory Server (Port 5000):
```bash
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.server.InventoryServer"
```
*Console output:* `[InventoryServer] Server listening on TCP port 5000 with thread pool of 10 workers...`

#### Terminal 2 — Run Inventory Client:
```bash
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.client.InventoryClient"
```
*Console output:*
```text
Connecting to InventoryServer on localhost:5000...
Sending command: CHECK_STOCK:1
Server Response: SUCCESS:STOCK:25:ThinkPad X1 Carbon Gen 11
Sending command: UPDATE_STOCK:1:5
Server Response: SUCCESS:UPDATED:30:ThinkPad X1 Carbon Gen 11
```

#### Additional Demos in Network Module:
```bash
# Object Serialization Demo (demonstrates transient fields and serialVersionUID)
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.demo.SerializationDemo"

# Custom URL Protocol Handler Demo (shop://catalog/item/1)
mvn -pl network exec:java "-Dexec.mainClass=com.shopsphere.network.demo.UrlDemo"
```

---

### 5.3 Java Remote Method Invocation (`shopsphere-rmi`)

Demonstrates `java.rmi.Remote`, `UnicastRemoteObject`, `LocateRegistry`, and `Naming` lookup.

#### Terminal 1 — Start RMI Server & Registry (Port 1099):
```bash
mvn -pl rmi exec:java "-Dexec.mainClass=com.shopsphere.rmi.server.RmiServer"
```
*Console output:* `[RmiServer] LocateRegistry created on port 1099. Bound 'InventoryService' successfully.`

#### Terminal 2 — Run RMI Client:
```bash
mvn -pl rmi exec:java "-Dexec.mainClass=com.shopsphere.rmi.client.RmiClient"
```
*Console output:*
```text
Looking up 'rmi://localhost:1099/InventoryService'...
RMI Stub acquired: Proxy[InventoryService, ...]
Current stock for Product ID 1: 30 units
Invoking remote updateStock(1, -5)...
New stock after deduction: 25 units
```

---

## 6. Docker Container Deployment (Alternative)

For automated zero-configuration deployment of both MySQL 9 and Tomcat 11:

```bash
# 1. Build the WAR file first
mvn clean package

# 2. Launch MySQL 9 and Tomcat 11 containers
docker compose up -d

# 3. Check container status
docker compose ps
```
The application will be live at `http://localhost:8080/shopsphere/`.

---

## 7. Default Credentials & Test Accounts

| Role | Email | Password | Access Privileges |
|:---|:---|:---|:---|
| **System Administrator** | `admin@shopsphere.com` | `Admin@123` | Full access to Web Admin, Swing Desktop Admin, Product/Category CRUD, Order status modification |
| **Customer 1** | `customer@shopsphere.com` | `Customer@123` | Storefront browsing, Shopping cart, Wishlist, Checkout, Order tracking, Reviews |
| **Customer 2** | `rahul@shopsphere.com` | `Customer@123` | Pre-populated orders, delivery addresses in Jaipur |
| **Customer 3** | `priya@shopsphere.com` | `Customer@123` | Pre-populated orders, delivery addresses in Udaipur |

---

## 8. Troubleshooting & Common Pitfalls

### 1. Port Conflicts
- **Port 8080 (Tomcat):** If port 8080 is already used (e.g. by Jenkins or Oracle XE), edit `conf/server.xml` in Tomcat and change `<Connector port="8080"` to `8088`.
- **Port 3306 (MySQL):** Verify service name `MySQL97` or `MySQL80` is running in Windows Services (`services.msc`) or via `systemctl status mysql` on Linux.
- **Port 5000 (Socket Server):** Used by `InventoryServer`. Ensure no duplicate server instance is running.
- **Port 1099 (RMI Registry):** Used by `RmiServer`. Terminate any previous RMI server before starting a new one.

### 2. `jakarta.*` vs `javax.*` ClassNotFoundException
If deploying on an external server, ensure you are using **Apache Tomcat 10.1+ or 11.0+**. Tomcat 9.0 and earlier only support the legacy `javax.servlet` namespace, which will cause `ClassNotFoundException: jakarta.servlet.http.HttpServlet`.

### 3. MySQL Public Key Retrieval Error
If MySQL throws `Public Key Retrieval is not allowed`, ensure `allowPublicKeyRetrieval=true` is present in `core/src/main/resources/db.properties`. This is already pre-configured in ShopSphere.

### 4. PowerShell Parameter Quoting
In Windows PowerShell, parameters containing hyphens and property dots (such as `-Dexec.mainClass=...`) must be enclosed in double quotes:
```powershell
mvn -pl rmi exec:java "-Dexec.mainClass=com.shopsphere.rmi.server.RmiServer"
```
