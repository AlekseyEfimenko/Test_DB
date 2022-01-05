package com.tables;

import com.utils.DataBaseManager;

public class ProjectTable {
    private static final String TABLE_NAME = "project";
    private static final String COLUMN_NAME_ID = "id";
    private static final String NAME = "Test";
    private static long id;
    private final DataBaseManager dbActions = DataBaseManager.getInstance();

    public void addRowToProjectTable() {
        if (dbActions.isEmpty("*", TABLE_NAME, String.format("name = '%1$s'", NAME))) {
            id = dbActions.isEmpty(TABLE_NAME) ? 1 : (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            String putQuery = String.format("INSERT INTO %1$s VALUES (%2$s, '%3$s')", TABLE_NAME, id, NAME);
            dbActions.insertQuery(putQuery);
        }
    }

    public String getName() {
        return NAME;
    }
}

