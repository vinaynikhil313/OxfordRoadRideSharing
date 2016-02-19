package com.vinay.oxfordroadridesharing.login.register.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.vinay.oxfordroadridesharing.login.register.presenter.OnRegisterFinishedListener;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.UpdateFirebaseLogin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor,
        Firebase.ValueResultHandler<Map<String, Object>>,
        Firebase.AuthResultHandler{

    String email;
    String password;

    OnRegisterFinishedListener listener;

    Firebase firebase = new Firebase(Constants.FIREBASE_REF);

    @Override
    public void registerUser (String email, String password, OnRegisterFinishedListener listener) {
        firebase.createUser (email, password, this);
        this.email = email;
        this.password = password;
        this.listener = listener;
    }

    @Override
    public void onError (FirebaseError firebaseError) {
        switch (firebaseError.getCode ()) {
            case FirebaseError.EMAIL_TAKEN:
                //Toast.makeText (context, "Email Already exists", Toast.LENGTH_LONG).show ();
                listener.onFailure (FirebaseError.EMAIL_TAKEN);
                break;
        }
    }

    @Override
    public void onSuccess (Map<String, Object> stringObjectMap) {
        Log.i ("Authenticate Create", stringObjectMap.toString ());
        //Toast.makeText (context, "User created", Toast.LENGTH_SHORT).show ();
        Map<String, Object> map = new HashMap<String, Object> ();
        map.put ("displayName", "user");
        map.put ("email", email);
        map.put ("provider", "password");
        firebase.child ("users").child ((String) stringObjectMap.get ("uid")).setValue (map);
        firebase.authWithPassword (email, password, this);
    }

    @Override
    public void onAuthenticated (AuthData authData) {
        listener.onSuccess (authData.getUid (), authData.getToken ());
        UpdateFirebaseLogin.updateFirebase (authData);
    }

    @Override
    public void onAuthenticationError (FirebaseError firebaseError) {
        listener.onFailure(firebaseError.getCode ());
    }
}
