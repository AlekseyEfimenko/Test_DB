package com.cucumber;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private final Map<String, Object> scenario;

    public ScenarioContext() {
        scenario = new HashMap<>();
    }

    public void setContext(Context key, Object value) {
        scenario.put(key.toString(), value);
    }

    public Object getContext(Context key) {
        return scenario.get(key.toString());
    }

    public boolean isContains(Context key) {
        return scenario.containsKey(key.toString());
    }
}
