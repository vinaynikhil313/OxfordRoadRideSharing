package com.vinay.oxfordroadridesharing.main.interactor.share;

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
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vinay.oxfordroadridesharing.application.ORRSApp;
import com.vinay.oxfordroadridesharing.main.presenter.OnResultGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.GoogleDirectionsApiHelper;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinay Nikhil Pabba on 03-03-2016.
 */
public class SharingInteractorImpl implements SharingInteractor,
		ResultCallback<PlaceBuffer> {

	private OnResultGeneratedListener listener;

	private Firebase mFirebase = new Firebase(Constants.FIREBASE_REF);

	private static final int RIDES = 0;
	private static final int DRIVERS = 1;

	private final String TAG = Utilities.getTag(this);

	private List<Ride> mCurrentRides = new ArrayList<>();
	private List<Ride> mMatchedRides = new ArrayList<>();
	private List<User> mDriversList = new ArrayList<>();

	private LatLng srcLatLng, dstnLatLng;

	private GoogleApiClient mGoogleApiClient = ORRSApp.getGoogleApiClient();

	@Override
	public void getRides(String src, String dstn, OnResultGeneratedListener listener) {
		this.listener = listener;
		Log.i(TAG, "Is connected - " + mGoogleApiClient.isConnected());
		Places.GeoDataApi.getPlaceById(mGoogleApiClient, dstn,
				src).setResultCallback(this);
	}

	@Override
	public void getRides(LatLng src, String dstn, OnResultGeneratedListener listener) {
		this.listener = listener;
		srcLatLng = src;
		Log.i(TAG, "Src = " + srcLatLng.latitude + ", " + srcLatLng.longitude);
		Log.i(TAG, "Is connected - " + mGoogleApiClient.isConnected());
		Places.GeoDataApi.getPlaceById(mGoogleApiClient, dstn).setResultCallback(this);
	}

	@Override
	public void onResult(PlaceBuffer places) {
		dstnLatLng = places.get(0).getLatLng();
		if(places.getCount() > 1)
			srcLatLng = places.get(1).getLatLng();
		Log.i(TAG, "Places are - " + srcLatLng + " -> " + dstnLatLng);
		mFirebase.child("rides").addListenerForSingleValueEvent(new FirebaseValueEventListener(RIDES));
		places.release();
	}

	private void getRides() {

		StringBuilder origins = new StringBuilder();
		StringBuilder destinations = new StringBuilder();
		origins.append(srcLatLng.latitude + "," + srcLatLng.longitude
				+ "|" + dstnLatLng.latitude + "," + dstnLatLng.longitude);
		Log.i(TAG, "Current Rides = " + mCurrentRides.size());
		for(int i = 0; i < mCurrentRides.size(); i++) {
			LatLng mLatLng;
			destinations.append(mCurrentRides.get(i).getCurrentLocationLat() + "," +
					mCurrentRides.get(i).getCurrentLocationLng() + "|");
			List<LatLng> mRoute = PolyUtil.decode(mCurrentRides.get(i).getRoute());
			mLatLng = mRoute.get(mRoute.size() - 1);
			destinations.append(mLatLng.latitude + "," + mLatLng.longitude + "|");
		}
		destinations.deleteCharAt(destinations.length() - 1);
		Log.i(TAG, "Origins = " + origins);
		Log.i(TAG, "Destns = " + destinations);

		RequestParams mRequestParams = new RequestParams();
		mRequestParams.put(Constants.ORIGINS_TEXT, origins);
		mRequestParams.put(Constants.DESTINATIONS_TEXT, destinations);

		GoogleDirectionsApiHelper.get("distances", mRequestParams, responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			try {
				//Log.i(TAG, response.toString(5));
				JSONArray rows = response.getJSONArray("rows");
				JSONArray mCurrentLocationElements = rows.getJSONObject(0).getJSONArray("elements");
				JSONArray mDestinationElements = rows.getJSONObject(1).getJSONArray("elements");
				Log.i(TAG, "Size of Current Location elements = " + mCurrentLocationElements.length());
				Log.i(TAG, "Size of Destination Location elements = " + mDestinationElements.length());
				for(int i = 0; i < mCurrentLocationElements.length(); i += 2) {
					Log.i(TAG, "i = " + i);
					if(! mCurrentLocationElements.getJSONObject(i).getString("status").equals("OK")
							|| ! mDestinationElements.getJSONObject(i + 1).getString("status").equals("OK"))
						continue;
					JSONObject mCurrentLocationDuration = mCurrentLocationElements.getJSONObject
							(i).getJSONObject("duration");
					JSONObject mDestinationDuration = mDestinationElements.getJSONObject
							(i + 1).getJSONObject("duration");
					if(mCurrentLocationDuration.getInt("value") < 600 &&
							mDestinationDuration.getInt("value") < 900) {
						Log.i(TAG, "MATCH FOUND!!!!");
						Log.i(TAG, mCurrentRides.get(i / 2).getId());
						mMatchedRides.add(mCurrentRides.get(i / 2));
					}
				}
				Log.i(TAG, "Matched Rides Size = " + mMatchedRides.size());
				for(int i = 0; i < mMatchedRides.size(); i++) {
					mFirebase.child("users").child(mMatchedRides.get(i).getDriverUid())
							.addListenerForSingleValueEvent(new FirebaseValueEventListener(DRIVERS));
				}
				//listener.onRidesGenerated(mMatchedRides);
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			Log.e(TAG, "Error in fetching distances");
		}
	};

	private class FirebaseValueEventListener implements ValueEventListener {

		private int mode;

		public FirebaseValueEventListener(int mode) {
			this.mode = mode;
		}

		@Override
		public void onDataChange(DataSnapshot dataSnapshot) {
			if(mode == RIDES) {
				for(DataSnapshot rides : dataSnapshot.getChildren()) {
					Ride ride = rides.getValue(Ride.class);
					if(ride.isActive())
						mCurrentRides.add(ride);
				}
				getRides();
			} else {
				mDriversList.add(dataSnapshot.getValue(User.class));
				Log.i(TAG, "Drivers List Size = " + mDriversList.size());
				if(mDriversList.size() == mMatchedRides.size()) {
					listener.onDriversListGenerated(mMatchedRides, mDriversList);
				}
			}
		}

		@Override
		public void onCancelled(FirebaseError firebaseError) {
			Log.e(TAG, "Error in Firebase");
		}
	}

}
