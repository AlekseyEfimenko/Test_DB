package com.steps.tests;

import com.cucumber.Context;
import com.tables.TestTable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

public class TestDataUpdate extends BaseTest {
    private List<TestTable> testTables = new ArrayList<>();

    @BeforeMethod
    public void copyTests() {
        scenarioContext.setContext(Context.MAX_ID_BEFORE_SIMULATE, steps.getMaxIDBeforeSimulate());
        scenarioContext.setContext(Context.ID_RANGE, steps.getIdRange((long) scenarioContext.getContext((Context.MAX_ID_BEFORE_SIMULATE))));
        testTables = steps.copyRandomTestsFromDataBase((String) scenarioContext.getContext(Context.ID_RANGE));
    }

    @Test
    public void runCopiedTests() {
        steps.simulateRunningOfTests(testTables);
        scenarioContext.setContext(Context.TEST_SIMULATED_IDS, steps.getTestSimulatedId((long) scenarioContext.getContext((Context.MAX_ID_BEFORE_SIMULATE))));
        steps.assertTestResultsIsUpdate((String) scenarioContext.getContext(Context.ID_RANGE), (String) scenarioContext.getContext(Context.TEST_SIMULATED_IDS));
    }

    @AfterMethod
    public void deleteTests() {
        steps.deleteCopiedTests((String) scenarioContext.getContext(Context.TEST_SIMULATED_IDS));
        steps.assertTestsAreDeleted((String) scenarioContext.getContext(Context.TEST_SIMULATED_IDS));
    }
}

