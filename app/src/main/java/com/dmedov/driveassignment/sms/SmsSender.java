package com.dmedov.driveassignment.sms;

public interface SmsSender {
    void send(String to, String message);
}
