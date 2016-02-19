package com.vinay.oxfordroadridesharing.login.register.presenter;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface OnRegisterFinishedListener {

    void onSuccess (String uid, String token);

    void onFailure (int errorCode);

}
