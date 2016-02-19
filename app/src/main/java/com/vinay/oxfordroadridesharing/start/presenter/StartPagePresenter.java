package com.vinay.oxfordroadridesharing.start.presenter;

import com.facebook.FacebookSdk;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface StartPagePresenter extends FacebookSdk.InitializeCallback {

    void loginWithPassword (String accessToken);
}