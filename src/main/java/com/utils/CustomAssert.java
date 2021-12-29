package com.utils;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import org.apache.log4j.Logger;

public class CustomAssert {
    private static final Logger LOGGER = Logger.getLogger(CustomAssert.class);

    private CustomAssert() {}

    public static void customAssertTrue(boolean condition) {
        try {
            assertTrue(condition);
        } catch (AssertionError ex) {
            LOGGER.error(String.format("Expected condition is true, but found condition is false: %s", ex.getMessage()));
            throw new AssertionError();
        }
    }

    public static void customAssertFalse(boolean condition) {
        try {
            assertFalse(condition);
        } catch (AssertionError ex) {
            LOGGER.error(String.format("Expected condition is false, but found condition is true: %s", ex.getMessage()));
            throw new AssertionError();
        }
    }

    public static <T> void customAssertEquals(T actual, T expected) {
        try {
            assertEquals(actual, expected);
        } catch (AssertionError ex) {
            LOGGER.error(String.format("This two objects are not equals: %s", ex.getMessage()));
            throw new AssertionError();
        }
    }

    public static <T> void customAssertNotEquals(T actual, T expected) {
        try {
            assertNotEquals(actual, expected);
        } catch (AssertionError ex) {
            LOGGER.error(String.format("This two objects are equals: %s", ex.getMessage()));
            throw new AssertionError();
        }
    }

    public static void customAssertNotNull(Object obj) {
        try {
            assertNotNull(obj);
        } catch (AssertionError ex) {
            LOGGER.error(String.format("This object is null: %s", ex.getMessage()));
            throw new AssertionError();
        }
    }
}

