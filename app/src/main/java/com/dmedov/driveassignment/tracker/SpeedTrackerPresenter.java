package com.dmedov.driveassignment.tracker;

import com.dmedov.driveassignment.storage.Storage;

public class SpeedTrackerPresenter  {

    private View view;
    private Storage storage;
    private SpeedObserver speedObserver;

    public SpeedTrackerPresenter(View view, Storage storage, SpeedObserver observer) {
        this.view = view;
        this.storage = storage;
        this.speedObserver = observer;
    }

    public void init() {
        view.setDriveMode(storage.isDriveModeEnabled());

        if (storage.isDriveModeEnabled()) {
            view.checkGpsSmsPermissions();
        }
    }

    public void onDriveModeClicked() {
        view.setDriveMode(storage.isDriveModeEnabled());
        if (storage.isDriveModeEnabled()) {
            stopSpeedTracker();
            storage.setDriveMode(false);
            view.setDriveMode(false);
        } else {
            view.checkGpsSmsPermissions();
        }
    }

    private void stopSpeedTracker() {
        speedObserver.stop();
        view.showTrackerStopMessage();
    }

    public void onAllPermissionsGranted() {
        if (view.isGpsEnabled()) {
            runSpeedTracker();
            view.setDriveMode(true);
            storage.setDriveMode(true);
        } else {
            view.showDisabledGpsWarning();
        }
    }

    public void runSpeedTracker() {
        speedObserver.start();
        view.showTrackerRunMessage();
    }

    public void saveResponseText(String text) {
        storage.setResponseMessage(text);
    }

    public interface View {
        void setDriveMode(boolean enabled);
        void checkGpsSmsPermissions();
        void showDisabledGpsWarning();
        boolean isGpsEnabled();
        void showTrackerRunMessage();
        void showTrackerStopMessage();
    }
}

