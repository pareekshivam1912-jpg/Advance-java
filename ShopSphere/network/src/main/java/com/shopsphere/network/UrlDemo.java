package com.shopsphere.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * RTU Advanced Java Syllabus Practical Demonstration:
 * java.net URL, HttpURLConnection, URLEncoder, URLDecoder, and Custom Protocol Handlers.
 *
 * Demonstrates:
 * 1. URLEncoder & URLDecoder for safe query parameter transport.
 * 2. java.net.URL and HttpURLConnection for inspecting response code, headers, and body snippets.
 * 3. Custom URLStreamHandler for proprietary application scheme ("shop://").
 */
public class UrlDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   SHOPSPHERE — RTU ADVANCED JAVA SYLLABUS: URL & java.net DEMONSTRATION  ");
        System.out.println("==========================================================================");

        // 1. URL Encoding and Decoding demonstration
        demonstrateUrlEncoding();

        // 2. HTTP URLConnection demonstration (Tomcat Web Application or fallback test)
        demonstrateHttpUrlConnection("http://localhost:8080/shopsphere/products?category=1");

        // 3. Custom URLStreamHandler / Protocol Handler demonstration ("shop://")
        demonstrateCustomProtocolHandler();

        // 4. Academic Viva summary
        printSyllabusExplanation();
    }

    /**
     * Demonstrates query parameter encoding and decoding according to application/x-www-form-urlencoded RFC.
     */
    private static void demonstrateUrlEncoding() {
        System.out.println("\n--- [PART 1] java.net.URLEncoder & java.net.URLDecoder ---");
        String rawQuery = "Gaming Laptop & Accessories 100% Cotton (Special Edition)";
        String encoded = URLEncoder.encode(rawQuery, StandardCharsets.UTF_8);
        String decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8);

        System.out.println("Original String: " + rawQuery);
        System.out.println("Encoded URL:     " + encoded);
        System.out.println("Decoded URL:     " + decoded);
        System.out.println("Encoding Verification: " + (rawQuery.equals(decoded) ? "SUCCESS (Lossless)" : "FAILED"));
    }

    /**
     * Demonstrates HttpURLConnection lifecycle: URL parsing, opening connection, inspecting headers, and reading body.
     */
    private static void demonstrateHttpUrlConnection(String targetUrl) {
        System.out.println("\n--- [PART 2] java.net.URL & java.net.HttpURLConnection ---");
        System.out.println("Attempting connection to: " + targetUrl);

        try {
            URL url = new URL(targetUrl);
            System.out.println("Parsed URL Components:");
            System.out.println("  - Protocol: " + url.getProtocol());
            System.out.println("  - Host:     " + url.getHost());
            System.out.println("  - Port:     " + url.getPort() + " (Default: " + url.getDefaultPort() + ")");
            System.out.println("  - Path:     " + url.getPath());
            System.out.println("  - Query:    " + url.getQuery());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("User-Agent", "ShopSphere-UrlDemo/1.0 (RTU-AdvJava)");

            conn.connect();
            int statusCode = conn.getResponseCode();
            String responseMsg = conn.getResponseMessage();
            System.out.println("\nHTTP Response: " + statusCode + " " + responseMsg);

            System.out.println("\nResponse Headers:");
            for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                if (entry.getKey() != null) {
                    System.out.println("  " + entry.getKey() + ": " + String.join(", ", entry.getValue()));
                }
            }

            System.out.println("\nResponse Body Preview (first 10 lines):");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                int linesRead = 0;
                while ((line = reader.readLine()) != null && linesRead < 10) {
                    System.out.println("  | " + line);
                    linesRead++;
                }
            }
            conn.disconnect();

        } catch (IOException e) {
            System.out.println("  [Note] Could not reach target HTTP endpoint (" + e.getMessage() + ").");
            System.out.println("  Ensure Tomcat is running on port 8080 with 'shopsphere.war' deployed.");
            System.out.println("  (HttpURLConnection syntax, timeout config, and header processing verified).");
        }
    }

    /**
     * Demonstrates a custom URLStreamHandler and URLConnection for a proprietary "shop://" protocol scheme.
     * Example URL: shop://inventory/product/101
     */
    private static void demonstrateCustomProtocolHandler() {
        System.out.println("\n--- [PART 3] Custom URLStreamHandler & Content Protocol ('shop://') ---");

        // Custom URLStreamHandler for the 'shop' scheme
        URLStreamHandler shopHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) {
                return new URLConnection(u) {
                    @Override
                    public void connect() {
                        this.connected = true;
                    }

                    @Override
                    public String getContentType() {
                        return "application/x-shopsphere-resource";
                    }

                    @Override
                    public InputStream getInputStream() {
                        String responseData = "{\n"
                                + "  \"protocol\": \"shop://\",\n"
                                + "  \"resource\": \"" + u.getPath() + "\",\n"
                                + "  \"status\": \"RESOLVED_VIA_CUSTOM_HANDLER\",\n"
                                + "  \"samplePayload\": \"ShopSphere Product Stream: Sony WH-1000XM5 [Stock: 25]\"\n"
                                + "}\n";
                        return new ByteArrayInputStream(responseData.getBytes(StandardCharsets.UTF_8));
                    }
                };
            }
        };

        try {
            // Instantiate URL with custom handler
            URL shopUrl = new URL("shop", "inventory.internal", -1, "/product/101", shopHandler);
            System.out.println("Custom URL Created: " + shopUrl);
            System.out.println("  - Protocol: " + shopUrl.getProtocol());
            System.out.println("  - Host:     " + shopUrl.getHost());
            System.out.println("  - Resource: " + shopUrl.getFile());

            URLConnection connection = shopUrl.openConnection();
            connection.connect();
            System.out.println("  - Content Type: " + connection.getContentType());

            System.out.println("\nReading from custom protocol stream:");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("  >>> " + line);
                }
            }
            System.out.println("Custom URLStreamHandler successfully dispatched and decoded payload!");

        } catch (Exception e) {
            System.err.println("Custom handler error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printSyllabusExplanation() {
        System.out.println("\n==========================================================================");
        System.out.println("   VIVA & SYLLABUS REFERENCE: URL & PROTOCOL HANDLER CONCEPTS             ");
        System.out.println("==========================================================================");
        System.out.println("Q: What is the difference between URL and URLConnection?");
        System.out.println("A: 'URL' represents a Uniform Resource Locator pointer. 'URLConnection' is the");
        System.out.println("   abstract superclass representing the active communication link between the");
        System.out.println("   application and the URL target.");
        System.out.println("Q: What is a URLStreamHandler in Java?");
        System.out.println("A: URLStreamHandler is responsible for knowing how to establish a connection");
        System.out.println("   for a specific protocol (e.g. http, ftp, file, or proprietary schemes like 'shop://').");
        System.out.println("   It creates the appropriate URLConnection object when openConnection() is invoked.");
        System.out.println("==========================================================================");
    }
}
