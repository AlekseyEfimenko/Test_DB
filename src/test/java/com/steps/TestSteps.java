package com.steps;

import static com.utils.CustomAssert.customAssertEquals;
import static com.utils.CustomAssert.customAssertTrue;
import static com.utils.CustomAssert.customAssertNotEquals;
import com.steps.tests.utils.DataManager;
import com.tables.TestTable;
import com.utils.Config;
import com.utils.DataBaseManager;
import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TestSteps {
    private static final Logger LOGGER = Logger.getLogger(TestSteps.class.getName());
    private static final Config CONFIG = Config.getInstance();
    private static final String TEST_TABLE_NAME = "test";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_START_TIME = "start_time";
    private static final int RANDOM_AMOUNT_OF_TESTS = new Random().nextInt(5) + 5;
    private static final int START_ID = 10;
    private final DataBaseManager dbActions = DataBaseManager.getInstance();
    private long maxTestIdBeforeTest;

    public void assertTwoNumbersAreEquals(int number) {
        maxTestIdBeforeTest = (long) dbActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME);
        LOGGER.info("Checking two numbers for equals");
        customAssertEquals(15, number);
    }

    public void assertTestResultAddedToDataBase() {
        long maxTestIdAfterTest = (long) dbActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME);
        LOGGER.info("Checking if id is increased after test result is added to the database");
        customAssertTrue(maxTestIdAfterTest > maxTestIdBeforeTest);
    }

    public List<TestTable> copyRandomTestsFromDataBase(String rangeOfSuitableIds) {
        LOGGER.info(String.format("Copying %1$s tests from database and setting current project and author", RANDOM_AMOUNT_OF_TESTS));
        ResultSet resultSet = dbActions.selectQuery("*", TEST_TABLE_NAME, String.format("%1$s IN (%2$s)", COLUMN_NAME_ID, rangeOfSuitableIds));
        List<TestTable> testTables = DataManager.convertResultSetToList(resultSet);
        testTables.forEach(DataManager::updateAuthorAndProject);
        testTables.forEach(TestTable::addRowToTestTable);
        return testTables;
    }

    public long getMaxIDBeforeSimulate() {
        return (long) dbActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME) + 1;

    }

    public String getIdRange(long currentMaxId) {
        return DataManager.getRandomIds(START_ID, (int) currentMaxId, RANDOM_AMOUNT_OF_TESTS);
    }

    public void simulateRunningOfTests(List<TestTable> tables) {
        LOGGER.info("Simulating copied tests");
        tables.forEach(table -> {
            table.setStatusId(new Random().nextInt(3) + 1);
            table.setStartTime();
            table.setEndTime();
            table.setName(table.getName().replace("'", ""));
        });
        LOGGER.info("Adding simulated tests results");
        tables.forEach(table -> dbActions.updateRecord(String.format(CONFIG.getSQLQuery("sql_query/update_test.sql"),
                    table.getStatusId(), table.getStartTime(), table.getEndTime(), table.getId())));
    }

    public void assertTestResultsIsUpdate(String rangeOfTestIds, String rangeOfSimulatedIds) {
        ResultSet startTimeBeforeSimulate = getStartTimeBeforeSimulate(rangeOfTestIds);
        ResultSet startTimeAfterSimulate = getStartTimeAfterSimulate(rangeOfSimulatedIds);
        LOGGER.info(String.format("Checking if values in column %1$s before simulation and after simulation are different", COLUMN_NAME_START_TIME));
        try {
            while (startTimeAfterSimulate.next() && startTimeBeforeSimulate.next()) {
                customAssertNotEquals(startTimeBeforeSimulate.getTimestamp(COLUMN_NAME_START_TIME), startTimeAfterSimulate.getTimestamp(COLUMN_NAME_START_TIME));
            }
        } catch (SQLException ex) {
            LOGGER.info(String.format("Can't get access to database or incorrect SQL query%n%1$s", ex.getMessage()));
        }
    }

    private ResultSet getStartTimeBeforeSimulate(String rangeOfTestIds) {
        return dbActions.selectQuery(COLUMN_NAME_START_TIME, TEST_TABLE_NAME, String.format("%1$s IN (%2$s)", COLUMN_NAME_ID, rangeOfTestIds));
    }

    private ResultSet getStartTimeAfterSimulate(String rangeOfSimulatedIds) {
        return dbActions.selectQuery(COLUMN_NAME_START_TIME, TEST_TABLE_NAME, String.format("%1$s IN (%2$s)", COLUMN_NAME_ID, rangeOfSimulatedIds));
    }

    public String getTestSimulatedId(long maxIdBeforeSimulation) {
        long maxIdAfterSimulate = (long) dbActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME) + 1;
        String ids = Arrays.toString(IntStream.range((int) maxIdBeforeSimulation, (int) maxIdAfterSimulate).toArray());
        return ids.substring(1, ids.length() - 1);
    }

    public void deleteCopiedTests(String rangeOfSimulatedIds) {
        LOGGER.info(String.format("Deleting %1$s tests, that was copied for simulation", RANDOM_AMOUNT_OF_TESTS));
        dbActions.deleteRecord(String.format(CONFIG.getSQLQuery("sql_query/delete_by_id.sql"), TEST_TABLE_NAME, rangeOfSimulatedIds));
    }

    public void assertTestsAreDeleted(String rangeOfSimulatedIds) {
        LOGGER.info("Checking if copied tests are deleted from database");
        customAssertTrue(dbActions.isEmpty("*", TEST_TABLE_NAME, String.format("%1$s IN (%2$s)", COLUMN_NAME_ID, rangeOfSimulatedIds)));
    }
}

