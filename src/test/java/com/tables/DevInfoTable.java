package com.tables;

import com.utils.DataBaseActions;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DevInfoTable {
    private static final String TABLE_NAME = "dev_info";
    private static final String COLUMN_NAME_ID = "id";
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
        if (DataBaseActions.isEmpty(TABLE_NAME)) {
            id = 1;
        } else {
            id = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        }
        DataBaseActions.insertQuery(String.format("INSERT INTO %s VALUES (%s, %s, %s)", TABLE_NAME, id, devTime, testId));
    }
}

