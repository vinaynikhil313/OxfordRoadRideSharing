package com.vinay.oxfordroadridesharing.login.login.facebook.interactor;

import com.vinay.oxfordroadridesharing.login.login.facebook.presenter.OnFacebookLoginFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface FacebookLoginInteractor {

    void requestData (OnFacebookLoginFinishedListener listener);

}
