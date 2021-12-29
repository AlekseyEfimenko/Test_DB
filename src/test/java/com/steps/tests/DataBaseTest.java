package com.steps.tests;

import com.steps.tests.utils.TestResultListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestResultListener.class)
public class DataBaseTest extends BaseTest {
    private static final int TEST_NUMBER = 15;
    private static final int FAILURE_TEST_NUMBER = 10;

    @Test
    public void runSomeTest() {
        steps.assertTwoNumbersAreEquals(TEST_NUMBER);
    }

    @Test
    public void testDataBaseActions() {
        steps.assertTestResultAddedToDataBase();
    }
}

