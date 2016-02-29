package com.vinay.oxfordroadridesharing.main.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.vinay.oxfordroadridesharing.application.OxfordRoadRideSharingApplication;
import com.vinay.oxfordroadridesharing.main.presenter.OnConnectionEstablishedListener;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentInteractorImpl implements MainActivityFragmentInteractor,
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

        mGoogleApiClient = OxfordRoadRideSharingApplication.getGoogleApiHelper ().getGoogleApiClient ();

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
                onLocationChanged (mLocation);
            LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void requestLocationUpdates () {

        Log.i(TAG, "Request Location Updates is Connected = " + mGoogleApiClient.isConnected ());

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected ()) {
            if (ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    @Override
    public void disconnectConnection () {

        Log.i(TAG, "Disconnect Connection is Connected = " + mGoogleApiClient.isConnected ());

        if (mGoogleApiClient.isConnected ()) {
            LocationServices.FusedLocationApi.removeLocationUpdates (mGoogleApiClient, this);
        }
    }

    @Override
    public void showPlaceAutoComplete (Fragment fragment) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder (PlaceAutocomplete.MODE_OVERLAY)
                            .build (mActivity);
            fragment.startActivityForResult (intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void destinationReceived (int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace (mActivity, data);
            Log.i (TAG, "Place: " + place.getName ());
            listener.onDestinationReceived (place);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus (mActivity, data);
            // TODO: Handle the error.
            Log.i (TAG, status.getStatusMessage ());

        } else if (resultCode == mActivity.RESULT_CANCELED) {
            // The user canceled the operation.
        } else {
            Status status = PlaceAutocomplete.getStatus (mActivity, data);
            Log.i (TAG, status.getStatusMessage ());
        }
    }

    @Override
    public void onLocationChanged (final Location location) {

        if (ActivityCompat.checkSelfPermission (mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace (mGoogleApiClient, null);
            result.setResultCallback (new ResultCallback<PlaceLikelihoodBuffer> () {
                @Override
                public void onResult (PlaceLikelihoodBuffer likelyPlaces) {
                    Log.i (TAG, likelyPlaces.get (0).getPlace ().toString ());
                    listener.onLocationDetected (likelyPlaces.get (0).getPlace ());
                    likelyPlaces.release ();
                }
            });
        }

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
                    onLocationChanged (mLocation);
                Log.i (TAG, "Permission received!");
                LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            } else {
                Toast.makeText (mActivity, "Unable to get Location Permissions", Toast.LENGTH_LONG).show ();
            }
        }
    }

}
