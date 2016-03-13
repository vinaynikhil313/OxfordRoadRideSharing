package com.vinay.oxfordroadridesharing.rides.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.rides.presenter.RidesListPresenter;
import com.vinay.oxfordroadridesharing.rides.presenter.RidesListPresenterImpl;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public class RidesListActivity extends AppCompatActivity implements RidesListView{

	private final String TAG = Utilities.getTag(this);
	private RidesListPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rides_list_main);

		ArrayList<Ride> matchedRides = (ArrayList<Ride>) getIntent().getSerializableExtra("ridesList");
		Log.i(TAG, "Matched Rides List = " + matchedRides.size());

		presenter = new RidesListPresenterImpl(this);

		presenter.getDrivers(matchedRides);

	}

	@Override
	public void createDriversListView(List<User> driversList) {
		Log.i(TAG, "Drivers List = " + driversList.size());
		ListView mRidesList = new ListView(this);
		mRidesList.setAdapter(null);
	}

	@Override
	public void openRideDetailsPage(Ride ride) {

	}
}
