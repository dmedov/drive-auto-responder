package com.dmedov.driveassignment.tracker;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GpsSpeedObserver implements SpeedObserver {
    private static final double TRIGGER_SPEED_METERS_PER_SECONDS = 4.16667; // 15 km/h
    private Context context;

    public GpsSpeedObserver(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void start() {
        context.startService(new Intent(context, SpeedTrackerService.class));
    }

    @Override
    public void onSpeedDetermined(float speed) {
        if (speed > TRIGGER_SPEED_METERS_PER_SECONDS) {
            Log.d("GpsSpeedObserver", "15 km/h speed reached");
            turnOffScreen();
        } else {
            Log.d("GpsSpeedObserver", "speed is less then 15 km/h");
        }
    }

    private void turnOffScreen() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.lockNow();
    }

    @Override
    public void stop() {
        context.stopService(new Intent(context, SpeedTrackerService.class));
    }
}
