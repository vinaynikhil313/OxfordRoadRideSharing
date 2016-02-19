package com.vinay.oxfordroadridesharing.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.vinay.oxfordroadridesharing.utils.Constants;

/**
 * Created by Vinay Nikhil Pabba on 20-02-2016.
 */
public class OxfordRoadRideSharingApplication extends Application {

    @Override
    public void onCreate () {
        super.onCreate ();
        FacebookSdk.sdkInitialize (getApplicationContext ());
        Firebase.setAndroidContext (this);
        Firebase.getDefaultConfig().setPersistenceEnabled (true);
        Firebase scoresRef = new Firebase(Constants.FIREBASE_REF);
        scoresRef.keepSynced (true);
    }

}
