package com.tables;

import com.utils.DataBaseActions;
import java.sql.Timestamp;

public class SessionTable {
    private static final String TABLE_NAME = "session";
    private static final String COLUMN_NAME_ID = "id";
    private static long buildNumber = 0;
    private long id;
    private Timestamp createdTime;
    private String sessionKey;

    public void setSessionKey(String key) {
        sessionKey = key;
    }

    public void setCreatedTime(Timestamp time) {
        createdTime = time;
    }

    public void addRowToSessionTable() {
        if (DataBaseActions.isEmpty(TABLE_NAME)) {
            id = 1;
        } else {
            id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        }
        buildNumber++;
        DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, '%s', '%s', %s)", TABLE_NAME, id, sessionKey, createdTime, buildNumber));
    }
}

