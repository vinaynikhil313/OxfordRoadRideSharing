package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.vinay.oxfordroadridesharing.main.interactor.MainActivityFragmentInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.MainActivityFragmentInteractorImpl;
import com.vinay.oxfordroadridesharing.main.view.MainActivityFragmentView;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentPresenterImpl implements MainActivityFragmentPresenter,
		OnResultGeneratedListener {

	private MainActivityFragmentView view;

	private MainActivityFragmentInteractor interactor;

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
	public void getDirections(String src, String dstn) {
		interactor.fetchDirectionsFromApi(src, dstn);
	}

	@Override
	public void getRides(String src, String dstn) {
		view.showProgressDialog();
	}

	@Override
	public void onLocationDetected(Place place) {
		view.moveSourceLocation(place);
	}

	@Override
	public void onDirectionsGenerated(List<LatLng> points, LatLngBounds latLngBounds) {
		view.drawPath(points, latLngBounds);
	}

	@Override
	public void onRidesGenerated() {
		view.hideProgressDialog();
	}
}
