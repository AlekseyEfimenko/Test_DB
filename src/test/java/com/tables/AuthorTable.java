package com.tables;

import com.utils.Config;
import com.utils.DataBaseActions;

public class AuthorTable {
    private static final String TABLE_NAME = "author";
    private static final String COLUMN_NAME_ID = "id";
    private static final Config CONFIG = Config.getInstance();
    private static final String NAME = CONFIG.getDataProperties("author");
    private static final String LOGIN = CONFIG.getDataProperties("login");
    private static final String EMAIL = CONFIG.getDataProperties("email");
    private long id;

    public void addRowToAuthorTable() {
        if (DataBaseActions.isEmpty(TABLE_NAME)) {
            id = 1;
            DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, '%s', '%s', '%s')", TABLE_NAME, id, NAME, LOGIN, EMAIL));
        } else  if (!DataBaseActions.isEmpty(COLUMN_NAME_ID, TABLE_NAME, String.format("email = '%s'", EMAIL))) {
        } else {
            id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, '%s', '%s', '%s')", TABLE_NAME, id, NAME, LOGIN, EMAIL));
        }
    }

    public String getEmail() {
        return EMAIL;
    }
}

