package com.vinay.oxfordroadridesharing.main.view;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentView {

	void moveSourceLocation(LatLng latLng);

	void createDialog();

	void drawPath(List<LatLng> points, List<LatLng> latLngBounds);

	void showProgressDialog();

	void hideProgressDialog();

}
