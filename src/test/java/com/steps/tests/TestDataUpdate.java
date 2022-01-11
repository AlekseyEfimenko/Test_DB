package com.steps.tests;

import static com.cucumber.ScenarioContext.setContext;
import static com.cucumber.ScenarioContext.getContext;
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
        setContext(Context.MAX_ID_BEFORE_SIMULATE, steps.getMaxIDBeforeSimulate());
        setContext(Context.ID_RANGE, steps.getIdRange((long) getContext((Context.MAX_ID_BEFORE_SIMULATE))));
        testTables = steps.copyRandomTestsFromDataBase((String) getContext(Context.ID_RANGE));
    }

    @Test
    public void runCopiedTests() {
        steps.simulateRunningOfTests(testTables);
        setContext(Context.TEST_SIMULATED_IDS, steps.getTestSimulatedId((long) getContext((Context.MAX_ID_BEFORE_SIMULATE))));
        steps.assertTestResultsIsUpdate((String) getContext(Context.ID_RANGE), (String) getContext(Context.TEST_SIMULATED_IDS));
    }

    @AfterMethod
    public void deleteTests() {
        steps.deleteCopiedTests((String) getContext(Context.TEST_SIMULATED_IDS));
        steps.assertTestsAreDeleted((String) getContext(Context.TEST_SIMULATED_IDS));
    }
}

