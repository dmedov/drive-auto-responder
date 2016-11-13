package com.dmedov.driveassignment.sms;


import com.dmedov.driveassignment.storage.Storage;

public class SmsPresenter {

    private SmsSender sender;
    private Storage storage;

    public SmsPresenter(SmsSender sender, Storage storage) {
        this.sender = sender;
        this.storage = storage;
    }

    public void onMessageReceived(String from) {
        if (storage.isDriveModeEnabled()) {
            sender.send(from, storage.getResponseMessage());
        }
    }
}
