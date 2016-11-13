package com.dmedov.driveassignment.app;

import android.app.Application;

import com.dmedov.driveassignment.storage.AppStorage;
import com.dmedov.driveassignment.storage.Storage;
import com.karumi.dexter.Dexter;

public class App extends Application {

    private Storage storage;

    @Override public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);

        storage = new AppStorage(getSharedPreferences("drive_pref", 0));
    }

    public Storage getStorage() {
        return storage;
    }
}
