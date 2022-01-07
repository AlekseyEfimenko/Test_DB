package com.tables;

import com.utils.Config;
import com.utils.DataBaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestTable {
    private static final String TABLE_NAME = "test";
    private static final String COLUMN_NAME_ID = "id";
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private long id;
    private String name;
    private int statusId;
    private String methodName;
    private long projectId;
    private long sessionId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String env = "Some environment";
    private String browser= null;
    private long authorId;

    public TestTable() {}

    public TestTable(ResultSet resultSet) throws SQLException {
        id = resultSet.getLong("id");
        name = resultSet.getString("name");
        statusId= resultSet.getInt("status_id");
        methodName = resultSet.getString("method_name");
        projectId = resultSet.getLong("project_id");
        sessionId = resultSet.getLong("session_id");
        startTime = resultSet.getTimestamp("start_time");
        endTime = resultSet.getTimestamp("end_time");
        env = resultSet.getString("env");
        browser = resultSet.getString("browser");
        authorId = resultSet.getInt("author_id");
    }

    public void setStartTime() {
        startTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public void setEndTime() {
        endTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public void setStatusId(int status) {
        statusId = status;
    }

    public void setMethodName(String name) {
        methodName = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectId(long id) {
        projectId = id;
    }

    public void setSessionId(long id) {
        sessionId = id;
    }

    public void setAuthorId(long id) {
        authorId = id;
    }

    public void addRowToTestTable() {
        id = dbActions.isEmpty(TABLE_NAME) ? 1 : (long) dbActions.getMax(COLUMN_NAME_ID, TABLE_NAME) + 1;
        dbActions.insertQuery(String.format(Config.getInstance().getSQLQuery("sql_query/insert_11values.sql"),
                    TABLE_NAME, id, name, statusId, methodName, projectId, sessionId, startTime, endTime, env, browser, authorId));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDevTime() {
        return startTime.getTime() - endTime.getTime();
    }

    public int getStatusId() {
        return statusId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }
}

