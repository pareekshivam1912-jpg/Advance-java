# ShopSphere — Multithreaded TCP Socket Flow Diagram

This diagram visualizes the low-level network programming flow in `shopsphere-network`, showing `ServerSocket` connection acceptance, thread-pool worker dispatch, stream encapsulation, and protocol command processing over TCP port 5000.

```mermaid
flowchart TD
    subgraph ServerLifecycle["InventoryServer Lifecycle (Port 5000)"]
        StartServer["InventoryServer.main()"] --> InitThreadPool["Create FixedThreadPool(10)"]
        InitThreadPool --> CreateSS["new ServerSocket(5000)"]
        CreateSS --> AcceptLoop{"serverSocket.accept()<br/>(Blocking Listen)"}
        AcceptLoop -->|Client Connects| DispatchWorker["Submit ClientHandler to ExecutorService"]
        DispatchWorker --> AcceptLoop
    end

    subgraph ClientHandler["Worker Thread (ClientHandler)"]
        DispatchWorker -.-> SetupStreams["Wrap socket.getInputStream() in BufferedReader<br/>Wrap socket.getOutputStream() in PrintWriter"]
        SetupStreams --> ReadCommand["String command = in.readLine()"]
        ReadCommand --> ParseCommand{"Command Type?"}

        ParseCommand -->|CHECK_STOCK:id| HandleCheck["LocalInventoryService.getStock(id)"]
        ParseCommand -->|UPDATE_STOCK:id:qty| HandleUpdate["LocalInventoryService.updateStock(id, qty)"]
        ParseCommand -->|PING| HandlePing["Prepare PONG Response"]
        ParseCommand -->|Unknown| HandleErr["Prepare ERROR:UNKNOWN_COMMAND"]

        HandleCheck --> FormatResp["out.println('SUCCESS:STOCK:' + count)"]
        HandleUpdate --> FormatResp2["out.println('SUCCESS:UPDATED:' + newStock)"]
        HandlePing --> FormatResp3["out.println('PONG')"]
        HandleErr --> FormatResp4["out.println('ERROR:...')"]

        FormatResp --> FlushOut["out.flush()"]
        FormatResp2 --> FlushOut
        FormatResp3 --> FlushOut
        FormatResp4 --> FlushOut
        FlushOut --> CloseSock["socket.close()"]
    end

    subgraph ClientLifecycle["InventoryClient Execution"]
        StartClient["InventoryClient.main()"] --> Connect["new Socket('localhost', 5000)"]
        Connect --> SendCmd["out.println('CHECK_STOCK:1')"]
        SendCmd --> ReadResp["String response = in.readLine()"]
        ReadResp --> PrintResult["System.out.println(response)"]
        PrintResult --> CloseClient["clientSocket.close()"]
    end

    Connect -.->|TCP 3-Way Handshake| AcceptLoop
    SendCmd -.->|TCP Payload| ReadCommand
    FlushOut -.->|TCP Stream Reply| ReadResp
```
