package com.steps.tests;

import com.tables.TestTable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

public class TestDataUpdate extends BaseTest {
    private List<TestTable> testTables = new ArrayList<>();
    private String idRange;
    private String testSimulatedId;
    private long maxIDBeforeSimulate;

    @BeforeMethod
    public void copyTests() {
        maxIDBeforeSimulate = steps.getMaxIDBeforeSimulate();
        idRange = steps.getIdRange(maxIDBeforeSimulate);
        testTables = steps.copyRandomTestsFromDataBase(idRange);
    }

    @Test
    public void runCopiedTests() {
        steps.simulateRunningOfTests(testTables);
        testSimulatedId = steps.getTestSimulatedId(maxIDBeforeSimulate);
        steps.assertTestResultsIsUpdate(idRange, testSimulatedId);
    }

    @AfterMethod
    public void deleteTests() {
        steps.deleteCopiedTests(testSimulatedId);
        steps.assertTestsAreDeleted(testSimulatedId);
    }
}

