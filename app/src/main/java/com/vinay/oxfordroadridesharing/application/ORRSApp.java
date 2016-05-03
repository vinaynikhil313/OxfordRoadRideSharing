package com.vinay.oxfordroadridesharing.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

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

		if(isNetworkAvailable())
			mApiHelper = new ApiHelper(getApplicationContext());

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private static synchronized ORRSApp getAppInstance() {
		return mInstance;
	}

	private ApiHelper getApiHelperInstance() {
		return this.mApiHelper;
	}

	public static GoogleApiClient getGoogleApiClient() {
		return getAppInstance().getApiHelperInstance().getGoogleApiClient();
	}

	public static Firebase getFirebaseInstance() {
		return getAppInstance().getApiHelperInstance().getFirebaseInstance();
	}

}
