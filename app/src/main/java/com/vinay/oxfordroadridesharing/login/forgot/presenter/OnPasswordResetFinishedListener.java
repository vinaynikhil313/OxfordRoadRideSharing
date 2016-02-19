package com.vinay.oxfordroadridesharing.login.forgot.presenter;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface OnPasswordResetFinishedListener {

    void onSuccess (int flag);

    void onFailure (String message);

}
