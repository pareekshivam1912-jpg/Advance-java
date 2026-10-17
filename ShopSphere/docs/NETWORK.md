# ShopSphere — Java Networking & Serialization Subsystem (`network`)

This document covers the **Java Networking** and **Object Serialization** subsystem of ShopSphere, satisfying the RTU Advanced Java curriculum topics: `java.net`, `Socket`, `ServerSocket`, multithreaded client-server architecture, `ObjectOutputStream` / `ObjectInputStream`, `serialVersionUID`, and URL/protocol handlers.

---

## 1. Architecture Overview

ShopSphere includes a standalone TCP inventory service that allows warehouse clients or external inventory scanners to query stock and product details over raw sockets using Java Object Serialization:

```
+---------------------------------------+
|  InventoryClient                      |
|  - ObjectOutputStream (commands)      |
|  - ObjectInputStream (Product objects)|
+---------------------------------------+
                  │
                  ▼  TCP Port 5000
+---------------------------------------+
|  InventoryServer (ServerSocket: 5000) |
|  - Thread Pool (ExecutorService)      |
|  - ClientHandler (isolated thread)    |
|  - Delegates to ServiceFactory        |
+---------------------------------------+
                  │
                  ▼
+---------------------------------------+
|  Core Service Layer / MySQL Database  |
+---------------------------------------+
```

---

## 2. Text-Object Protocol Specification

The TCP communication on port `5000` adheres to the following request-response contracts:

| Client Command | Server Response Type | Description |
|---|---|---|
| `PING` | `String` | Returns `"PONG"` to verify network liveness. |
| `GET_PRODUCT <id>` | `Product` (Serialized) or `String` | Returns fully deserialized `Product` domain entity or error message if not found. |
| `GET_STOCK <id>` | `Integer` | Returns current available warehouse inventory count. |
| `SEARCH_PRODUCT <keyword>` | `ArrayList<Product>` (Serialized) | Returns list of matching active products. |
| `EXIT` | `String` | Gracefully closes connection with `"BYE"`. |

> **Deadlock Prevention Note:** To prevent Java Object Stream handshake deadlocks, both `InventoryServer` and `InventoryClient` construct and flush their `ObjectOutputStream` *before* constructing their `ObjectInputStream`.

---

## 3. How to Run the Networking Demonstrations

All network commands can be executed directly via Maven from the project root (`ShopSphere`):

### A. Run TCP Inventory Server
Open a terminal and start the server:
```powershell
mvn exec:java -pl network -Dexec.mainClass="com.shopsphere.network.InventoryServer"
```
The server will bind to port `5000` and wait for concurrent connections:
```
==================================================================
  ShopSphere TCP Inventory Server Started on Port 5000
  Protocol: java.net.ServerSocket + Object Serialization
  Ready for concurrent client connections...
==================================================================
```

### B. Run TCP Inventory Client (Interactive Console & Automated Suite)
In a second terminal, execute:
```powershell
mvn exec:java -pl network -Dexec.mainClass="com.shopsphere.network.InventoryClient"
```
You can execute automated test passes or switch to an interactive CLI menu to query products:
```
--- ShopSphere TCP Inventory Client ---
1. Ping Server
2. Get Product by ID (Object Transmission)
3. Get Stock Quantity
4. Search Products
5. Run Automated Protocol Test Suite
0. Exit
```

### C. Run Serialization Demo (`SerializationDemo`)
```powershell
mvn exec:java -pl network -Dexec.mainClass="com.shopsphere.network.SerializationDemo"
```
Demonstrates:
1. Writing a `Product` entity to binary file `product.ser` using `ObjectOutputStream`.
2. Verifying that the `transient String sessionViewToken` field is stripped and deserialized as `null`.
3. Verifying that `serialVersionUID = 1L` preserves class version integrity across JVM executions.

### D. Run URL & Protocol Handler Demo (`UrlDemo`)
```powershell
mvn exec:java -pl network -Dexec.mainClass="com.shopsphere.network.UrlDemo"
```
Demonstrates:
1. `URLEncoder` & `URLDecoder` parameter sanitization.
2. `HttpURLConnection` inspecting headers and response body from a web server.
3. Custom `URLStreamHandler` registering and resolving proprietary `shop://inventory/product/101` protocols.

---

## 4. Key Viva Questions & Answers

1. **Why does each client get handled in an `ExecutorService` thread pool?**
   - Standard `ServerSocket.accept()` blocks until a client connects. Without concurrency, one client would block all other clients from connecting. Using a thread pool allows concurrent serving while capping resource allocation to prevent server exhaustion.
2. **What does the `transient` keyword do during serialization?**
   - It instructs the JVM serialization mechanism to skip that field. Upon deserialization, reference types become `null` and primitives become `0`/`false`.
3. **What happens if sender and receiver have differing `serialVersionUID` values?**
   - The JVM throws `java.io.InvalidClassException`, refusing to deserialize incompatible classes.
