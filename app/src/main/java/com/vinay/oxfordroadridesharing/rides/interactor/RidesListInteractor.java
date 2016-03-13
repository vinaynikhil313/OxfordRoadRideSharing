package com.vinay.oxfordroadridesharing.rides.interactor;

import com.vinay.oxfordroadridesharing.rides.presenter.OnDriversGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public interface RidesListInteractor {

	void generateDriversList(List<Ride> ridesList, OnDriversGeneratedListener listener);

}
