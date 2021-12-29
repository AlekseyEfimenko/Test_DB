package com.tables;

import com.utils.DataBaseActions;

public class LogTable {
    private static final String TABLE_NAME = "log";
    private static final String COLUMN_NAME_ID = "id";
    private long id;
    private String content;
    private boolean isException = false;
    private long testId;
    private String errorInfo;

    public void setTestId(long id) {
        testId = id;
    }

    public void setContent(String content, String method) {
        this.content = String.format("%s%n%s Test-case: %s %s%n%s%n%s",
                "-".repeat(80), "=".repeat(20), method, "=".repeat(20), "-".repeat(80), content.replace("'", ""));
    }

    public void setErrorInfo(String error) {
        errorInfo = error;
    }

    public void addRowToLogTable() {
        if (DataBaseActions.isEmpty(TABLE_NAME)) {
            id = 1;
        } else {
            id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, '%s', %s, %s)", TABLE_NAME, id, content, isException, testId));
        }
        if (!errorInfo.equals("")) {
            id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
            isException = true;
            DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, '%s', %s, %s)", TABLE_NAME, id, errorInfo, isException, testId));
        }
    }
}

