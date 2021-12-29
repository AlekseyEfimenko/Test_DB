package com.tables;

import com.utils.DataBaseActions;

public class ProjectTable {
    private static final String TABLE_NAME = "project";
    private static final String COLUMN_NAME_ID = "id";
    private static final String NAME = "Test";
    private static long id;

    public void addRowToProjectTable() {
        if (DataBaseActions.isEmpty("*", TABLE_NAME, String.format("name = '%s'", NAME))) {
            if (DataBaseActions.isEmpty(TABLE_NAME)) {
                id = 1;
            } else {
                id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            }
            String putQuery = String.format("INSERT INTO %s VALUES (%s, '%s')", TABLE_NAME, id, NAME);
            DataBaseActions.insertQuery(putQuery);
        }
    }

    public String getName() {
        return NAME;
    }
}

