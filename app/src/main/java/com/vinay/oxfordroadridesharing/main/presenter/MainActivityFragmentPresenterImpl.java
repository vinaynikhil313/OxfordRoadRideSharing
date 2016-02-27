package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.vinay.oxfordroadridesharing.main.interactor.MainActivityFragmentInteractor;
import com.vinay.oxfordroadridesharing.main.interactor.MainActivityFragmentInteractorImpl;
import com.vinay.oxfordroadridesharing.main.view.MainActivityFragmentView;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public class MainActivityFragmentPresenterImpl implements MainActivityFragmentPresenter,
        OnConnectionEstablishedListener{

    private MainActivityFragmentView view;

    private MainActivityFragmentInteractor interactor;

    public MainActivityFragmentPresenterImpl (MainActivityFragmentView view) {

        this.view = view;
        interactor = new MainActivityFragmentInteractorImpl ();

    }

    @Override
    public void connectToGoogleApi (Activity activity) {
        interactor.establishConnection(activity, this);
    }

    @Override
    public void disconnectGoogleApi () {
        interactor.disconnectConnection ();
    }

    @Override
    public void onLocationDetected (LatLng latLng) {
        view.moveToLocation (latLng);
    }

    @Override
    public String getPlaceId (LatLng latLng) {
        interactor.getPlaceId (latLng);
        return null;
    }
}
