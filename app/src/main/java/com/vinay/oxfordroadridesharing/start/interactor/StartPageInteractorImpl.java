package com.vinay.oxfordroadridesharing.start.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.vinay.oxfordroadridesharing.start.presenter.OnTokenLoginFinishedListener;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.UpdateFirebaseLogin;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public class StartPageInteractorImpl implements StartPageInteractor, Firebase.AuthResultHandler{

    Firebase firebase = new Firebase(Constants.FIREBASE_REF);

    OnTokenLoginFinishedListener listener;

    String provider;

    String TAG = Utilities.getTag (this);

    @Override
    public void loginWithToken(String provider, String accessToken, OnTokenLoginFinishedListener listener){
        this.listener = listener;
        this.provider = provider;
        if(provider.equals (Constants.PROVIDER_FACEBOOK))
            firebase.authWithOAuthToken (provider, accessToken, this);
        else
            firebase.authWithCustomToken (accessToken, this);
    }

    @Override
    public void onAuthenticated (AuthData authData) {
        UpdateFirebaseLogin.updateFirebase (authData);
        listener.onLoginSuccessful (provider, authData.getUid (), authData.getToken ());
    }

    @Override
    public void onAuthenticationError (FirebaseError firebaseError) {
        Log.e(TAG, firebaseError.getMessage ());
        listener.onLoginUnsuccessful ();
    }
}
