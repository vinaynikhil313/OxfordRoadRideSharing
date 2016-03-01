package com.vinay.oxfordroadridesharing.main.view;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentView {

	void moveSourceLocation(Place place);

	void createDialog();

	void drawPath(List<LatLng> points, LatLngBounds latLngBounds);

	void showProgressDialog();

	void hideProgressDialog();

}
