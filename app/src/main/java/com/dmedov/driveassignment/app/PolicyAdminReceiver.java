package com.dmedov.driveassignment.app;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;


public class PolicyAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }
}
