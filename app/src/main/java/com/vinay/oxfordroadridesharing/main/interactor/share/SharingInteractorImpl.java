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
import com.vinay.oxfordroadridesharing.application.OxfordRoadRideSharingApplication;
import com.vinay.oxfordroadridesharing.main.presenter.OnResultGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;
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
public class SharingInteractorImpl implements SharingInteractor, ValueEventListener,
		ResultCallback<PlaceBuffer> {

	private OnResultGeneratedListener listener;

	private Firebase mFirebase = new Firebase(Constants.FIREBASE_REF);

	private final String TAG = Utilities.getTag(this);

	private List<Ride> mCurrentRides = new ArrayList<>();
	private List<Ride> mMatchedRides = new ArrayList<>();

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
		places.release();
	}

	public void onDataChange(DataSnapshot dataSnapshot) {
		for(DataSnapshot rides : dataSnapshot.getChildren()) {
			Ride ride = rides.getValue(Ride.class);
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

		StringBuilder origins = new StringBuilder();
		StringBuilder destinations= new StringBuilder();
		origins.append(srcLatLng.latitude + "," + srcLatLng.longitude
				+ "|" + dstnLatLng.latitude + "," + dstnLatLng.longitude);
		Log.i(TAG, "Current Rides = " + mCurrentRides.size());
		for(int i=0;i<mCurrentRides.size();i++){
			LatLng mLatLng;// = mCurrentRides.get(i).getCurrentLocation();
			destinations.append(mCurrentRides.get(i).getCurrentLocationLat() + "," +
					mCurrentRides.get(i).getCurrentLocationLng()  + "|");
			List<LatLng> mRoute = PolyUtil.decode(mCurrentRides.get(i).getRoute());
			mLatLng = mRoute.get(mRoute.size()-1);
			destinations.append(mLatLng.latitude + "," + mLatLng.longitude + "|");
		}
		destinations.deleteCharAt(destinations.length() - 1);
		Log.i(TAG, "Origins = " + origins);
		Log.i(TAG, "Destns = " + destinations);

		RequestParams mRequestParams = new RequestParams();
		mRequestParams.put(Constants.ORIGINS_TEXT, origins);
		mRequestParams.put(Constants.DESTINATIONS_TEXT, destinations);
		mRequestParams.put(Constants.DEPARTURE_TIME_TEXT, "now");
		mRequestParams.put(Constants.TRAFFIC_TEXT, "pessimistic");
		mRequestParams.put(Constants.KEY_TEXT, Constants.KEY_VALUE);

		GoogleDirectionsApiHelper.get(Constants.DISTANCE_API, mRequestParams, responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			try {
				//Log.i(TAG, response.toString(5));
				JSONArray rows = response.getJSONArray("rows");
				JSONArray mCurrentLocationElements = rows.getJSONObject(0).getJSONArray("elements");
				JSONArray mDestinationElements = rows.getJSONObject(1).getJSONArray("elements");
				for(int i=0;i<mCurrentLocationElements.length();i += 2){
					JSONObject mCurrentLocationDuration = mCurrentLocationElements.getJSONObject
							(i).getJSONObject("duration");
					JSONObject mDestinationDuration = mDestinationElements.getJSONObject
							(i+1).getJSONObject("duration");
					if(mCurrentLocationDuration.getInt("value") < 600 &&
							mDestinationDuration.getInt("value") < 600){
						Log.i(TAG, "MATCH FOUND!!!!");
						Log.i(TAG, mCurrentRides.get(i/2).getId());
						mMatchedRides.add(mCurrentRides.get(i / 2));
					}
				}
				listener.onRidesGenerated(mMatchedRides);
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			Log.e(TAG, "Error in fetching distances");
		}
	};

}
