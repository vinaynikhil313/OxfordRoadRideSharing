package com.vinay.oxfordroadridesharing.main.interactor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.vinay.oxfordroadridesharing.main.presenter.OnConnectionEstablishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentInteractor {

    void establishConnection(Activity activity, OnConnectionEstablishedListener listener);

    void requestLocationUpdates();

    void disconnectConnection();

    void showPlaceAutoComplete(Fragment fragment);

    void destinationReceived(int resultCode, Intent data);

}
