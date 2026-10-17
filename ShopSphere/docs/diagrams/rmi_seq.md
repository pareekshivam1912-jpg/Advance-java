# ShopSphere — Java RMI Distributed Architecture Sequence Diagram

This sequence diagram depicts the distributed object interaction in **ShopSphere** using Java Remote Method Invocation (RMI), showing registry binding, stub generation, parameter serialization, and remote execution across separate JVMs.

```mermaid
sequenceDiagram
    autonumber
    actor AdminUser as Admin Operator
    participant ClientApp as RmiClient (Client JVM)
    participant Registry as RMI Registry (Port 1099)
    participant Stub as Remote Stub (Proxy)
    participant Skeleton as Remote Skeleton / Dispatcher
    participant ServerImpl as InventoryServiceImpl (Server JVM)
    participant LocalSvc as LocalInventoryService
    participant DB as MySQL Database

    Note over Registry,ServerImpl: Phase 1: Server Initialization & Registry Binding
    ServerImpl->>Registry: LocateRegistry.createRegistry(1099)
    ServerImpl->>Registry: Naming.rebind("rmi://localhost:1099/InventoryService", serverStub)
    Registry-->>ServerImpl: Bound successfully

    Note over ClientApp,DB: Phase 2: Remote Method Lookup & Invocation
    AdminUser->>ClientApp: Run check stock / restock
    ClientApp->>Registry: Naming.lookup("rmi://localhost:1099/InventoryService")
    Registry-->>ClientApp: Return Remote Stub implementing InventoryService

    ClientApp->>Stub: getStock(productId = 1)
    Stub->>Stub: Marshal parameters (serialize int)
    Stub->>Skeleton: Send binary RMI invocation over TCP socket
    Skeleton->>Skeleton: Unmarshal parameters
    Skeleton->>ServerImpl: invoke getStock(1)

    ServerImpl->>LocalSvc: getStock(1)
    LocalSvc->>DB: SELECT stock_quantity FROM products WHERE id=1
    DB-->>LocalSvc: 25
    LocalSvc-->>ServerImpl: 25

    ServerImpl-->>Skeleton: Return int value (25)
    Skeleton->>Skeleton: Marshal return value
    Skeleton-->>Stub: Send binary RMI result over TCP socket
    Stub->>Stub: Unmarshal return value
    Stub-->>ClientApp: Return integer 25
    ClientApp-->>AdminUser: Display: "Current stock for Product 1: 25 units"
```
