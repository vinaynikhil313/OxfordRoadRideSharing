package com.vinay.oxfordroadridesharing.login.login.facebook.interactor;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vinay.oxfordroadridesharing.login.login.facebook.presenter.OnFacebookLoginFinishedListener;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.UpdateFirebaseLogin;

import org.json.JSONObject;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class FacebookLoginInteractorImpl implements
        FacebookLoginInteractor, Firebase.AuthResultHandler, ValueEventListener{

    Firebase firebase = new Firebase(Constants.FIREBASE_REF);
    OnFacebookLoginFinishedListener listener;

    @Override
    public void requestData (OnFacebookLoginFinishedListener listener) {
        //Log.i(TAG + " inside requestData ", AccessToken.getCurrentAccessToken ().getToken ());
        this.listener = listener;
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Log.i("JSON ", json.toString ());

                        if (json != null) {
                            firebase.authWithOAuthToken ("facebook", AccessToken.getCurrentAccessToken ().getToken (), FacebookLoginInteractorImpl.this);
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString ("fields", "id,name,link,email,picture");
        request.setParameters (parameters);
        request.executeAsync ();
    }

    @Override
    public void onAuthenticated (AuthData authData) {
        //listener.onFirebaseLoginSuccess (user);
        UpdateFirebaseLogin.updateFirebase (authData);

        firebase.child ("users").child (authData.getUid ()).addListenerForSingleValueEvent (this);
    }

    @Override
    public void onAuthenticationError (FirebaseError firebaseError) {
        listener.onFirebaseLoginFailure ();
    }

    @Override
    public void onCancelled (FirebaseError firebaseError) {

    }

    @Override
    public void onDataChange (DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue (User.class);
        Log.i("FACEBOOK INTERACTOR", "UID = " + user.getUid ());
        listener.onFirebaseLoginSuccess (user);
    }
}
