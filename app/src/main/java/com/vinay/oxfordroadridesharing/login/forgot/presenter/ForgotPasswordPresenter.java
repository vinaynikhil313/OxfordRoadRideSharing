package com.vinay.oxfordroadridesharing.login.forgot.presenter;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface ForgotPasswordPresenter {

    void resetPassword (String email);

    void changePassword (String email, String oldPassword, String newPassword);

}
