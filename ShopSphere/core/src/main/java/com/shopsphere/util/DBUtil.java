package com.shopsphere.util;

import com.shopsphere.exception.ShopSphereException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC Connection Utility providing database connections using standard DriverManager.
 * Reads connection settings from classpath resource db.properties.
 */
public class DBUtil {

    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        loadProperties();
    }

    private DBUtil() {
        // Utility class
    }

    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
                driver = props.getProperty("db.driver", DEFAULT_DRIVER).trim();
                url = props.getProperty("db.url", DEFAULT_URL).trim();
                user = props.getProperty("db.user", DEFAULT_USER).trim();
                password = props.getProperty("db.password", DEFAULT_PASSWORD).trim();
            } else {
                driver = DEFAULT_DRIVER;
                url = DEFAULT_URL;
                user = DEFAULT_USER;
                password = DEFAULT_PASSWORD;
            }
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            throw new ShopSphereException("Failed to initialize JDBC Driver / DB Properties", e);
        }
    }

    /**
     * Obtains a new database connection.
     *
     * @return active java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
