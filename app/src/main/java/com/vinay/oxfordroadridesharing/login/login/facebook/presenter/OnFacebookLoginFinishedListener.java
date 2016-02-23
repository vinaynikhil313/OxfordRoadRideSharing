package com.vinay.oxfordroadridesharing.login.login.facebook.presenter;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface OnFacebookLoginFinishedListener {

    void onFirebaseLoginSuccess(User user);

    void onFirebaseLoginFailure ();

}
