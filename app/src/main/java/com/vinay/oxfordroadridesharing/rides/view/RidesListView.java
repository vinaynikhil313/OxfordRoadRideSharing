package com.vinay.oxfordroadridesharing.rides.view;

import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public interface RidesListView {

	void createDriversListView(List<User> driversList);

	void openRideDetailsPage(Ride ride);

}
