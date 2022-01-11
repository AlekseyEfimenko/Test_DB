package com.steps.tests;

import com.steps.TestSteps;
import com.utils.DataBaseManager;
import org.testng.annotations.AfterSuite;

public class BaseTest {
    protected TestSteps steps = new TestSteps();;

    @AfterSuite
    public void closeConnection() {
        DataBaseManager.closeConnection();
    }
}

