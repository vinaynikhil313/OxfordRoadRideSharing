package com.vinay.oxfordroadridesharing.rides.presenter;

import com.vinay.oxfordroadridesharing.user.User;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public interface OnDriversGeneratedListener {

	void onDriversListGenerated(List<User> driversList);

}
