package com.shopsphere.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.logging.Logger;

/**
 * RTU Advanced Java Syllabus Core Requirement: Java Remote Method Invocation (RMI)
 *
 * RMI Server Host:
 * 1. Initializes an in-process rmiregistry on port 1099 using LocateRegistry.createRegistry().
 * 2. Instantiates the remote service object (InventoryServiceImpl).
 * 3. Registers the stub into the RMI naming directory using Naming.rebind() and Registry.rebind().
 */
public class RmiServer {

    private static final Logger LOGGER = Logger.getLogger(RmiServer.class.getName());
    public static final int DEFAULT_PORT = 1099;
    public static final String SERVICE_NAME = "InventoryService";

    private final int port;
    private Registry registry;
    private InventoryServiceImpl serviceImpl;

    public RmiServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        System.out.println("==========================================================================");
        System.out.println("        SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: RMI SERVER               ");
        System.out.println("==========================================================================");

        // 1. Locate or create RMI registry on target port
        try {
            System.out.println("[RMI Server] Creating in-process RMI Registry on port " + port + "...");
            registry = LocateRegistry.createRegistry(port);
            System.out.println("  >>> RMI Registry successfully started on port " + port);
        } catch (ExportException e) {
            System.out.println("  >>> RMI Registry already active on port " + port + ". Connecting to existing registry...");
            registry = LocateRegistry.getRegistry(port);
        }

        // 2. Instantiate Remote Object
        System.out.println("[RMI Server] Instantiating InventoryServiceImpl (UnicastRemoteObject)...");
        serviceImpl = new InventoryServiceImpl();

        // 3. Bind stub to Registry via Naming and Registry
        String rmiUrl = "rmi://localhost:" + port + "/" + SERVICE_NAME;
        System.out.println("[RMI Server] Registering service stub with naming directory: " + rmiUrl);
        Naming.rebind(rmiUrl, serviceImpl);

        System.out.println("\n==========================================================================");
        System.out.println("  RMI Server Status: ACTIVE & LISTENING                                   ");
        System.out.println("  Service Name:      " + SERVICE_NAME);
        System.out.println("  Lookup URL:        " + rmiUrl);
        System.out.println("  Underlying DB:     MySQL localhost:3306/shopsphere");
        System.out.println("  Remote Methods:    getProduct(), getStock(), updateStock(), getLowStock()");
        System.out.println("==========================================================================");
        System.out.println("Press Ctrl+C to terminate RMI server.\n");
    }

    public void stop() {
        try {
            String rmiUrl = "rmi://localhost:" + port + "/" + SERVICE_NAME;
            Naming.unbind(rmiUrl);
            System.out.println("[RMI Server] Service unbound successfully.");
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        try {
            RmiServer server = new RmiServer(port);
            server.start();

            // Keep main thread alive
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Fatal RMI Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
