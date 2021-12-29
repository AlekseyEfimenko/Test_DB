package com.steps.tests.utils;

import com.tables.AuthorTable;
import com.tables.DevInfoTable;
import com.tables.LogTable;
import com.tables.ProjectTable;
import com.tables.SessionTable;
import com.tables.TestTable;
import com.utils.Config;
import com.utils.DataBaseActions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestResultListener extends TestListenerAdapter {
    private static final String COLUMN_NAME_ID = "id";
    private static final String ERROR_FILE_NAME = "error.txt";
    private static final String LOG_PATH = "target/log/log.log";
    private static final String METHOD_NAME = "runSomeTest";
    private final PrintStream errorLog = DataManager.createFile(ERROR_FILE_NAME);
    TestTable testTable = new TestTable();
    ProjectTable projectTable = new ProjectTable();
    SessionTable sessionTable = new SessionTable();
    AuthorTable authorTable = new AuthorTable();
    LogTable logTable = new LogTable();
    DevInfoTable devInfoTable = new DevInfoTable();

    @Override
    public void onTestStart(ITestResult result) {
        if (result.getName().equals(METHOD_NAME)) {
            authorTable.addRowToAuthorTable();
            sessionTable.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            projectTable.addRowToProjectTable();
            testTable.setName(result.getMethod().getDescription());
            testTable.setStartTime();
            testTable.setProjectId((long) DataBaseActions.getFirst(COLUMN_NAME_ID, "project", "name = 'Test'"));
            testTable.setAuthorId((long) DataBaseActions.getFirst(COLUMN_NAME_ID, "author", String.format("email = '%s'",
                    Config.getInstance().getDataProperties("email"))));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (result.getName().equals(METHOD_NAME)) {
            logTable.setErrorInfo("");
            sessionTable.setSessionKey(String.valueOf(result.getStartMillis()));
            sessionTable.addRowToSessionTable();
            testTable.setStatusId(result.getStatus());
            testTable.setMethodName(String.format("%s.%s", result.getInstanceName(), result.getName()));
            testTable.setEndTime();
            testTable.setSessionId((long) DataBaseActions.getMax(COLUMN_NAME_ID, "session"));
            testTable.addRowToTestTable();
            logTable.setTestId(testTable.getId());
            devInfoTable.setTestId(testTable.getId());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getName().equals(METHOD_NAME)) {
            sessionTable.setSessionKey(String.valueOf(result.getStartMillis()));
            sessionTable.addRowToSessionTable();
            testTable.setStatusId(result.getStatus());
            testTable.setMethodName(String.format("%s.%s", result.getInstanceName(), result.getName()));
            testTable.setEndTime();
            testTable.setSessionId((long) DataBaseActions.getMax(COLUMN_NAME_ID, "session"));
            testTable.addRowToTestTable();
            logTable.setTestId(testTable.getId());
            devInfoTable.setTestId(testTable.getId());
            result.getThrowable().printStackTrace(new PrintStream(errorLog));
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
            devInfoTable.setDevTime(testTable.getDevTime());
            devInfoTable.addRowToDevInfoTable();
            logTable.setErrorInfo(DataManager.printLog(ERROR_FILE_NAME));
            logTable.setContent(DataManager.printLog(LOG_PATH), testTable.getName());
            logTable.addRowToLogTable();
    }
}

