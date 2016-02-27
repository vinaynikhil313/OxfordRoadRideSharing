package com.vinay.oxfordroadridesharing.main.interactor;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.main.presenter.OnConnectionEstablishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentInteractor {

    void establishConnection(Activity activity, OnConnectionEstablishedListener listener);

    void disconnectConnection();

    String getPlaceId(LatLng latlng);

}
