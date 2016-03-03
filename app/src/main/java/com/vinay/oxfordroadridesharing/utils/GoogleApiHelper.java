package com.vinay.oxfordroadridesharing.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by Vinay Nikhil Pabba on 29-02-2016.
 */
public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private static final String TAG = GoogleApiHelper.class.getSimpleName();

	GoogleApiClient mGoogleApiClient;

	Context mContext;

	public GoogleApiHelper(Context context) {
		this.mContext = context;
		buildGoogleApiClient();
		connect();
	}

	public GoogleApiClient getGoogleApiClient() {
		return this.mGoogleApiClient;
	}

	public void connect() {
		if(mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	public void disconnect() {
		if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	public boolean isConnected() {
		if(mGoogleApiClient != null) {
			return mGoogleApiClient.isConnected();
		} else {
			return false;
		}
	}

	private void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(mContext)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.addApi(Places.GEO_DATA_API)
				.addApi(Places.PLACE_DETECTION_API)
				.build();

	}

	@Override
	public void onConnected(Bundle bundle) {
		//You are connected do what ever you want
		//Like i get last known location
		Log.i(TAG, "Location Services Connected");
		//Location location = LocationServices.FusedLocationApi.getLastLocation (mGoogleApiClient);
		//Log.i(TAG, location.toString ());
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
	}

}