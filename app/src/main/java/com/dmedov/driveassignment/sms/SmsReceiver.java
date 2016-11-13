package com.dmedov.driveassignment.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.dmedov.driveassignment.app.App;
import com.dmedov.driveassignment.storage.Storage;

public class SmsReceiver extends BroadcastReceiver {

    public void onReceive(final Context context, Intent intent) {
        Storage storage = ((App)context.getApplicationContext()).getStorage();
        SmsPresenter presenter = new SmsPresenter(new AndroidSmsSender(), storage);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            if (pdus == null) {
                return;
            }

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = getSmsMessageFromPdu(bundle, (byte[])pdus[i]);
                presenter.onMessageReceived(smsMessage.getOriginatingAddress());
                Log.v("SMS", "sms from:" + smsMessage.getOriginatingAddress());
            }
        }
    }

    private SmsMessage getSmsMessageFromPdu(Bundle bundle, byte[] pdu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            return SmsMessage.createFromPdu(pdu, format);
        } else {
            return SmsMessage.createFromPdu(pdu);
        }
    }
}
