package com.vinay.oxfordroadridesharing.main.view;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenter;
import com.vinay.oxfordroadridesharing.main.presenter.MainActivityFragmentPresenterImpl;
import com.vinay.oxfordroadridesharing.main.view.pojo.PlaceDetails;
import com.vinay.oxfordroadridesharing.src_dstn.view.SrcDstnActivity;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainActivityFragmentView, OnPointsGeneratedListener {

	private final String TAG = Utilities.getTag(this);

	private MainActivityFragmentPresenter presenter;

	private MapView mMapView;
	private GoogleMap mGoogleMap;

	private MainActivity mMainActivity;

	Button ride;

	View view;

	private int mRideMode;

	private static Place mSourcePlace;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.maps_fragment, container, false);

		presenter = new MainActivityFragmentPresenterImpl(this);

		mMainActivity = (MainActivity) getActivity();

		setupGoogleMap(savedInstanceState);

		presenter.connectToGoogleApi(mMainActivity);

		ride = (Button) mMainActivity.findViewById(R.id.rideButton);

		ride.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(), SrcDstnActivity.class), Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
			}
		});

		return view;
	}

	private void setupGoogleMap(Bundle savedInstanceState) {

		mMapView = (MapView) view.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();

		MapsInitializer.initialize(mMainActivity.getApplicationContext());

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
	public void markDestinationLocation(Place place) {
		Log.i(TAG, "Destination id = " + place.getId());

		LatLng mLatLng = place.getLatLng();
		MarkerOptions marker = new MarkerOptions()
				.position(mLatLng)
				.title(place.getName().toString())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2_24x40));

		mGoogleMap.addMarker(marker);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(mLatLng)
				.zoom(Constants.LOCATION_ZOOM_LEVEL - 2)
				.build();
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, requestCode + " " + resultCode + " " + data.toString());

		if(requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			//presenter.receivedDestination (resultCode, data);
			Bundle mBundle = data.getExtras();
			String mSrcId = mBundle.getString("src");
			String mDstnId = mBundle.getString("dstn");

			//PlaceDetails src = getPlaceDetails(mBundle.getString("src"));
			//PlaceDetails dstn = getPlaceDetails(mBundle.getString("dstn"));
			Log.i(TAG, mBundle.get("src").toString() + " " + mBundle.get("dstn").toString());
			try {
				new DirectionsFetcher(this, mSrcId, mDstnId).execute(new URL("http://www.facebook.com"));
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	private PlaceDetails getPlaceDetails(String json) {
		Gson gson = new Gson();
		PlaceDetails mPlaceDetails = gson.fromJson(json, PlaceDetails.class);
		return mPlaceDetails;
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

		mSourcePlace = place;

		CameraPosition cameraPosition = new CameraPosition(mLatLng, Constants.LOCATION_ZOOM_LEVEL, 0, 0);
		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void createDialog() {

		AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mMainActivity)
				.setTitle("Type of Ride")
				.setSingleChoiceItems(new String[] {"Drive", "Share"}, 1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Selected item is " + which);
						mRideMode = which;
					}
				})
				.setPositiveButton("Ride!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(mRideMode == 0) {
						}
						//presenter.chooseDestination (MainActivityFragment.this);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "Cancel Pressed");
					}
				});

		mAlertDialogBuilder.show();
	}

	@Override
	public void drawPath(List<LatLng> points) {

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

		mGoogleMap.addPolyline(polyline);
	}
}
