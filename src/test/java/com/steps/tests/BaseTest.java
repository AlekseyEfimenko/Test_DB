package com.steps.tests;

import com.cucumber.ScenarioContext;
import com.steps.TestSteps;
import com.utils.DataBaseManager;
import org.testng.annotations.AfterSuite;

public class BaseTest {
    protected TestSteps steps = new TestSteps();
    protected ScenarioContext scenarioContext = new ScenarioContext();

    @AfterSuite
    public void closeConnection() {
        DataBaseManager.closeConnection();
    }
}

