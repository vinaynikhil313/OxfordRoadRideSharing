package com.vinay.oxfordroadridesharing.main.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.main.presenter.OnConnectionEstablishedListener;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentInteractorImpl implements MainActivityFragmentInteractor,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Activity mActivity;

    private OnConnectionEstablishedListener listener;

    private final String TAG = Utilities.getTag (this);

    public MainActivityFragmentInteractorImpl () {

        mLocationRequest = new LocationRequest ();
        mLocationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval (10 * 1000);
        mLocationRequest.setFastestInterval (2 * 1000);

    }

    @Override
    public void establishConnection (Activity activity, OnConnectionEstablishedListener listener) {

        this.mActivity = activity;
        this.listener = listener;

        mGoogleApiClient = new GoogleApiClient.Builder (activity)
                .addApi (LocationServices.API)
                .addConnectionCallbacks (this)
                .addOnConnectionFailedListener (this)
                .build ();

        mGoogleApiClient.connect ();
    }

    @Override
    public void disconnectConnection () {
        if (mGoogleApiClient.isConnected ()) {
            LocationServices.FusedLocationApi.removeLocationUpdates (mGoogleApiClient, this);
            mGoogleApiClient.disconnect ();
        }
    }

    @Override
    public void onConnected (Bundle bundle) {
        Log.i (TAG, "Location Services Connected");
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i (TAG, "Permissions don't already Exist");
            ActivityCompat.requestPermissions (mActivity,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE_LOCATION);
        } else {
            Log.i (TAG, "Permissions already Exist or Version is not Marsh mellow");
            Location mLocation = LocationServices.FusedLocationApi.getLastLocation (mGoogleApiClient);
            if (mLocation != null)
                listener.onLocationDetected (new LatLng (mLocation.getLatitude (), mLocation.getLongitude ()));
            LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            //mGoogleMap.setMyLocationEnabled (true);
        }
    }

    @Override
    public void onConnectionSuspended (int i) {
        mGoogleApiClient.connect ();
    }

    @Override
    public void onConnectionFailed (ConnectionResult connectionResult) {
        Log.i (TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode ());
    }

    @Override
    public void onLocationChanged (Location location) {
        listener.onLocationDetected (new LatLng (location.getLatitude (), location.getLongitude ()));
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location mLocation = LocationServices.FusedLocationApi.getLastLocation (mGoogleApiClient);
                if (mLocation != null)
                    listener.onLocationDetected (new LatLng (mLocation.getLatitude (), mLocation.getLongitude ()));
                Log.i (TAG, "Permission received!");
                LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            } else {
                Toast.makeText (mActivity, "Unable to get Location Permissions", Toast.LENGTH_LONG).show ();
            }
        }
    }

}
