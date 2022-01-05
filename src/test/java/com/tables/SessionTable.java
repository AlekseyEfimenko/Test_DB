package com.tables;

import com.utils.DataBaseManager;
import java.sql.Timestamp;

public class SessionTable {
    private static final String TABLE_NAME = "session";
    private static final String COLUMN_NAME_ID = "id";
    private static long buildNumber = 0;
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
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
        id = dbActions.isEmpty(TABLE_NAME) ? 1 : (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        buildNumber++;
        dbActions.insertQuery(String.format("INSERT INTO %1$s VALUES (%2$s, '%3$s', '%4$s', %5$s)", TABLE_NAME, id, sessionKey, createdTime, buildNumber));
    }
}

