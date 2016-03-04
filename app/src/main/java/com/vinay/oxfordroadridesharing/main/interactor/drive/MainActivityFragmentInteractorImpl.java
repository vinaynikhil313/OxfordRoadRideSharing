package com.vinay.oxfordroadridesharing.main.interactor.drive;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vinay.oxfordroadridesharing.application.OxfordRoadRideSharingApplication;
import com.vinay.oxfordroadridesharing.main.presenter.OnResultGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.GoogleDirectionsApiHelper;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentInteractorImpl implements MainActivityFragmentInteractor,
		LocationListener,
		ActivityCompat.OnRequestPermissionsResultCallback {

	Firebase mFirebase = new Firebase(Constants.FIREBASE_REF);

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private LatLng mCurrentLatLng;

	private User mUser;

	private Ride mCurrentRide;
	private String mRideId = "R002";

	private Activity mActivity;

	private OnResultGeneratedListener listener;

	private final String TAG = Utilities.getTag(this);

	public MainActivityFragmentInteractorImpl() {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(10 * 1000);
		mLocationRequest.setFastestInterval(2 * 1000);

	}

	@Override
	public void establishConnection(Activity activity, OnResultGeneratedListener listener) {

		this.mActivity = activity;
		this.listener = listener;

		mGoogleApiClient = OxfordRoadRideSharingApplication.getGoogleApiHelper().getGoogleApiClient();

		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M
				&& ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.i(TAG, "Permissions don't already Exist");
			ActivityCompat.requestPermissions(mActivity,
					new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
					Constants.REQUEST_CODE_LOCATION);
		} else {
			Log.i(TAG, "Permissions already Exist or Version is not Marsh mellow");
			Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if(mLocation != null){
				//mCurrentLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
				//listener.onLocationDetected(mCurrentLatLng);
				onLocationChanged(mLocation);
			}
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
	}

	@Override
	public void requestLocationUpdates() {

		Log.i(TAG, "Request Location Updates is Connected = " + mGoogleApiClient.isConnected());

		if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			if(ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			}
		}
	}

	@Override
	public void disconnectConnection() {

		if(mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}
	}

	@Override
	public void fetchDirectionsFromApi(User user, String src, String dstn) {

		this.mUser = user;

		RequestParams mRequestParams = new RequestParams();
		mRequestParams.put(Constants.ORIGIN_TEXT, Constants.PLACE_ID_TEXT + src);
		mRequestParams.put(Constants.DESTINATION_TEXT, Constants.PLACE_ID_TEXT + dstn);
		mRequestParams.put(Constants.KEY_TEXT, Constants.KEY_VALUE);

		GoogleDirectionsApiHelper.get("directions", mRequestParams, responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			try {
				JSONObject mRoute = (JSONObject) response.getJSONArray("routes").get(0);
				JSONObject mNorthEast = mRoute.getJSONObject("bounds").getJSONObject("northeast");
				JSONObject mSouthWest = mRoute.getJSONObject("bounds").getJSONObject("southwest");

				List<LatLng> mBounds = new ArrayList<>();
				mBounds.add(new LatLng(mNorthEast.getDouble("lat"), mNorthEast.getDouble
						("lng")));

				mBounds.add(new LatLng(mSouthWest.getDouble("lat"), mSouthWest.getDouble
						("lng")));

				String mEncodedPoints = mRoute.getJSONObject("overview_polyline").getString
						("points");

				mCurrentRide = new Ride();
				mCurrentRide.setId(mRideId);
				mCurrentRide.setDriverUid(mUser.getUid());
				mCurrentRide.setActive(false);
				if(mCurrentLatLng != null) {
					mCurrentRide.setCurrentLocationLat(mCurrentLatLng.latitude);
					mCurrentRide.setCurrentLocationLng(mCurrentLatLng.longitude);
				}
				mCurrentRide.setRoute(mEncodedPoints);
				mCurrentRide.setBounds(PolyUtil.encode(mBounds));

				mUser.getRides().add(mRideId);

				mFirebase.child("rides").child(mRideId).setValue(mCurrentRide);
				mFirebase.child("users").child(mUser.getUid()).setValue(mUser);

				List<LatLng> mPoints = PolyUtil.decode(mEncodedPoints);
				Log.i(TAG, "Size of Points = " + mPoints.size());
				listener.onDirectionsGenerated(mPoints, mBounds);
			} catch(JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			Log.e(TAG, "Error in Directions API Call. Check the URL and permissions");
		}
	};

	@Override
	public void startRide() {
		Log.i(TAG, "Ride started in Interactor");
		if(mCurrentRide != null){
			mCurrentRide.setActive(true);
			mFirebase.child("rides").child(mRideId).setValue(mCurrentRide);
		}
	}

	@Override
	public void finishRide() {
		Log.i(TAG, "Ride finished in Interactor");
		if(mCurrentRide != null){
			mCurrentRide.setActive(false);
			mFirebase.child("rides").child(mRideId).setValue(mCurrentRide);
		}
	}

	@Override
	public void onLocationChanged(final Location location) {

		mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		Log.i(TAG, "in OnLocationChanged " + mCurrentLatLng.toString());
		listener.onLocationDetected(mCurrentLatLng);

		if(mCurrentRide != null && mCurrentRide.isActive()){
			mCurrentRide.setCurrentLocationLat(mCurrentLatLng.latitude);
			mCurrentRide.setCurrentLocationLng(mCurrentLatLng.longitude);
			mFirebase.child("rides").child(mCurrentRide.getId()).setValue(mCurrentRide);
		}

	}

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if(requestCode == Constants.REQUEST_CODE_LOCATION) {
			if(grantResults.length == 1
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				if(ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
						&& ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					return;
				}
				Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				mCurrentLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
				if(mLocation != null)
					onLocationChanged(mLocation);
				Log.i(TAG, "Permission received!");
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			} else {
				Toast.makeText(mActivity, "Unable to get Location Permissions", Toast.LENGTH_LONG).show();
			}
		}
	}

}
