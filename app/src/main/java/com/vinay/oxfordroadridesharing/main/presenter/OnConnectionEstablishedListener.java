package com.vinay.oxfordroadridesharing.main.presenter;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface OnConnectionEstablishedListener {

    void onLocationDetected(LatLng latLng);

}
