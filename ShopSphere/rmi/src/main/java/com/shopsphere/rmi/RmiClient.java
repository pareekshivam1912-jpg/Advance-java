package com.shopsphere.rmi;

import com.shopsphere.model.Product;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

/**
 * RTU Advanced Java Syllabus Core Requirement: Java Remote Method Invocation (RMI)
 *
 * RMI Client:
 * 1. Demonstrates both 'Naming.lookup()' and 'Registry.lookup()' mechanisms.
 * 2. Invokes remote procedures across JVM boundaries with automatic argument/return marshaling.
 * 3. Demonstrates RemoteException, NotBoundException, and ConnectException handling.
 */
public class RmiClient {

    private final String host;
    private final int port;

    public RmiClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Executes automated test suite invoking all remote methods for viva demonstrations.
     */
    public void runScriptedDemo() {
        System.out.println("==========================================================================");
        System.out.println("   SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: RMI CLIENT DEMONSTRATION      ");
        System.out.println("   Target: " + host + ":" + port + " (" + RmiServer.SERVICE_NAME + ")");
        System.out.println("==========================================================================");

        try {
            // Demonstration 1: Lookup using java.rmi.Naming URL syntax
            String rmiUrl = "rmi://" + host + ":" + port + "/" + RmiServer.SERVICE_NAME;
            System.out.println("\n[Lookup Method 1] Using Naming.lookup(\"" + rmiUrl + "\")...");
            InventoryService serviceViaNaming = (InventoryService) Naming.lookup(rmiUrl);
            System.out.println("  >>> Successfully retrieved remote stub: " + serviceViaNaming.getClass().getName());

            // Demonstration 2: Lookup using java.rmi.registry.Registry
            System.out.println("\n[Lookup Method 2] Using LocateRegistry.getRegistry().lookup(\"" + RmiServer.SERVICE_NAME + "\")...");
            Registry registry = LocateRegistry.getRegistry(host, port);
            InventoryService service = (InventoryService) registry.lookup(RmiServer.SERVICE_NAME);
            System.out.println("  >>> Successfully retrieved remote stub via Registry reference.");

            // 1. Remote call: getProduct(1)
            System.out.println("\n--- [Step 1] Remote Procedure Call: getProduct(1) ---");
            Product p1 = service.getProduct(1);
            if (p1 != null) {
                System.out.println("Remote Product Result (Deserialized in Client JVM):");
                System.out.println("  - ID:           " + p1.getId());
                System.out.println("  - Name:         " + p1.getName());
                System.out.println("  - Brand:        " + p1.getBrand());
                System.out.println("  - Price:        ₹" + p1.getPrice());
                System.out.println("  - Rating:       " + p1.getRating() + " / 5.0");
                System.out.println("  - Stock:        " + p1.getStock() + " units");
            } else {
                System.out.println("Product #1 not found in database.");
            }

            // 2. Remote call: getStock(1)
            System.out.println("\n--- [Step 2] Remote Procedure Call: getStock(1) ---");
            int stock = service.getStock(1);
            System.out.println("Remote Stock Result: " + stock + " units available");

            // 3. Remote call: updateStock(1, newStock) and restore
            System.out.println("\n--- [Step 3] Remote Procedure Call: updateStock(1, stock + 2) ---");
            boolean updated = service.updateStock(1, stock + 2);
            System.out.println("Stock updated (+2): " + updated);
            int newStock = service.getStock(1);
            System.out.println("Verified new stock: " + newStock + " units");

            // Revert back
            service.updateStock(1, stock);
            System.out.println("Stock reverted back to: " + service.getStock(1) + " units");

            // 4. Remote call: getLowStockProducts(15)
            System.out.println("\n--- [Step 4] Remote Procedure Call: getLowStockProducts(threshold=15) ---");
            List<Product> lowStockList = service.getLowStockProducts(15);
            System.out.println("Found " + lowStockList.size() + " products with stock <= 15:");
            for (Product lp : lowStockList) {
                System.out.println("  - #" + lp.getId() + " [" + lp.getBrand() + "] " + lp.getName() + " -> Stock: " + lp.getStock());
            }

            // 5. Remote call error handling demonstration (Invalid ID)
            System.out.println("\n--- [Step 5] Remote Call: getProduct(99999) Non-existent ID ---");
            Product missing = service.getProduct(99999);
            System.out.println("Result for non-existent ID (should be null): " + missing);

            System.out.println("\n==========================================================================");
            System.out.println("  [VERIFIED] RMI Client Distributed Operations Completed Successfully!    ");
            System.out.println("==========================================================================");

        } catch (ConnectException e) {
            System.err.println("\n[RMI Error] Connection refused! Ensure RmiServer is running on " + host + ":" + port);
            System.err.println("Details: " + e.getMessage());
        } catch (NotBoundException e) {
            System.err.println("\n[RMI Error] Service '" + RmiServer.SERVICE_NAME + "' is not bound in Registry on " + host + ":" + port);
            System.err.println("Details: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("\n[RMI Error] Distributed RemoteException occurred during communication:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n[Error] Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Interactive console loop for manual viva examiner testing.
     */
    public void runInteractive() {
        try {
            InventoryService service = (InventoryService) Naming.lookup("rmi://" + host + ":" + port + "/" + RmiServer.SERVICE_NAME);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to ShopSphere RMI Inventory Service.");

            while (true) {
                System.out.println("\n--- ShopSphere RMI Client Menu ---");
                System.out.println("1. Get Product by ID");
                System.out.println("2. Check Stock by Product ID");
                System.out.println("3. Update Product Stock");
                System.out.println("4. List Low-Stock Products");
                System.out.println("0. Exit");
                System.out.print("Select choice: ");

                String line = scanner.nextLine();
                if ("0".equals(line)) break;

                switch (line) {
                    case "1":
                        System.out.print("Enter Product ID: ");
                        int pid = Integer.parseInt(scanner.nextLine().trim());
                        Product p = service.getProduct(pid);
                        System.out.println(p != null ? "Product: " + p : "Product not found.");
                        break;
                    case "2":
                        System.out.print("Enter Product ID: ");
                        int spid = Integer.parseInt(scanner.nextLine().trim());
                        System.out.println("Current Stock: " + service.getStock(spid) + " units");
                        break;
                    case "3":
                        System.out.print("Enter Product ID: ");
                        int upid = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter New Stock Quantity: ");
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        boolean ok = service.updateStock(upid, qty);
                        System.out.println("Update Status: " + (ok ? "SUCCESS" : "FAILED"));
                        break;
                    case "4":
                        System.out.print("Enter Stock Threshold: ");
                        int th = Integer.parseInt(scanner.nextLine().trim());
                        List<Product> low = service.getLowStockProducts(th);
                        System.out.println("Found " + low.size() + " items:");
                        for (Product lp : low) System.out.println("  " + lp);
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }
            }
        } catch (Exception e) {
            System.err.println("RMI Interactive Client Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = RmiServer.DEFAULT_PORT;

        if (args.length > 0 && "--interactive".equalsIgnoreCase(args[0])) {
            new RmiClient(host, port).runInteractive();
        } else {
            new RmiClient(host, port).runScriptedDemo();
        }
    }
}
