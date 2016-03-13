package com.vinay.oxfordroadridesharing.main.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface OnResultGeneratedListener {

	void onLocationDetected(LatLng latLng);

	void onDirectionsGenerated(List<LatLng> points, List<LatLng> latLngBounds);

	void onRidesGenerated(ArrayList<Ride> matchedRides);

	void onDriversListGenerated(List<Ride> matchedRides, List<User> driversList);

}
