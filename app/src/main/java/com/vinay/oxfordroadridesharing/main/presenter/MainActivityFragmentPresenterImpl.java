package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.vinay.oxfordroadridesharing.main.interactor.drive.MainActivityFragmentInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.drive.MainActivityFragmentInteractorImpl;
import com.vinay.oxfordroadridesharing.main.interactor.share.SharingInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.share.SharingInteractorImpl;
import com.vinay.oxfordroadridesharing.main.view.MainActivityFragmentView;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentPresenterImpl implements MainActivityFragmentPresenter,
		OnResultGeneratedListener {

	private MainActivityFragmentView view;

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
	public void onRidesGenerated(List<Ride> matchedRides) {
		view.hideProgressDialog();
		if(matchedRides.size() > 0)
			view.drawPath(PolyUtil.decode(matchedRides.get(0).getRoute()), PolyUtil.decode(matchedRides.get(0)
					.getBounds()));
	}
}
