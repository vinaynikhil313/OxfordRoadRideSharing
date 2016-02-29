package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentPresenter {

    void connectToGoogleApi(Activity activity);

    void requestLocationUpdates();

    void disconnectGoogleApi();

    void chooseDestination(Fragment fragment);

    void receivedDestination(int resultCode, Intent data);

}
