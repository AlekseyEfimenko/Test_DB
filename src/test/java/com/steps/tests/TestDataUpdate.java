package com.steps.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestDataUpdate extends BaseTest {

    @BeforeMethod
    public void copyTests() {
        steps.copyRandomTestsFromDataBase();
    }

    @Test
    public void runCopiedTests() {
        steps.simulateRunningOfTests();
        steps.assertTestResultsIsUpdate();
    }

    @AfterMethod
    public void deleteTests() {
        steps.deleteCopiedTests();
        steps.assertTestsAreDeleted();
    }
}

