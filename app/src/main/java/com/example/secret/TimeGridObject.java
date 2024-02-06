package com.example.secret;

public class TimeGridObject implements GridObject {

    private String displayName;
    public TimeGridObject(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
