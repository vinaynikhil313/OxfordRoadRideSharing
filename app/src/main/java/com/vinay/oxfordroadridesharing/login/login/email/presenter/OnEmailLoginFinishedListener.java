package com.vinay.oxfordroadridesharing.login.login.email.presenter;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 21-01-2016.
 */
public interface OnEmailLoginFinishedListener {

    void onSuccess(User user);

    void onEmailError ();

    void onPasswordError ();

}
