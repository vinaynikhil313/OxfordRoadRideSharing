package com.vinay.oxfordroadridesharing.login.login.facebook.presenter;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.vinay.oxfordroadridesharing.login.login.facebook.interactor.FacebookLoginInteractor;
import com.vinay.oxfordroadridesharing.login.login.facebook.interactor.FacebookLoginInteractorImpl;
import com.vinay.oxfordroadridesharing.login.login.facebook.view.FacebookLoginFragment;
import com.vinay.oxfordroadridesharing.login.login.facebook.view.FacebookLoginFragmentView;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class FacebookLoginPresenterImpl implements FacebookLoginPresenter,
        OnFacebookLoginFinishedListener, FacebookCallback<LoginResult> {

    FacebookLoginFragmentView view;
    FacebookLoginInteractor interactor;

    private static final String TAG = FacebookLoginPresenterImpl.class.getSimpleName ();

    public FacebookLoginPresenterImpl(FacebookLoginFragment view){

        this.view = view;
        interactor = new FacebookLoginInteractorImpl ();
        Log.i(TAG, "FacebookPresenter created");

    }

    @Override
    public void onFirebaseLoginFailure () {
        view.hideProgressDialog ();
        view.onError ();
    }

    @Override
    public void onFirebaseLoginSuccess (String uid, String token) {
        Log.i(TAG, "Firebase Facebook Login successful");
        view.writeToSharedPrefernces (uid, token);
        view.hideProgressDialog ();
        view.openMainPage ();
    }

    @Override
    public void onCancel () {

    }

    @Override
    public void onError (FacebookException error) {

    }

    @Override
    public void onSuccess (LoginResult loginResult) {
        view.showProgressDialog ();
        AccessToken accessToken = loginResult.getAccessToken ();
        Log.i (TAG, accessToken.getToken ());
        AccessToken.setCurrentAccessToken (accessToken);
        if (accessToken != null) {
            Log.i (TAG, "Facebook Login Successful");
            interactor.requestData (this);
        }
    }
}
