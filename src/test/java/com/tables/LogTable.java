package com.tables;

import com.utils.Config;
import com.utils.DataBaseManager;

public class LogTable {
    private static final String TABLE_NAME = "log";
    private static final String COLUMN_NAME_ID = "id";
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private long id;
    private String content;
    private boolean isException = false;
    private long testId;
    private String errorInfo;

    public void setTestId(long id) {
        testId = id;
    }

    public void setContent(String content, String method) {
        this.content = String.format("%1$s%n%2$s Test-case: %3$s %4$s%n%5$s%n%6$s",
                "-".repeat(80), "=".repeat(20), method, "=".repeat(20), "-".repeat(80), content.replace("'", ""));
    }

    public void setErrorInfo(String error) {
        errorInfo = error;
    }

    public void addRowToLogTable() {
        if (dbActions.isEmpty(TABLE_NAME)) {
            id = 1;
        } else {
            insertRow();
        }
        if (!errorInfo.equals("")) {
            isException = true;
            insertRow();
        }
    }

    private void insertRow() {
        id = (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        dbActions.insertQuery(String.format(Config.getInstance().getSQLQuery("sql_query/insert_4values.sql"), TABLE_NAME, id, content, isException, testId));
    }
}

