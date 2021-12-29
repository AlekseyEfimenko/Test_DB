package com.utils;

import static com.utils.DataBaseManager.getConnection;
import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseActions {
    private static final Logger LOGGER = Logger.getLogger(DataBaseActions.class.getName());
    private static final String ERROR_MSG = "Can't get access to database or incorrect SQL query";
    private static ResultSet rs = null;

    private DataBaseActions() {}

    public static ResultSet selectQuery(String table) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT * FROM %s\"", table));
            rs = statement.executeQuery(String.format("SELECT * FROM %s", table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%s: \"SELECT * FROM %s\"", ERROR_MSG, table));
            ex.printStackTrace();
        }
        return rs;
    }

    public static void selectQuery(String columnName, String table) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %s FROM %s\"", columnName, table));
            rs = statement.executeQuery(String.format("SELECT %s FROM %s", columnName, table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%s: \"SELECT %s FROM %s\"", ERROR_MSG, columnName, table));
            ex.printStackTrace();
        }
    }

    public static ResultSet selectQuery(String columnName, String table, String conditions) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %s FROM %s WHERE %s\"", columnName, table, conditions));
            rs = statement.executeQuery(String.format("SELECT %s FROM %s WHERE %s", columnName, table, conditions));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%s: \"SELECT %s FROM %s WHERE %s\"", ERROR_MSG, columnName, table, conditions));
            ex.printStackTrace();
        }
        return rs;
    }

    public static void insertQuery(String sqlQuery) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Putting data into database with next query: %s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
    }

    public static void updateRecord(String sqlQuery) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Updating data in database with next query: %s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
    }

    public static void deleteRecord(String sqlQuery) {
        try {
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.info(String.format("Deleting data from database with next query: %s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
    }

    public static Object getMax(String columnName, String table) {
        Object max = null;
        selectQuery(String.format("max(%s)", columnName), table);
        try {
            if (rs.first()) {
                max = rs.getObject(String.format("max(%s)", columnName));
            }
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
        return max;
    }

    public static Object getFirst(String columnName, String table, String conditions) {
        Object first = null;
        selectQuery(columnName, table, conditions);
        try {
            if (rs.first()) {
                first = rs.getObject(columnName);
            }
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
        return first;
    }

    public static boolean isEmpty(String table) {
        boolean cond = false;
        try {
            cond = !selectQuery(table).next();
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
        return cond;
    }

    public static boolean isEmpty(String  columnName, String table, String conditions) {
        boolean cond = false;
        try {
            cond = !selectQuery(columnName, table, conditions).next();
        } catch (SQLException ex) {
            LOGGER.error(ERROR_MSG);
            ex.printStackTrace();
        }
        return cond;
    }
}

