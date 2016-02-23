package com.vinay.oxfordroadridesharing.login.login.email.view;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 22-01-2016.
 */
public interface EmailLoginFragmentView {

    void emailError ();

    void passwordError ();

    void openMainPage ();

    void writeToSharedPreferences(User user);

    void showProgressDialog ();

    void hideProgressDialog ();

}