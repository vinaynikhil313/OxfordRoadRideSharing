package com.vinay.oxfordroadridesharing.login.register.view;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterActivityView {

    void openHomePage ();

    void registrationError (String message);

    void showProgressBar ();

    void hideProgressBar ();

    void writeToSharedPreferences (String uid, String token);

}
