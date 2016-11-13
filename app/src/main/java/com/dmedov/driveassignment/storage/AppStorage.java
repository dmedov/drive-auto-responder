package com.dmedov.driveassignment.storage;


import android.content.SharedPreferences;

public class AppStorage implements Storage {
    private static final String DRIVE_MODE = "drive_mode";
    private static final String RESPONSE_MESSAGE = "response_message";

    private SharedPreferences preferences;

    public AppStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean isDriveModeEnabled() {
        return getBooleanValue(DRIVE_MODE);
    }

    @Override
    public void setDriveMode(boolean enabled) {
        saveBoolean(DRIVE_MODE, enabled);
    }

    @Override
    public void setResponseMessage(String message) {
        saveString(RESPONSE_MESSAGE, message);
    }

    @Override
    public String getResponseMessage() {
        return getStringValue(RESPONSE_MESSAGE);
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    private String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    private boolean getBooleanValue(String key) {
        return preferences.getBoolean(key, false);
    }
}
