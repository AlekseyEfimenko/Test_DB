package com.utils;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
    private static final Logger LOGGER = Logger.getLogger(DataBaseManager.class.getName());
    private static final Config CONFIG = Config.getInstance();
    private static final String ERROR_MSG = "Can't get access to database or incorrect SQL query";
    private static final String ERROR_FORMAT = "%1$s%n%2$s";
    private static DataBaseManager instance;
    private static Connection connection;
    private Statement statement;
    private ResultSet rs = null;

    private DataBaseManager() {
        setConnection();
        try {
            statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public static void closeConnection() {
        LOGGER.info("Close connection");
        try {
            connection.close();
            LOGGER.info("Connection is closed successfully");
        } catch (SQLException ex) {
            LOGGER.error(String.format("Can't close connection%n%1$s",  ex.getMessage()));
        }
    }

    public ResultSet selectQuery(String table) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT * FROM %1$s\"", table));
            rs = statement.executeQuery(String.format(CONFIG.getSQLQuery("sql_query/select_all.sql"), table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT * FROM %2$s\"%n%3$s", ERROR_MSG, table, ex.getMessage()));
        }
        return rs;
    }

    public void selectQuery(String columnName, String table) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %1$s FROM %2$s\"", columnName, table));
            rs = statement.executeQuery(String.format(CONFIG.getSQLQuery("sql_query/select_without_conditions.sql"), columnName, table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT %2$s FROM %3$s\"%n%4$s", ERROR_MSG, columnName, table, ex.getMessage()));
        }
    }

    public ResultSet selectQuery(String columnName, String table, String conditions) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %1$s FROM %2$s WHERE %3$s\"", columnName, table, conditions));
            rs = statement.executeQuery(String.format(CONFIG.getSQLQuery("sql_query/select_with_conditions.sql"), columnName, table, conditions));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT %2$s FROM %3$s WHERE %4$s\"%n%5$s", ERROR_MSG, columnName, table, conditions, ex.getMessage()));
        }
        return rs;
    }

    public void insertQuery(String sqlQuery) {
        try {
            LOGGER.info(String.format("Putting data into database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    public void updateRecord(String sqlQuery) {
        try {
            LOGGER.info(String.format("Updating data in database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    public void deleteRecord(String sqlQuery) {
        try {
            LOGGER.info(String.format("Deleting data from database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    public Object getMax(String columnName, String table) {
        Object max = null;
        selectQuery(String.format("max(%1$s)", columnName), table);
        try {
            if (rs.first()) {
                max = rs.getObject(String.format("max(%1$s)", columnName));
            }
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return max;
    }

    public Object getFirst(String columnName, String table, String conditions) {
        Object first = null;
        selectQuery(columnName, table, conditions);
        try {
            if (rs.first()) {
                first = rs.getObject(columnName);
            }
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return first;
    }

    public boolean isEmpty(String table) {
        boolean cond = false;
        try {
            cond = !selectQuery(table).next();
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return cond;
    }

    public boolean isEmpty(String  columnName, String table, String conditions) {
        boolean cond = false;
        try {
            cond = !selectQuery(columnName, table, conditions).next();
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return cond;
    }

    private static void setConnection() {
        if (connection == null) {
            String connectionString = CONFIG.getProperties("connectionPath");
            String login = CONFIG.getProperties("user");
            String password = CONFIG.getProperties("password");
            try {
                LOGGER.info(String.format("Set connection to %1$s", connectionString));
                connection = DriverManager.getConnection(connectionString, login, password);

            } catch (SQLException ex) {
                LOGGER.error(String.format("Can't get connection. Incorrect URL%n%1$s", ex.getMessage()));
            }
        }
        LOGGER.info("Connection is created successfully");
    }
}

