package com.tables;

import com.utils.DataBaseManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DevInfoTable {
    private static final String TABLE_NAME = "dev_info";
    private static final String COLUMN_NAME_ID = "id";
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private long id;
    private long testId;
    private double devTime;

    public void setDevTime(long time) {
        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
        ts.setTime(time);
        devTime = ts.getTime();
    }

    public void setTestId(long id) {
        testId = id;
    }

    public void addRowToDevInfoTable() {
        id = dbActions.isEmpty(TABLE_NAME) ? 1 : (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        dbActions.insertQuery(String.format("INSERT INTO %1$s VALUES (%2$s, %3$s, %4$s)", TABLE_NAME, id, devTime, testId));
    }
}

