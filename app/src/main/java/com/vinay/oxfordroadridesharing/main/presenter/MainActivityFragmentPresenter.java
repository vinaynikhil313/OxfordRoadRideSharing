package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentPresenter {

    void connectToGoogleApi(Activity activity);

    void disconnectGoogleApi();

    String getPlaceId(LatLng latLng);

}
