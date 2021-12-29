package com.steps.tests;

import com.steps.TestSteps;
import com.utils.DataBaseManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected TestSteps steps = new TestSteps();

    @BeforeSuite
    public void openConnection() {
        DataBaseManager.setConnection();
    }

    @AfterSuite
    public void closeConnection() {
        DataBaseManager.closeConnection();
    }
}

