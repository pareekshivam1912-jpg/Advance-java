package com.shopsphere.network;

import com.shopsphere.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test verifying TCP InventoryServer and Client socket communication.
 */
public class InventoryServerTest {

    private static final int TEST_PORT = 5055;
    private static InventoryServer server;
    private static Thread serverThread;

    @BeforeAll
    static void startServer() throws Exception {
        server = new InventoryServer(TEST_PORT);
        serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception ignored) {
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        // Brief pause for server to bind port
        Thread.sleep(500);
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testSocketCommunication() throws Exception {
        try (
            Socket socket = new Socket("localhost", TEST_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.flush();

            // 1. PING
            out.writeUTF("PING");
            out.flush();
            String pong = in.readUTF();
            assertEquals("PONG", pong);

            // 2. GET_PRODUCT 1
            out.writeUTF("GET_PRODUCT 1");
            out.flush();
            String prodStatus = in.readUTF();
            if ("OK".equals(prodStatus)) {
                Product product = (Product) in.readObject();
                assertNotNull(product);
                assertEquals(1, product.getId());
            }

            // 3. GET_STOCK 1
            out.writeUTF("GET_STOCK 1");
            out.flush();
            String stockStatus = in.readUTF();
            if ("OK".equals(stockStatus)) {
                int stock = in.readInt();
                assertTrue(stock >= 0);
            }

            // 4. SEARCH_PRODUCT
            out.writeUTF("SEARCH_PRODUCT Phone");
            out.flush();
            String searchStatus = in.readUTF();
            if ("OK".equals(searchStatus)) {
                @SuppressWarnings("unchecked")
                List<Product> results = (List<Product>) in.readObject();
                assertNotNull(results);
            }

            // 5. EXIT
            out.writeUTF("EXIT");
            out.flush();
            String bye = in.readUTF();
            assertEquals("BYE", bye);
        }
    }
}
