package com.dmedov.driveassignment.storage;


public interface Storage {
    boolean isDriveModeEnabled();
    void setDriveMode(boolean enabled);
    void setResponseMessage(String message);
    String getResponseMessage();
}