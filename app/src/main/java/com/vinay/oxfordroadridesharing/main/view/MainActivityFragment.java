package com.vinay.oxfordroadridesharing.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenter;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenterImpl;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainActivityFragmentView{

    private final String TAG = Utilities.getTag (this);

    private MainActivityFragmentPresenter presenter;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private MainActivity mMainActivity;

    Button ride;

    View view;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        view = inflater.inflate (R.layout.maps_fragment, container, false);

        presenter = new MainActivityFragmentPresenterImpl(this);

        mMainActivity = (MainActivity) getActivity ();

        setupGoogleMap (savedInstanceState);

        presenter.connectToGoogleApi (mMainActivity);

        ride = (Button) mMainActivity.findViewById (R.id.rideButton);

        ride.setOnClickListener (new View.OnClickListener () {

            int mRideMode;

            @Override
            public void onClick (View v) {
                //startAutoComplete ();

                final AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder (mMainActivity)
                        .setTitle ("Type of Ride")
                        .setSingleChoiceItems (new String[] {"Drive", "Share"}, 1, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                Log.i(TAG, "Selected item is " + which);
                                mRideMode = which;
                            }
                        })
                        .setPositiveButton ("Ride!", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                if(mRideMode == 0)
                                    startAutoComplete ();
                            }
                        })
                        .setNegativeButton ("Cancel", new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                Log.i (TAG, "Cancel Pressed");
                            }
                        })
                        .setCancelable (true);

                mAlertDialogBuilder.show ();

            }
        });

        return view;
    }

    private void setupGoogleMap (Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById (R.id.mapView);
        mMapView.onCreate (savedInstanceState);
        mMapView.onResume ();

        MapsInitializer.initialize (mMainActivity.getApplicationContext ());

        mMapView.getMapAsync (new OnMapReadyCallback () {

            @Override
            public void onMapReady (GoogleMap googleMap) {
                mGoogleMap = googleMap;

                mGoogleMap.getUiSettings ().setZoomGesturesEnabled (true);
                mGoogleMap.getUiSettings ().setZoomControlsEnabled (true);
                mGoogleMap.getUiSettings ().setCompassEnabled (true);
                mGoogleMap.getUiSettings ().setMyLocationButtonEnabled (true);

                CameraPosition cameraPosition = new CameraPosition.Builder ()
                        .target (new LatLng (Constants.LONDON_LAT, Constants.LONDON_LNG))
                        .zoom (9)
                        .build ();

                mGoogleMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));
            }
        });

    }

    @Override
    public void onPause () {
        super.onPause ();
        presenter.disconnectGoogleApi ();
        mMapView.onPause ();
    }

    @Override
    public void onResume () {
        super.onResume ();
        mMapView.onResume ();
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        presenter.disconnectGoogleApi ();
        mMapView.onDestroy ();

    }

    @Override
    public void onLowMemory () {
        super.onLowMemory ();
        mMapView.onLowMemory ();
    }

    private void showDestination (LatLng latLng, String place) {
        MarkerOptions marker = new MarkerOptions ().position (latLng);
        if (place != null)
            marker.title (place);
        else
            marker.title ("You are here");
        mGoogleMap.addMarker (marker);
        CameraPosition cameraPosition = new CameraPosition.Builder ()
                .target (latLng)
                .zoom (Constants.LOCATION_ZOOM_LEVEL-2)
                .build ();
        mGoogleMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    void startAutoComplete () {

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder (PlaceAutocomplete.MODE_OVERLAY)
                            .build (mMainActivity);
            startActivityForResult (intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.i (TAG, requestCode + " " + resultCode + " " + data.toString ());
        super.onActivityResult (requestCode, resultCode, data);

        Log.i (TAG, requestCode + " " + resultCode + " " + data.toString ());

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == mMainActivity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace (mMainActivity, data);
                Log.i (TAG, "Place: " + place.getName ());
                showDestination (place.getLatLng (), place.getName ().toString ());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus (mMainActivity, data);
                // TODO: Handle the error.
                Log.i (TAG, status.getStatusMessage ());

            } else if (resultCode == mMainActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            } else {
                Status status = PlaceAutocomplete.getStatus (mMainActivity, data);
                Log.i (TAG, status.getStatusMessage ());
            }
        }
    }

    @Override
    public void moveToLocation (LatLng latLng) {
        Log.i(TAG, latLng.toString ());

        String placeId = presenter.getPlaceId (latLng);

        CameraPosition cameraPosition = new CameraPosition (latLng, Constants.LOCATION_ZOOM_LEVEL, 0, 0);
        mGoogleMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));
        mGoogleMap.setMyLocationEnabled (true);
    }
}
