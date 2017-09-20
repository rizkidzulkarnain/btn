package com.mitkoindo.smartcollection.backgroundservice;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mitkoindo.smartcollection.FetchAddressIntentService;
import com.mitkoindo.smartcollection.R;
import com.mitkoindo.smartcollection.network.ApiUtils;
import com.mitkoindo.smartcollection.network.RestConstants;
import com.mitkoindo.smartcollection.network.body.CheckInBody;
import com.mitkoindo.smartcollection.network.response.CheckInResponse;

import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ericwijaya on 9/17/17.
 */

public class MyJobServiceTracking extends JobService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQ_CODE_LOCATION_PERMISSION = 1;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_ID = "user_id";

    private CompositeDisposable mComposites = new CompositeDisposable();
    private JobParameters mJobParameters;
    private String mAccessToken;
    private String mUserId;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private Location mLastKnownLocation;
    private String mAddressOutput;
    private boolean mAddressRequested;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.i("onCreate");

        initGoogleService();
        mGoogleApiClient.connect();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressOutput = "";
        mAddressRequested = false;
    }

    @Override
    public void onDestroy() {
        Timber.i("onDestroy");

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onDestroy();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Timber.i("onStartJob");

        mJobParameters = job;
        mAccessToken = job.getExtras().getString(ACCESS_TOKEN);
        mUserId = job.getExtras().getString(USER_ID);

        requestAccessLocationPermission();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Timber.i("onStopJob");
        return false;
    }

    // Create a GoogleApiClient instance
    private void initGoogleService() {
        Timber.i("initGoogleService");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle aBundle) {
        Timber.i("GoogleService onConnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.i("GoogleService onConnectionFailed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.i("GoogleService onConnectionSuspended");
    }

    private void requestAccessLocationPermission() {
        if (isLocationPermissionGranted()) {
            Timber.i("getLastKnownLocation");
            getLastKnownLocation();
        }
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void getLastKnownLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;

                                Timber.i("getLastKnownLocation success");
                                Timber.i("Latitude : " + location.getLatitude());
                                Timber.i("Longitude : " + location.getLongitude());
//                                Get Address string
                                startIntentService(location);
                                mAddressRequested = true;
                            }
                        }
                    });
        } catch (SecurityException e) {
        }
    }

    private void startIntentService(Location location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in Activity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

            Timber.i("getAddress success : " + mAddressOutput);
            trackingLocation(mUserId, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), mAddressOutput);

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }

    public void trackingLocation(String userId, double latitude, double longitude, String address) {
        CheckInBody checkInBody = new CheckInBody();
        checkInBody.setDatabaseId(RestConstants.DATABASE_ID_VALUE);
        checkInBody.setSpName(RestConstants.CHECK_IN_SP_NAME);

        CheckInBody.SpParameter spParameter = new CheckInBody.SpParameter();
        spParameter.setUserID(userId);
        spParameter.setLatitude(latitude);
        spParameter.setLongitude(longitude);
        spParameter.setAddress(address);
        spParameter.setIsCheckin("0");

        checkInBody.setSpParameter(spParameter);

        Disposable disposable = ApiUtils.getRestServices(mAccessToken).checkIn(checkInBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CheckInResponse>>() {
                    @Override
                    public void onNext(List<CheckInResponse> listCheckInResponse) {
                        Timber.i("send trackingLocation Success");

//                        NotificationCompat.Builder mBuilder =
//                                new NotificationCompat.Builder(MyJobServiceTracking.this)
//                                        .setSmallIcon(R.drawable.ic_play_icon)
//                                        .setContentTitle("Tracking Location")
//                                        .setContentText(mAddressOutput);
//
//                        Random r = new Random();
//                        int mNotificationId = r.nextInt((1000-10)+1)+10;
//                        // Gets an instance of the NotificationManager service
//                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        // Builds the notification and issues it.
//                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("send trackingLocation Error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mComposites.add(disposable);
    }
}
