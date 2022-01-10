package com.steps.tests.utils;

import com.tables.AuthorTable;
import com.tables.DevInfoTable;
import com.tables.LogTable;
import com.tables.ProjectTable;
import com.tables.SessionTable;
import com.tables.TestTable;
import com.utils.Config;
import com.utils.DataBaseManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestResultListener extends TestListenerAdapter {
    private static final String COLUMN_NAME_ID = "id";
    private static final String ERROR_FILE_NAME = "error.txt";
    private static final String LOG_PATH = String.format("%1$s/log/log.log", Config.getInstance().getRootPath());
    private static final String METHOD_NAME = "runSomeTest";
    private final PrintStream errorLog = DataManager.createFile(ERROR_FILE_NAME);
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private final TestTable testTable = new TestTable();
    private final ProjectTable projectTable = new ProjectTable();
    private final SessionTable sessionTable = new SessionTable();
    private final AuthorTable authorTable = new AuthorTable();
    private final LogTable logTable = new LogTable();
    private final DevInfoTable devInfoTable = new DevInfoTable();

    @Override
    public void onTestStart(ITestResult result) {
        if (result.getName().equals(METHOD_NAME)) {
            authorTable.addRowToAuthorTable();
            sessionTable.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            projectTable.addRowToProjectTable();
            testTable.setName(result.getMethod().getDescription());
            testTable.setStartTime();
            testTable.setProjectId((long) dbActions.getFirst(COLUMN_NAME_ID, "project", "name = 'Test'"));
            testTable.setAuthorId((long) dbActions.getFirst(COLUMN_NAME_ID, "author", String.format("email = '%1$s'",
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
            testTable.setMethodName(String.format("%1$s.%2$s", result.getInstanceName(), result.getName()));
            testTable.setEndTime();
            testTable.setSessionId((long) dbActions.getMax(COLUMN_NAME_ID, "session"));
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
            testTable.setMethodName(String.format("%1$s.%2$s", result.getInstanceName(), result.getName()));
            testTable.setEndTime();
            testTable.setSessionId((long) dbActions.getMax(COLUMN_NAME_ID, "session"));
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

