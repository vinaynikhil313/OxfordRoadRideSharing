package com.vinay.oxfordroadridesharing.login.login.email.view;

/**
 * Created by Vinay Nikhil Pabba on 22-01-2016.
 */
public interface EmailLoginFragmentView {

    void emailError ();

    void passwordError ();

    void openMainPage ();

    void writeToSharedPreferences (String uid, String token);

    void showProgressDialog ();

    void hideProgressDialog ();

}