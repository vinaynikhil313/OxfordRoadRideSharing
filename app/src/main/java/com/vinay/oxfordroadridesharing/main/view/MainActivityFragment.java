package com.vinay.oxfordroadridesharing.main.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenter;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenterImpl;
import com.vinay.oxfordroadridesharing.src_dstn.view.SrcDstnActivity;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainActivityFragmentView, OnPointsGeneratedListener {

	public static final int MODE_DRIVE = 0;

	public static final int MODE_SHARE = 1;

	private final String TAG = Utilities.getTag(this);

	private MainActivityFragmentPresenter presenter;

	private MapView mMapView;
	private GoogleMap mGoogleMap;

	private Activity mActivity;

	Button ride;

	View view;

	private int mRideMode = 100;

	private final String[] MODES = {"Drive", "Share"};

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
		mProgressDialog.setIndeterminate(true);

		ride = (Button) mActivity.findViewById(R.id.rideButton);

		ride.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog();
			}
		});

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

				LatLngBounds latLngBounds = mGoogleMap.getProjection().getVisibleRegion()
						.latLngBounds;

				Log.i(TAG, "London Bounds = " + latLngBounds.toString());

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
		Log.i(TAG, requestCode + " " + resultCode + " " + data.toString());

		if(requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			Bundle mBundle = data.getExtras();
			String mSrcId = mBundle.getString("src");
			String mDstnId = mBundle.getString("dstn");

			Log.i(TAG, mBundle.get("src").toString() + " " + mBundle.get("dstn").toString());
			if(mRideMode == MODE_DRIVE)
				presenter.getDirections(mSrcId, mDstnId);
			else if (mRideMode == MODE_SHARE)
				presenter.getRides(mSrcId, mDstnId);
		}
	}

	@Override
	public void moveSourceLocation(Place place) {

		LatLng mLatLng = place.getLatLng();

		Log.i(TAG, place.getLatLng().toString() + " " + place.toString());

		MarkerOptions marker = new MarkerOptions()
				.position(mLatLng)
				.title("You are here")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1_40x32));

		mGoogleMap.addMarker(marker);

		CameraPosition cameraPosition = new CameraPosition(mLatLng, Constants.LOCATION_ZOOM_LEVEL, 0, 0);
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void createDialog() {

		final AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mActivity)
				.setTitle("Type of Ride")
				.setSingleChoiceItems(MODES, 1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Selected item is " + which);
						mRideMode = which;
					}
				})
				.setPositiveButton("Ride!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
	public void drawPath(List<LatLng> points, LatLngBounds latLngBounds) {

		Log.i(TAG, "Size = " + points.size());

		mGoogleMap.clear();

		mGoogleMap.addMarker(new MarkerOptions()
						.position(points.get(0))
						.title("Source")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1_40x32))
		);
		mGoogleMap.addMarker(new MarkerOptions()
						.position(points.get(points.size()-1))
						.title("Destination")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2_24x40))
		);

		PolylineOptions polyline = new PolylineOptions()
				.width(10)
				.color(Color.RED);

		for(int i = 0; i < points.size(); i++) {
			polyline.add(points.get(i));
		}
		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
		mGoogleMap.addPolyline(polyline);
	}

	@Override
	public void showProgressDialog() {
		mProgressDialog.show();
	}

	@Override
	public void hideProgressDialog() {
		mProgressDialog.hide();
	}
}
