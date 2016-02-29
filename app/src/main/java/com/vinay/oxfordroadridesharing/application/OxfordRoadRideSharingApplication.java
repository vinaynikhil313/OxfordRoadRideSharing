package com.vinay.oxfordroadridesharing.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.GoogleApiHelper;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 20-02-2016.
 */
public class OxfordRoadRideSharingApplication extends Application {

    private GoogleApiHelper mGoogleApiHelper;

    private static OxfordRoadRideSharingApplication mInstance;

    private final String TAG = Utilities.getTag (this);

    @Override
    public void onCreate () {
        super.onCreate ();

        mInstance = this;
        mGoogleApiHelper = new GoogleApiHelper(getApplicationContext ());

        FacebookSdk.sdkInitialize (getApplicationContext ());

        Firebase.setAndroidContext (this);
        Firebase.getDefaultConfig().setPersistenceEnabled (true);
        Firebase scoresRef = new Firebase(Constants.FIREBASE_REF);
        scoresRef.keepSynced (true);
    }

    private static synchronized OxfordRoadRideSharingApplication getInstance() {
        return mInstance;
    }

    private GoogleApiHelper getGoogleApiHelperInstance() {
        return this.mGoogleApiHelper;
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }

}
