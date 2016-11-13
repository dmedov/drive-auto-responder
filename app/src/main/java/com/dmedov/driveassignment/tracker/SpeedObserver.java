package com.dmedov.driveassignment.tracker;

public interface SpeedObserver {
    void start();
    void stop();
    void onSpeedDetermined(float speed);
}
