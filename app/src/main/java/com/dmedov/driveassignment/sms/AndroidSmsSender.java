package com.dmedov.driveassignment.sms;

import android.telephony.SmsManager;

public class AndroidSmsSender implements SmsSender {

    private SmsManager smsManager;

    public AndroidSmsSender() {
        this.smsManager = SmsManager.getDefault();
    }

    @Override
    public void send(String to, String message) {
        smsManager.sendTextMessage(to, null, message, null, null);
    }
}
