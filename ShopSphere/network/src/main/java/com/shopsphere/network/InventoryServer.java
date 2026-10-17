package com.shopsphere.network;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * RTU Syllabus Core Requirement: Java Networking Module (Client-Server Sockets)
 *
 * Multithreaded TCP Server listening on configurable port (default 5000).
 * Handles each incoming client socket in its own thread using an ExecutorService pool.
 * Transmits serialized Product domain objects using ObjectOutputStream/ObjectInputStream.
 */
public class InventoryServer {

    private static final Logger LOGGER = Logger.getLogger(InventoryServer.class.getName());
    public static final int DEFAULT_PORT = 5000;

    private final int port;
    private final ProductService productService;
    private final ExecutorService threadPool;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public InventoryServer(int port) {
        this.port = port;
        this.productService = ServiceFactory.getProductService();
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.info("==================================================================");
        LOGGER.info("  ShopSphere TCP Inventory Server Started on Port " + port);
        LOGGER.info("  Protocol: java.net.ServerSocket + Object Serialization");
        LOGGER.info("  Ready for concurrent client connections...");
        LOGGER.info("==================================================================");

        try {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("[Server] Incoming connection accepted from: " + clientSocket.getRemoteSocketAddress());
                threadPool.submit(new ClientHandler(clientSocket, productService));
            }
        } catch (SocketException e) {
            if (!running) {
                LOGGER.info("[Server] ServerSocket closed gracefully.");
            } else {
                throw e;
            }
        }
    }

    public void stop() {
        this.running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
        threadPool.shutdown();
        LOGGER.info("[Server] InventoryServer stopped.");
    }

    /**
     * Dedicated Runnable handling client communication on an isolated thread.
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final ProductService productService;

        public ClientHandler(Socket socket, ProductService productService) {
            this.socket = socket;
            this.productService = productService;
        }

        @Override
        public void run() {
            try (
                // Note: Always create ObjectOutputStream before ObjectInputStream to prevent stream header deadlock!
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                out.flush();

                while (true) {
                    String command;
                    try {
                        command = in.readUTF();
                    } catch (EOFException e) {
                        break; // Client disconnected cleanly
                    }

                    if (command == null || command.trim().isEmpty()) {
                        continue;
                    }

                    command = command.trim();
                    LOGGER.info(String.format("[ClientHandler %s] Received command: %s",
                            socket.getRemoteSocketAddress(), command));

                    if ("PING".equalsIgnoreCase(command)) {
                        out.writeUTF("PONG");
                        out.flush();
                    } else if (command.toUpperCase().startsWith("GET_PRODUCT ")) {
                        String idStr = command.substring("GET_PRODUCT ".length()).trim();
                        try {
                            int productId = Integer.parseInt(idStr);
                            Product product = productService.getProductById(productId);
                            if (product != null) {
                                out.writeUTF("OK");
                                out.writeObject(product);
                            } else {
                                out.writeUTF("NOT_FOUND");
                            }
                        } catch (NumberFormatException e) {
                            out.writeUTF("ERROR: Invalid product ID format");
                        }
                        out.flush();
                    } else if (command.toUpperCase().startsWith("GET_STOCK ")) {
                        String idStr = command.substring("GET_STOCK ".length()).trim();
                        try {
                            int productId = Integer.parseInt(idStr);
                            Product product = productService.getProductById(productId);
                            if (product != null) {
                                out.writeUTF("OK");
                                out.writeInt(product.getStock());
                            } else {
                                out.writeUTF("NOT_FOUND");
                            }
                        } catch (NumberFormatException e) {
                            out.writeUTF("ERROR: Invalid product ID format");
                        }
                        out.flush();
                    } else if (command.toUpperCase().startsWith("SEARCH_PRODUCT ")) {
                        String keyword = command.substring("SEARCH_PRODUCT ".length()).trim();
                        List<Product> results = productService.searchProducts(keyword, null, null, null, null, 1, 20);
                        out.writeUTF("OK");
                        out.writeObject(new ArrayList<>(results));
                        out.flush();
                    } else if ("EXIT".equalsIgnoreCase(command)) {
                        out.writeUTF("BYE");
                        out.flush();
                        break;
                    } else {
                        out.writeUTF("ERROR: Unknown command '" + command + "'. Valid: PING, GET_PRODUCT <id>, GET_STOCK <id>, SEARCH_PRODUCT <kw>, EXIT");
                        out.flush();
                    }
                }
            } catch (Exception e) {
                LOGGER.warning(String.format("[ClientHandler %s] Exception: %s", socket.getRemoteSocketAddress(), e.getMessage()));
            } finally {
                try {
                    socket.close();
                    LOGGER.info("[ClientHandler] Connection closed: " + socket.getRemoteSocketAddress());
                } catch (IOException ignored) {
                }
            }
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

        InventoryServer server = new InventoryServer(port);
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Fatal: Could not start InventoryServer on port " + port + ": " + e.getMessage());
        }
    }
}
