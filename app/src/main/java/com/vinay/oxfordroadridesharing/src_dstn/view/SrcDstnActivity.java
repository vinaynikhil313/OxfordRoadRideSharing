package com.vinay.oxfordroadridesharing.src_dstn.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.application.ORRSApp;
import com.vinay.oxfordroadridesharing.utils.Constants;
import com.vinay.oxfordroadridesharing.utils.Utilities;

/**
 * Created by Vinay Nikhil Pabba on 28-02-2016.
 */
public class SrcDstnActivity extends AppCompatActivity implements SrcDstnView {

	private AutoCompleteTextView from;
	private AutoCompleteTextView to;

	private Button ride;

	private final String TAG = Utilities.getTag(this);

	private PlaceArrayAdapter mPlaceArrayAdapter;

	private Intent mIntent;
	private Bundle mBundle;

	private static final GoogleApiClient mGoogleApiClient = ORRSApp.getGoogleApiClient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.src_dstn);

		mIntent = new Intent();
		mBundle = new Bundle();

		from = (AutoCompleteTextView) findViewById(R.id.from);
		to = (AutoCompleteTextView) findViewById(R.id.to);

		from.setText(Constants.YOUR_LOCATION);

		from.setOnItemClickListener(new OnItemClickListener("src"));
		to.setOnItemClickListener(new OnItemClickListener("dstn"));

		ride = (Button) findViewById(R.id.srcDstnChosen);

		LatLngBounds.Builder mLatLngBoundsBuilder = new LatLngBounds.Builder();
		mLatLngBoundsBuilder.include(new LatLng(-66.44310771413123,-63.2812225073576));
		mLatLngBoundsBuilder.include(new LatLng(66.44310463221659,63.281262405216694));

		mPlaceArrayAdapter = new PlaceArrayAdapter(this,
				android.R.layout.simple_list_item_1,
				mLatLngBoundsBuilder.build(),
				null);
		mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

		from.setAdapter(mPlaceArrayAdapter);
		to.setAdapter(mPlaceArrayAdapter);

		ride.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, from.getText().toString());
				mIntent.putExtras(mBundle);
				setResult(Activity.RESULT_OK, mIntent);
				finish();
			}
		});

	}

	private class OnItemClickListener implements AdapterView.OnItemClickListener {

		private String type;

		public OnItemClickListener(String type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			Log.i(TAG, "Selected: " + item.description);
			Log.i(TAG, "Fetching details for ID: " + item.placeId);
			//Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(mUpdatePlaceDetailsCallback);
			mBundle.putString(type, item.placeId.toString());
		}

		private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
				= new ResultCallback<PlaceBuffer>() {
			@Override
			public void onResult(PlaceBuffer places) {
				if (!places.getStatus().isSuccess()) {
					Log.e(TAG, "Place query did not complete. Error: " +
							places.getStatus().toString());
					return;
				}
				// Selecting the first object buffer.
				final Place place = places.get(0);
				//PlaceDetails mPlaceDetails = new PlaceDetails(place.getId(), place.getName(),
						//place.getAddress(), place.getLatLng());
				//mBundle.putSerializable(type, mPlaceDetails);
				//mBundle.putString(type, getJsonString(mPlaceDetails));
				//Log.i(TAG, type + " " + getJsonString(mPlaceDetails));
				places.release();
			}
		};

	}

}
