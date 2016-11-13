package com.dmedov.driveassignment.ui;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



import com.dmedov.driveassignment.R;
import com.dmedov.driveassignment.app.App;
import com.dmedov.driveassignment.app.PolicyAdminReceiver;
import com.dmedov.driveassignment.storage.Storage;
import com.dmedov.driveassignment.tracker.GpsSpeedObserver;
import com.dmedov.driveassignment.tracker.SpeedTrackerPresenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SpeedTrackerPresenter.View {
    public static final int REQ_ACTIVATE_DEVICE_ADMIN = 75;

    private SpeedTrackerPresenter presenter;
    private CheckBox driveModeCheckBox;
    private EditText responseMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        driveModeCheckBox = (CheckBox) findViewById(R.id.drive_mode);

        Storage storage = ((App) getApplication()).getStorage();
        presenter = new SpeedTrackerPresenter(this,
                                              storage,
                                              new GpsSpeedObserver(getApplicationContext()));
        presenter.init();

        responseMessage = (EditText) findViewById(R.id.response_text);
        driveModeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveResponseText(responseMessage.getText().toString());
                presenter.onDriveModeClicked();
            }
        });
    }

    @Override
    public void setDriveMode(boolean enabled) {
        driveModeCheckBox.setChecked(enabled);
    }

    @Override
    public void checkGpsSmsPermissions() {
        if (!Dexter.isRequestOngoing()) {
            Dexter.checkPermissions(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    // we check device admin permission to turn off screen when reach 15 km/h
                    checkDeviceAdminPermission();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
            }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);
        }
    }

    @Override
    public void showDisabledGpsWarning() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.gps_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean isGpsEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void showTrackerRunMessage() {
        Toast.makeText(this, R.string.tracker_is_running, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTrackerStopMessage() {
    }

    public void checkDeviceAdminPermission() {
        DevicePolicyManager policyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponent = new ComponentName(getApplicationContext(), PolicyAdminReceiver.class);
        if (!policyManager.isAdminActive(deviceAdminComponent)) {

            Intent activateDeviceAdminIntent =
                    new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

            activateDeviceAdminIntent.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    deviceAdminComponent);

            activateDeviceAdminIntent.putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getResources().getString(R.string.device_admin_activation_message));

            startActivityForResult(activateDeviceAdminIntent,
                    REQ_ACTIVATE_DEVICE_ADMIN);
        } else {
            presenter.onAllPermissionsGranted();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ACTIVATE_DEVICE_ADMIN && resultCode == RESULT_OK) {
            // User just activated the application as a device administrator.
            presenter.onAllPermissionsGranted();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
