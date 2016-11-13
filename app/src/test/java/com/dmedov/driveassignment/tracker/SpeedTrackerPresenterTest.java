package com.dmedov.driveassignment.tracker;

import com.dmedov.driveassignment.storage.Storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.dmedov.driveassignment.tracker.SpeedTrackerPresenter.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpeedTrackerPresenterTest {

    @Mock
    Storage storage;

    @Mock
    View view;

    @Mock
    SpeedObserver speedObserver;

    private SpeedTrackerPresenter presenter;

    @Before
    public void init() {
        presenter = new SpeedTrackerPresenter(view, storage, speedObserver);
    }

    @Test
    public void testCheckPermissionsRequest() {
        when(storage.isDriveModeEnabled()).thenReturn(false);
        presenter.onDriveModeClicked();
        verify(view).checkGpsSmsPermissions();
    }

    @Test
    public void testCheckGpsSettingsEnabled() {
        when(view.isGpsEnabled()).thenReturn(false);
        presenter.onAllPermissionsGranted();
        verify(view).showDisabledGpsWarning();
        verify(view, never()).showTrackerRunMessage();
    }

    @Test
    public void testRunSpeedTracker() {
        when(view.isGpsEnabled()).thenReturn(true);
        presenter.onAllPermissionsGranted();
        verify(view).showTrackerRunMessage();
        verify(speedObserver).start();
    }

    @Test
    public void testStopSpeedTracker() {
        when(storage.isDriveModeEnabled()).thenReturn(true);
        presenter.onDriveModeClicked();
        verify(view).setDriveMode(false);
        verify(speedObserver).stop();
    }
}