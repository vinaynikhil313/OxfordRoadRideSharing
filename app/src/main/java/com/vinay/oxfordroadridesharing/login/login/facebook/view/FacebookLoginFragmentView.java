package com.vinay.oxfordroadridesharing.login.login.facebook.view;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 22-01-2016.
 */
public interface FacebookLoginFragmentView {

    void openMainPage ();

    void onError ();

    void writeToSharedPrefernces(User user);

    void showProgressDialog ();

    void hideProgressDialog ();

}
