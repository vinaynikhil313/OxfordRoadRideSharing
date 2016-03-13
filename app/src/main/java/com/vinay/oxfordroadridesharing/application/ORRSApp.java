package com.vinay.oxfordroadridesharing.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vinay.oxfordroadridesharing.utils.ApiHelper;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 20-02-2016.
 */
public class ORRSApp extends Application {

	private ApiHelper mApiHelper;

	private static ORRSApp mInstance;

	private final String TAG = Utilities.getTag(this);

	@Override
	public void onCreate() {
		super.onCreate();

		mInstance = this;

		FacebookSdk.sdkInitialize(getApplicationContext());

		Firebase.setAndroidContext(this);
		Firebase.getDefaultConfig().setPersistenceEnabled(true);

		mApiHelper = new ApiHelper(getApplicationContext());
	}

	private static synchronized ORRSApp getAppInstance() {
		return mInstance;
	}

	private ApiHelper getApiHelperInstance() {
		return this.mApiHelper;
	}

	/*public static ApiHelper getApiHelper() {
		return getAppInstance().getApiHelperInstance();
	}*/

	public static GoogleApiClient getGoogleApiClient() {
		return getAppInstance().getApiHelperInstance().getGoogleApiClient();
	}

	public static Firebase getFirebaseInstance() {
		return getAppInstance().getApiHelperInstance().getFirebaseInstance();
	}

}
