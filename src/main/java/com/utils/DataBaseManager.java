package com.utils;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseManager {
    private static final Logger LOGGER = Logger.getLogger(DataBaseManager.class.getName());
    private static final Config CONFIG = Config.getInstance();
    private static Connection connection;

    private DataBaseManager() {}

    public static void setConnection() {
        if (connection == null) {
            String connectionString = CONFIG.getProperties("connectionPath");
            String login = CONFIG.getProperties("user");
            String password = CONFIG.getProperties("password");
            try {
                LOGGER.info(String.format("Set connection to %s", connectionString));
                connection = DriverManager.getConnection(connectionString, login, password);

            } catch (SQLException ex) {
                LOGGER.error("Can't get connection. Incorrect URL");
                ex.printStackTrace();
            }
        }
        LOGGER.info("Connection is created successfully");
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        LOGGER.info("Close connection");
        try {
            connection.close();
            LOGGER.info("Connection is closed successfully");
        } catch (SQLException e) {
            LOGGER.error("Can't close connection");
            e.printStackTrace();
        }
    }
}

