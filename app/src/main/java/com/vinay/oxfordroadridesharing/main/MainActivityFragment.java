package com.vinay.oxfordroadridesharing.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private final String TAG = Utilities.getTag (this);

    private GoogleApiClient mGoogleApiClient;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private Location mLocation;
    private LocationRequest mLocationRequest;

    View view;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        view = inflater.inflate (R.layout.maps_fragment, container, false);

        setupMapAndGoogleAPI (savedInstanceState);

        return view;
    }

    private void setupMapAndGoogleAPI (Bundle savedInstanceState) {

        mGoogleApiClient = new GoogleApiClient.Builder (getContext ())
                .addApi (LocationServices.API)
                .addConnectionCallbacks (this)
                .addOnConnectionFailedListener (this)
                .build ();

        mGoogleApiClient.connect ();

        mLocationRequest = new LocationRequest ();
        mLocationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval (10 * 1000);
        mLocationRequest.setFastestInterval (2 * 1000);

        mMapView = (MapView) view.findViewById (R.id.mapView);
        mMapView.onCreate (savedInstanceState);
        mMapView.onResume ();

        MapsInitializer.initialize (getActivity ().getApplicationContext ());

        mMapView.getMapAsync (new OnMapReadyCallback () {
            @Override
            public void onMapReady (GoogleMap googleMap) {
                mGoogleMap = googleMap;

                mGoogleMap.getUiSettings ().setZoomGesturesEnabled (true);
                mGoogleMap.getUiSettings ().setZoomControlsEnabled (true);
                mGoogleMap.getUiSettings ().setCompassEnabled (true);
                mGoogleMap.getUiSettings ().setMyLocationButtonEnabled (true);

                CameraPosition cameraPosition = new CameraPosition.Builder ()
                        .target (new LatLng (51.5072, 0.1275))
                        .zoom (9)
                        .build ();
                mGoogleMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));
            }
        });

    }

    @Override
    public void onPause () {
        super.onPause ();
        if (mGoogleApiClient.isConnected ())
            mGoogleApiClient.disconnect ();
        LocationServices.FusedLocationApi.removeLocationUpdates (mGoogleApiClient, this);
        mMapView.onPause ();
    }

    @Override
    public void onResume () {
        super.onResume ();
        if (mGoogleApiClient != null && ! mGoogleApiClient.isConnected ())
            mGoogleApiClient.connect ();

        mMapView.onResume ();
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        LocationServices.FusedLocationApi.removeLocationUpdates (mGoogleApiClient, this);
        mMapView.onDestroy ();

    }

    @Override
    public void onLowMemory () {
        super.onLowMemory ();

        mMapView.onLowMemory ();
    }

    @Override
    public void onConnected (Bundle bundle) {
        Log.i (TAG, "Location Services Connected");
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission (getContext (), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission (getContext (), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i (TAG, "Permissions don't already Exist");
            ActivityCompat.requestPermissions (getActivity (),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        } else {
            Log.i (TAG, "Permissions already Exist or Version is not Marsh mellow");
            Location location = LocationServices.FusedLocationApi.getLastLocation (mGoogleApiClient);
            if (location != null)
                showCurentLocation (location);
            LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            mGoogleMap.setMyLocationEnabled (true);
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
        Log.i (TAG, "New Location = " + location.toString ());
        showCurentLocation (location);

    }

    private static final int REQUEST_CODE_LOCATION = 2;

    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission (getContext (), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission (getContext (), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocation = LocationServices.FusedLocationApi.getLastLocation (mGoogleApiClient);
                if (mLocation != null)
                    showCurentLocation (mLocation);
                Log.i (TAG, "Permission received!");
                LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mLocationRequest, this);
            } else {
                Toast.makeText (getContext (), "Unable to get Location Permissions", Toast.LENGTH_LONG).show ();
            }
        }
    }

    private void showCurentLocation (Location mLocation) {
        CameraPosition cameraPosition = new CameraPosition.Builder ()
                .target (new LatLng (mLocation.getLatitude (), mLocation.getLongitude ()))
                .zoom (9)
                .build ();
        mGoogleMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));
    }

}
