package com.vinay.oxfordroadridesharing.main.presenter;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface OnResultGeneratedListener {

	void onLocationDetected(Place place);

	void onDirectionsGenerated(List<LatLng> points, LatLngBounds latLngBounds);

	void onRidesGenerated();

}
