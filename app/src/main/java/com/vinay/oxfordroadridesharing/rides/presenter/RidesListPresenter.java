package com.vinay.oxfordroadridesharing.rides.presenter;

import com.vinay.oxfordroadridesharing.user.Ride;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public interface RidesListPresenter {

	void getDrivers(List<Ride> ridesList);

}
