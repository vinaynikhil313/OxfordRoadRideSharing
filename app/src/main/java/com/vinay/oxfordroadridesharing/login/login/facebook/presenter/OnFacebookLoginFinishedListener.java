package com.vinay.oxfordroadridesharing.login.login.facebook.presenter;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface OnFacebookLoginFinishedListener {

    void onFirebaseLoginSuccess (String uid, String token);

    void onFirebaseLoginFailure ();

}
