package com.vinay.oxfordroadridesharing.rides.presenter;

import android.util.Log;

import com.vinay.oxfordroadridesharing.rides.interactor.RidesListInteractor;
import com.vinay.oxfordroadridesharing.rides.interactor.RidesListInteratorImpl;
import com.vinay.oxfordroadridesharing.rides.view.RidesListView;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public class RidesListPresenterImpl implements RidesListPresenter, OnDriversGeneratedListener {

	private RidesListInteractor interactor;
	private RidesListView view;
	private final String TAG = Utilities.getTag(this);

	public RidesListPresenterImpl(RidesListView view) {
		this.view = view;
		interactor = new RidesListInteratorImpl();
	}

	@Override
	public void getDrivers(List<Ride> ridesList) {
		interactor.generateDriversList(ridesList, this);
	}

	@Override
	public void onDriversListGenerated(List<User> driversList) {
		Log.i(TAG, "Drivers Size = " + driversList.size());
	}
}
