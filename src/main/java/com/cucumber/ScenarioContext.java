package com.cucumber;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private static final Map<String, Object> scenario = new HashMap<>();

    private ScenarioContext() {}

    public static void setContext(Context key, Object value) {
        scenario.put(key.toString(), value);
    }

    public static Object getContext(Context key) {
        return scenario.get(key.toString());
    }

    public static boolean isContains(Context key) {
        return scenario.containsKey(key.toString());
    }
}
