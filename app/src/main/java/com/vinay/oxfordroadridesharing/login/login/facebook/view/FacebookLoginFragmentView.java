package com.vinay.oxfordroadridesharing.login.login.facebook.view;

/**
 * Created by Vinay Nikhil Pabba on 22-01-2016.
 */
public interface FacebookLoginFragmentView {

    void openMainPage ();

    void onError ();

    void writeToSharedPrefernces (String uid, String token);

    void showProgressDialog ();

    void hideProgressDialog ();

}
