package com.shopsphere.network;

import com.shopsphere.model.Product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * RTU Syllabus Core Requirement: Java Networking Module (Socket Client)
 *
 * Console & Automated Scripted client connecting to InventoryServer.
 * Sends protocol commands, deserializes Product domain objects via ObjectInputStream,
 * and prints results to stdout.
 */
public class InventoryClient {

    private final String host;
    private final int port;

    public InventoryClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Executes scripted command suite for automated verification and viva demonstrations.
     */
    public void runScriptedDemo() {
        System.out.println("==================================================================");
        System.out.println("  ShopSphere TCP Inventory Client — Scripted Viva Demonstration  ");
        System.out.println("  Connecting to server at " + host + ":" + port + "...");
        System.out.println("==================================================================");

        try (
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.flush();
            System.out.println("[Connected] Successfully established socket with " + socket.getRemoteSocketAddress());

            // 1. PING Command
            System.out.println("\n--- Step 1: Testing Connection (PING) ---");
            out.writeUTF("PING");
            out.flush();
            String pong = in.readUTF();
            System.out.println("Server Response: " + pong);

            // 2. GET_PRODUCT 1 Command
            System.out.println("\n--- Step 2: Fetching Serialized Product (GET_PRODUCT 1) ---");
            out.writeUTF("GET_PRODUCT 1");
            out.flush();
            String prodStatus = in.readUTF();
            if ("OK".equals(prodStatus)) {
                Product product = (Product) in.readObject();
                System.out.println("Deserialized Product Received from Socket:");
                System.out.println("  ID:          " + product.getId());
                System.out.println("  Name:        " + product.getName());
                System.out.println("  Brand:       " + product.getBrand());
                System.out.println("  Price:       ₹" + product.getPrice());
                System.out.println("  Discount:    " + product.getDiscountPercentage() + "%");
                System.out.println("  Final Price: ₹" + product.getDiscountedPrice());
                System.out.println("  Live Stock:  " + product.getStock() + " units");
            } else {
                System.out.println("Product lookup returned: " + prodStatus);
            }

            // 3. GET_STOCK 1 Command
            System.out.println("\n--- Step 3: Fast Inventory Stock Probe (GET_STOCK 1) ---");
            out.writeUTF("GET_STOCK 1");
            out.flush();
            String stockStatus = in.readUTF();
            if ("OK".equals(stockStatus)) {
                int stock = in.readInt();
                System.out.println("Server Current Stock Level: " + stock + " units");
            }

            // 4. SEARCH_PRODUCT Command
            System.out.println("\n--- Step 4: Catalog Search Query (SEARCH_PRODUCT Phone) ---");
            out.writeUTF("SEARCH_PRODUCT Phone");
            out.flush();
            String searchStatus = in.readUTF();
            if ("OK".equals(searchStatus)) {
                @SuppressWarnings("unchecked")
                List<Product> searchResults = (List<Product>) in.readObject();
                System.out.println("Search Results Count: " + searchResults.size() + " matches found:");
                for (Product p : searchResults) {
                    System.out.println("  - #" + p.getId() + " [" + p.getBrand() + "] " + p.getName() + " (₹" + p.getDiscountedPrice() + ", Stock: " + p.getStock() + ")");
                }
            }

            // 5. EXIT Command
            System.out.println("\n--- Step 5: Terminating Session (EXIT) ---");
            out.writeUTF("EXIT");
            out.flush();
            String bye = in.readUTF();
            System.out.println("Server Farewell: " + bye);

            System.out.println("\n==================================================================");
            System.out.println("  [VERIFIED] Scripted TCP Client-Server Demo Completed Cleanly!   ");
            System.out.println("==================================================================");

        } catch (Exception e) {
            System.err.println("Client Socket Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Interactive console loop for manual testing.
     */
    public void runInteractive() {
        try (
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            out.flush();
            System.out.println("Connected to ShopSphere Inventory Server at " + host + ":" + port);
            System.out.println("Available commands: PING, GET_PRODUCT <id>, GET_STOCK <id>, SEARCH_PRODUCT <keyword>, EXIT");

            while (true) {
                System.out.print("\nShopSphere-Socket> ");
                String input = scanner.nextLine();
                if (input == null || input.trim().isEmpty()) continue;

                out.writeUTF(input.trim());
                out.flush();

                String status = in.readUTF();
                if ("PONG".equals(status) || "BYE".equals(status)) {
                    System.out.println("Server: " + status);
                    if ("BYE".equals(status)) break;
                } else if ("OK".equals(status)) {
                    if (input.toUpperCase().startsWith("GET_PRODUCT")) {
                        Product p = (Product) in.readObject();
                        System.out.println("Received: " + p);
                    } else if (input.toUpperCase().startsWith("GET_STOCK")) {
                        int stock = in.readInt();
                        System.out.println("Stock Count: " + stock);
                    } else if (input.toUpperCase().startsWith("SEARCH_PRODUCT")) {
                        @SuppressWarnings("unchecked")
                        List<Product> list = (List<Product>) in.readObject();
                        System.out.println("Matches (" + list.size() + "):");
                        for (Product p : list) System.out.println("  " + p);
                    }
                } else {
                    System.out.println("Server Response: " + status);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = InventoryServer.DEFAULT_PORT;

        if (args.length > 0 && "--interactive".equalsIgnoreCase(args[0])) {
            new InventoryClient(host, port).runInteractive();
        } else {
            new InventoryClient(host, port).runScriptedDemo();
        }
    }
}
