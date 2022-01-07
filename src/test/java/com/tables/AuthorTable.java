package com.tables;

import com.utils.Config;
import com.utils.DataBaseManager;
import org.apache.log4j.Logger;

public class AuthorTable {
    private static final Logger LOGGER = Logger.getLogger(AuthorTable.class.getName());
    private static final String TABLE_NAME = "author";
    private static final String COLUMN_NAME_ID = "id";
    private static final Config CONFIG = Config.getInstance();
    private static final String NAME = CONFIG.getDataProperties("author");
    private static final String LOGIN = CONFIG.getDataProperties("login");
    private static final String EMAIL = CONFIG.getDataProperties("email");
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private long id;

    public void addRowToAuthorTable() {
        if (dbActions.isEmpty(TABLE_NAME)) {
            id = 1;
            insertRow();
        } else  if (!dbActions.isEmpty(COLUMN_NAME_ID, TABLE_NAME, String.format("email = '%1$s'", EMAIL))) {
            LOGGER.info(String.format("Author with email: \"%1$s\" has already existed", EMAIL));
        } else {
            id = (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            insertRow();
        }
    }

    private void insertRow() {
        dbActions.insertQuery(String.format(CONFIG.getSQLQuery("sql_query/insert_4values.sql"), TABLE_NAME, id, NAME, LOGIN, EMAIL));
    }

    public String getEmail() {
        return EMAIL;
    }
}

