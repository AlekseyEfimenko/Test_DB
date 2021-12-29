package com.steps;

import static com.utils.CustomAssert.customAssertEquals;
import static com.utils.CustomAssert.customAssertTrue;
import static com.utils.CustomAssert.customAssertNotEquals;
import com.steps.tests.utils.DataManager;
import com.tables.TestTable;
import com.utils.DataBaseActions;
import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


public class TestSteps {
    private static final Logger LOGGER = Logger.getLogger(TestSteps.class.getName());
    private static final String TEST_TABLE_NAME = "test";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_START_TIME = "start_time";
    private static final int RANDOM_AMOUNT_OF_TESTS = new Random().nextInt(5) + 5;
    private static final int START_ID = 10;
    private long maxTestIdBeforeTest;
    private List<TestTable> testTables = new ArrayList<>();
    private String idRange;
    private String testSimulatedId;
    private long maxIDBeforeSimulate;

    public void assertTwoNumbersAreEquals(int number) {
        maxTestIdBeforeTest = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME);
        LOGGER.info("Checking two numbers for equals");
        customAssertEquals(15, number);
    }

    public void assertTestResultAddedToDataBase() {
        long maxTestIdAfterTest = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME);
        LOGGER.info("Checking if id is increased after test result is added to the database");
        customAssertTrue(maxTestIdAfterTest > maxTestIdBeforeTest);
    }

    public void copyRandomTestsFromDataBase() {
        LOGGER.info(String.format("Copying %s tests from database and setting current project and author", RANDOM_AMOUNT_OF_TESTS));
        maxIDBeforeSimulate = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME) + 1;
        idRange = DataManager.getRandomIds(START_ID, (int) maxIDBeforeSimulate, RANDOM_AMOUNT_OF_TESTS);
        ResultSet resultSet = DataBaseActions.selectQuery("*", TEST_TABLE_NAME, String.format("%s IN (%s)", COLUMN_NAME_ID, idRange));
        testTables = DataManager.convertResultSetToList(resultSet);
        testTables.forEach(DataManager::updateAuthorAndProject);
        testTables.forEach(TestTable::addRowToTestTable);
    }

    public void simulateRunningOfTests() {
        LOGGER.info("Simulating copied tests");
        testTables.forEach(table -> {
            table.setStatusId(new Random().nextInt(3) + 1);
            table.setStartTime();
            table.setEndTime();
            table.setName(table.getName().replace("'", ""));
        });
        LOGGER.info("Adding simulated tests results");
        testTables.forEach(table -> DataBaseActions.updateRecord(String.format("UPDATE test SET status_id = %s, start_time = '%s', end_time = '%s' WHERE id = %s",
                    table.getStatusId(), table.getStartTime(), table.getEndTime(), table.getId())));
    }

    public void assertTestResultsIsUpdate() {
        ResultSet startTimeBeforeSimulate = DataBaseActions.selectQuery(COLUMN_NAME_START_TIME, TEST_TABLE_NAME, String.format("%s IN (%s)", COLUMN_NAME_ID, idRange));
        long maxIdAfterSimulate = (long) DataBaseActions.getMax(COLUMN_NAME_ID, TEST_TABLE_NAME) +1;
        String ids = Arrays.toString(IntStream.range((int) maxIDBeforeSimulate, (int) maxIdAfterSimulate).toArray());
        testSimulatedId = ids.substring(1, ids.length() - 1);
        ResultSet startTimeAfterSimulate = DataBaseActions.selectQuery(COLUMN_NAME_START_TIME, TEST_TABLE_NAME, String.format("%s IN (%s)", COLUMN_NAME_ID, testSimulatedId));
        LOGGER.info(String.format("Checking if values in column %s before simulation and after simulation are different", COLUMN_NAME_START_TIME));
        try {
            while (startTimeAfterSimulate.next() && startTimeBeforeSimulate.next()) {
                customAssertNotEquals(startTimeBeforeSimulate.getTimestamp(COLUMN_NAME_START_TIME), startTimeAfterSimulate.getTimestamp(COLUMN_NAME_START_TIME));
            }
        } catch (SQLException ex) {
            LOGGER.info("Can't get access to database or incorrect SQL query");
        }
    }

    public void deleteCopiedTests() {
        LOGGER.info(String.format("Deleting %s tests, that was copied for simulation", RANDOM_AMOUNT_OF_TESTS));
        DataBaseActions.deleteRecord(String.format("DELETE FROM %s WHERE id IN (%s)", TEST_TABLE_NAME, testSimulatedId));
    }

    public void assertTestsAreDeleted() {
        LOGGER.info("Checking if copied tests are deleted from database");
        customAssertTrue(DataBaseActions.isEmpty("*", TEST_TABLE_NAME, String.format("%s IN (%s)", COLUMN_NAME_ID, testSimulatedId)));
    }
}

