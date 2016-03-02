package com.vinay.oxfordroadridesharing.main.interactor;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;
import com.vinay.oxfordroadridesharing.application.OxfordRoadRideSharingApplication;
import com.vinay.oxfordroadridesharing.main.presenter.OnResultGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 03-03-2016.
 */
public class SharingInteractorImpl implements SharingInteractor, ValueEventListener,
		ResultCallback<PlaceBuffer> {

	private OnResultGeneratedListener listener;

	private Firebase mFirebase = new Firebase(Constants.FIREBASE_REF);

	private final String TAG = Utilities.getTag(this);

	private List<Ride> mCurrentRides = new ArrayList<>();

	private LatLng srcLatLng, dstnLatLng;

	private GoogleApiClient mGoogleApiClient = OxfordRoadRideSharingApplication
			.getGoogleApiHelper().getGoogleApiClient();

	public SharingInteractorImpl() {
	}

	@Override
	public void getRides(String src, String dstn, OnResultGeneratedListener listener) {
		this.listener = listener;
		Log.i(TAG, "Is connected - " + mGoogleApiClient.isConnected());
		Places.GeoDataApi.getPlaceById(mGoogleApiClient, src,
				dstn).setResultCallback(this);
	}

	@Override
	public void onResult(PlaceBuffer places) {
		srcLatLng = places.get(0).getLatLng();
		dstnLatLng = places.get(1).getLatLng();
		Log.i(TAG, "Places are - " + srcLatLng + " -> " + dstnLatLng);
		mFirebase.child("rides").addListenerForSingleValueEvent(this);
	}

	public void onDataChange(DataSnapshot dataSnapshot) {
		for(DataSnapshot child : dataSnapshot.getChildren()) {
			Ride ride = child.getValue(Ride.class);
			if(ride.isActive())
				mCurrentRides.add(ride);
		}
		getRides();
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {
		Log.e(TAG, "Firebase Value event listener error");
	}

	private void getRides(){
		RequestParams mRequestParams = new RequestParams();
		mRequestParams.put(Constants.ORIGINS_TEXT, "");
		mRequestParams.put(Constants.DESTINATIONS_TEXT, "");
	}
}
