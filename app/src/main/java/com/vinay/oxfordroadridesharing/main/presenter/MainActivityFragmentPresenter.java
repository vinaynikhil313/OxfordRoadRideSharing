package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentPresenter {

	void connectToGoogleApi(Activity activity);

	void requestLocationUpdates();

	void disconnectGoogleApi();

	void getDirections(User user, String src, String dstn);

	void getRides(String src, String dstn);

	void getRides(LatLng src, String dstn);

	void startDrive();

	void finishDrive();

}
