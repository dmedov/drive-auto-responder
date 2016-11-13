package com.dmedov.driveassignment.tracker;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class SpeedTrackerService extends Service {
    public static final int MIN_ACCURACY_METERS = 60;
    public static final int LOCATION_UPDATES_INTERVAL_MS = 500;

    private CompositeSubscription compositeSubscription;
    private SpeedObserver speedObserver;

    @Override
    @SuppressWarnings({"MissingPermission"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        unsubscribeRxLocationUpdates();
        speedObserver = new GpsSpeedObserver(getApplicationContext());

        if (!isLocationPermissionsEnabled()) {
            return START_STICKY;
        }

        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATES_INTERVAL_MS);

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getApplicationContext());
        Subscription subscription = locationProvider.getUpdatedLocation(request)
                                        .subscribe(new Action1<Location>() {
                                            @Override
                                            public void call(Location location) {
                                                if (location != null) {
                                                    Log.v("TrackerService", "on location determined " + location.toString());
                                                }

                                                if (location != null &&
                                                    location.getSpeed() != 0.0 && // getSpeed() returns 0.0 if speeds is not determined
                                                    location.getAccuracy() < MIN_ACCURACY_METERS) {
                                                    speedObserver.onSpeedDetermined(location.getSpeed());
                                                }
                                            }
                                        }, new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                Log.e("TrackerService", "can't determine speed: " + throwable.getMessage());
                                            }
                                        });

        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(subscription);

        Log.d("TrackerService", "speed tracker started");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isLocationPermissionsEnabled() {
        int granted = PackageManager.PERMISSION_GRANTED;
        return ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)  == granted &&
               ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == granted;
    }

    @Override
    public void onDestroy() {
        unsubscribeRxLocationUpdates();
        Log.d("TrackerService", "speed tracker stopped");
        super.onDestroy();
    }

    private void unsubscribeRxLocationUpdates() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}

