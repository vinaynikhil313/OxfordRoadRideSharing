package com.vinay.oxfordroadridesharing.main.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenter;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenterImpl;
import com.vinay.oxfordroadridesharing.rides.view.RidesListActivity;
import com.vinay.oxfordroadridesharing.src_dstn.view.SrcDstnActivity;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainActivityFragmentView,
		OnExternalButtonClickedListener {

	public static final int MODE_DRIVE = 0;
	public static final int MODE_SHARE = 1;
	private final String[] MODES = {"Drive", "Share"};
	private int mRideMode = - 1;

	private final String TAG = Utilities.getTag(this);

	private MainActivityFragmentPresenter presenter;

	private MapView mMapView;
	private GoogleMap mGoogleMap;
	private LatLng mCurrentLocation;
	private List<LatLng> mDirections;

	private Activity mActivity;

	private Button mRideButton;

	private FragmentManager mFragmentManager;
	private ButtonsFragment mButtonsFragment;

	private SharedPreferences mSharedPreferences;

	private Map<Marker, User> mMarkersDataMap;

	View view;

	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.maps_fragment, container, false);

		presenter = new MainActivityFragmentPresenterImpl(this);

		mActivity = getActivity();

		setupGoogleMap(savedInstanceState);

		presenter.connectToGoogleApi(mActivity);

		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Please Wait..");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);

		mRideButton = (Button) mActivity.findViewById(R.id.rideButton);

		mRideButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*final Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?" + "saddr="
								+ 51.5072 + "," + -0.13 + "&daddr="
								+ 51.5172 + "," + -0.14));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);*/
				createDialog();
			}
		});

		mSharedPreferences = mActivity.getSharedPreferences(Constants.MY_PREF, Context
				.MODE_PRIVATE);
		mFragmentManager = getFragmentManager();

		mMarkersDataMap = new HashMap<>();

		return view;
	}

	private void setupGoogleMap(Bundle savedInstanceState) {

		mMapView = (MapView) view.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();

		MapsInitializer.initialize(mActivity.getApplicationContext());

		mMapView.getMapAsync(new OnMapReadyCallback() {

			@Override
			public void onMapReady(GoogleMap googleMap) {
				mGoogleMap = googleMap;

				mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
				mGoogleMap.getUiSettings().setCompassEnabled(true);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(Constants.LONDON_LAT, Constants.LONDON_LNG))
						.zoom(9)
						.build();

				mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		presenter.requestLocationUpdates();
		mMapView.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		presenter.disconnectGoogleApi();
		mMapView.onDestroy();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null)
			return;
		Log.i(TAG, requestCode + " " + resultCode + " " + data.toString());
		showProgressDialog();

		if(requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			Bundle mBundle = data.getExtras();
			String mSrcId = mBundle.getString("src");
			String mDstnId = mBundle.getString("dstn");
			if(mSrcId == null || mSrcId.isEmpty())
				mSrcId = Constants.YOUR_LOCATION;
			Log.i(TAG, mSrcId + " -> " + mDstnId);
			mRideButton.setVisibility(View.GONE);
			presenter.getDirections(getUser(), mSrcId, mDstnId);

			if(mRideMode == MODE_DRIVE) {
				/*mRideButton.setVisibility(View.GONE);
				presenter.getDirections(getUser(), mSrcId, mDstnId);*/
				mButtonsFragment = new ButtonsFragment(this);
				mFragmentManager.beginTransaction().replace(R.id.placeholderLayout, mButtonsFragment).commit();
			} else if(mRideMode == MODE_SHARE) {
				presenter.getRides(mSrcId, mDstnId);
			}
		} else if(requestCode == Constants.NAVIGATION_REQUEST_CODE) {
			Log.i(TAG, "Returned from Navigation to Main Page");
		}

	}

	@Override
	public void moveSourceLocation(LatLng latLng) {

		if(mGoogleMap == null)
			return;

		mCurrentLocation = latLng;

		if(ActivityCompat.checkSelfPermission(mActivity, Manifest.permission
				.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mActivity,
				Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			mGoogleMap.setMyLocationEnabled(true);

		}

		CameraPosition cameraPosition;

		if(mRideMode == - 1)
			cameraPosition = new CameraPosition(latLng, Constants.LOCATION_ZOOM_LEVEL, 0, 0);
		else
			cameraPosition = new CameraPosition(latLng, 0, 0, 0);
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void createDialog() {

		final AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mActivity)
				.setTitle("Type of Ride")
				.setSingleChoiceItems(MODES, - 1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Selected item is " + which);
						mRideMode = which;
					}
				})
				.setPositiveButton("Ride!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(mRideMode != - 1)
							startActivityForResult(new Intent(getActivity(), SrcDstnActivity.class), Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setCancelable(false);

		mAlertDialogBuilder.show();
	}

	@Override
	public void drawPath(List<LatLng> points, List<LatLng> latLngBounds) {

		Log.i(TAG, "Points Size = " + points.size());
		mDirections = points;

		mGoogleMap.clear();

		mGoogleMap.addMarker(new MarkerOptions()
						.position(points.get(0))
						.title("Source")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2_24x40))
		);
		mGoogleMap.addMarker(new MarkerOptions()
						.position(points.get(points.size() - 1))
						.title("Destination")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1_40x32))
		);

		PolylineOptions polyline = new PolylineOptions()
				.width(10)
				.color(Color.BLUE);

		for(int i = 0; i < points.size(); i++) {
			polyline.add(points.get(i));
		}
		LatLngBounds.Builder mBoundsBuilder = new LatLngBounds.Builder();
		mBoundsBuilder.include(latLngBounds.get(0));
		mBoundsBuilder.include(latLngBounds.get(1));
		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBoundsBuilder.build(), 100));
		mGoogleMap.addPolyline(polyline);

		hideProgressDialog();
	}

	@Override
	public void openRidesList(ArrayList<Ride> matchedRides) {
		Intent intent = new Intent(getContext(), RidesListActivity.class);
		intent.putExtra("ridesList", matchedRides);
		startActivity(intent);
	}

	@Override
	public void showDrivers(List<Ride> matchedRides, List<User> driversList) {
		for(int i = 0; i < driversList.size(); i++) {

			Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(
					new LatLng(matchedRides.get(i).getCurrentLocationLat(), matchedRides.get(i).getCurrentLocationLng
							())));
			mMarkersDataMap.put(marker, driversList.get(i));

			mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					User mDriver = mMarkersDataMap.get(marker);
					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
							.setTitle(mDriver.getDisplayName())
							.setMessage("You can call the driver at 9999999999");
					builder.show();
					return false;
				}
			});
		}
	}

	@Override
	public void showProgressDialog() {
		mProgressDialog.show();
	}

	@Override
	public void hideProgressDialog() {
		mProgressDialog.hide();
	}

	@Override
	public void startDrive() {
		Log.i(TAG, "Drive started");
		presenter.startRide();
		final Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?" + "saddr="
						+ mDirections.get(0).latitude + "," + mDirections.get(0).longitude
						+ "&daddr=" + mDirections.get(mDirections.size() - 1).latitude + "," + mDirections.get(mDirections.size() - 1)
						.longitude));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivityForResult(intent, Constants.NAVIGATION_REQUEST_CODE);
	}

	@Override
	public void finishDrive() {
		presenter.finishRide();
		mFragmentManager.beginTransaction().remove(mButtonsFragment).commit();
		mGoogleMap.clear();
		mRideButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void cancelDrive() {
		Log.i(TAG, "Drive cancelled");
		mFragmentManager.beginTransaction().remove(mButtonsFragment).commit();
		mGoogleMap.clear();
		mRideButton.setVisibility(View.VISIBLE);
	}

	private User getUser() {
		String mJSON = mSharedPreferences.getString("user", "");
		if(mJSON == null || mJSON.equals(""))
			return null;
		else {
			Gson gson = new Gson();
			return gson.fromJson(mJSON, User.class);
		}
	}

}
