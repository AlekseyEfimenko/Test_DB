package com.tables;

import com.utils.Config;
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
            dbActions.insertQuery(String.format(Config.getInstance().getSQLQuery("sql_query/insert_2values.sql"), TABLE_NAME, id, NAME));
        }
    }

    public String getName() {
        return NAME;
    }
}

