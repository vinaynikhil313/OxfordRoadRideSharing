package com.vinay.oxfordroadridesharing.start.presenter;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface OnTokenLoginFinishedListener {

    void onLoginSuccessful (String provider, String uid, String accessToken);

    void onLoginUnsuccessful ();

}
