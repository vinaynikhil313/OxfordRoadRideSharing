package com.vinay.oxfordroadridesharing.main.presenter;

import com.google.android.gms.location.places.Place;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface OnConnectionEstablishedListener {

    void onLocationDetected(Place place);

    void onDestinationReceived(Place place);

}
