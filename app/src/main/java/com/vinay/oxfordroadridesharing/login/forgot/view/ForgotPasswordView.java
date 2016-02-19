package com.vinay.oxfordroadridesharing.login.forgot.view;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface ForgotPasswordView {

    void showProgressDialog (String message);

    void hideProgressDialog ();

    void showMessage (String message);

    void showChangePasswordDialog ();

    void hideChangePasswordDialog ();

    void openLoginPage ();

}
