package com.vinay.oxfordroadridesharing.login.register.interactor;


import com.vinay.oxfordroadridesharing.login.register.presenter.OnRegisterFinishedListener;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface RegisterInteractor {

    void registerUser (String email, String password, String phoneNo, OnRegisterFinishedListener listener);

}
