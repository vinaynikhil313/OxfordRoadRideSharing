package com.vinay.oxfordroadridesharing.main;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;
import com.vinay.oxfordroadridesharing.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MapView mapView;

    public MainActivityFragment () {
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.maps_fragment, container, false);
        mapView = (MapView) view.findViewById (R.id.mapView);
        mapView.setStyleUrl(Style.EMERALD);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng (41.885, -87.679)) // Sets the center of the map to Chicago
                .zoom(11)                            // Sets the zoom
                .build ();

        mapView.moveCamera(CameraUpdateFactory.newCameraPosition (cameraPosition));

        mapView.setZoomControlsEnabled (true);

        mapView.onCreate (savedInstanceState);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
