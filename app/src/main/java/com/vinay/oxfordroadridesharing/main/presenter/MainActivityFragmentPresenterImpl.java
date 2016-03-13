package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.main.interactor.drive.MainActivityFragmentInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.drive.MainActivityFragmentInteractorImpl;
import com.vinay.oxfordroadridesharing.main.interactor.share.SharingInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.share.SharingInteractorImpl;
import com.vinay.oxfordroadridesharing.main.view.MainActivityFragmentView;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentPresenterImpl implements MainActivityFragmentPresenter,
		OnResultGeneratedListener {

	private MainActivityFragmentView view;

	private final String TAG = Utilities.getTag(this);

	private MainActivityFragmentInteractor interactor;
	private SharingInteractor mSharingInteractor;

	public MainActivityFragmentPresenterImpl(MainActivityFragmentView view) {
		this.view = view;
		interactor = new MainActivityFragmentInteractorImpl();
	}

	@Override
	public void connectToGoogleApi(Activity activity) {
		interactor.establishConnection(activity, this);
	}

	@Override
	public void requestLocationUpdates() {
		interactor.requestLocationUpdates();
	}

	@Override
	public void disconnectGoogleApi() {
		interactor.disconnectConnection();
	}

	@Override
	public void getDirections(User user, String src, String dstn) {
		interactor.fetchDirectionsFromApi(user, src, dstn);
	}

	@Override
	public void getRides(String src, String dstn) {
		view.showProgressDialog();
		mSharingInteractor = new SharingInteractorImpl();
		mSharingInteractor.getRides(src, dstn, this);
	}

	@Override
	public void startRide() {
		interactor.startRide();
	}

	@Override
	public void finishRide() {
		interactor.finishRide();
	}

	@Override
	public void onLocationDetected(LatLng latLng) {
		view.moveSourceLocation(latLng);
	}

	@Override
	public void onDirectionsGenerated(List<LatLng> points, List<LatLng> latLngBounds) {
		view.drawPath(points, latLngBounds);
	}

	@Override
	public void onRidesGenerated(ArrayList<Ride> matchedRides) {
		view.hideProgressDialog();
		Log.i(TAG, "Size = " + matchedRides.size());
		if(matchedRides != null && matchedRides.size() > 0) {
			Log.i(TAG, "Route = " + matchedRides.get(0).getRoute());
			Log.i(TAG, "Bounds = " + matchedRides.get(0).getBounds());
			//view.drawPath(PolyUtil.decode(matchedRides.get(0).getRoute()), PolyUtil.decode(matchedRides.get(0)
			//		.getBounds()));
			//view.openRidesList(matchedRides);
		}
	}

	@Override
	public void onDriversListGenerated(List<Ride> matchedRides, List<User> driversList) {
		if(driversList.size() > 0){
			view.showDrivers(matchedRides, driversList);
		}
	}
}
