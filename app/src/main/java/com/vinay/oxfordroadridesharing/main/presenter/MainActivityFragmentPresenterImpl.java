package com.vinay.oxfordroadridesharing.main.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.location.places.Place;
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
        interactor.establishConnection (activity, this);
    }

    @Override
    public void requestLocationUpdates () {
        interactor.requestLocationUpdates ();
    }

    @Override
    public void disconnectGoogleApi () {
        interactor.disconnectConnection ();
    }

    @Override
    public void chooseDestination (Fragment fragment) {
        interactor.showPlaceAutoComplete (fragment);
    }

    @Override
    public void receivedDestination (int resultCode, Intent data) {
        interactor.destinationReceived (resultCode, data);
    }

    @Override
    public void onLocationDetected (Place place) {
        view.moveSourceLocation (place);
    }

    @Override
    public void onDestinationReceived (Place place) {
        view.markDestinationLocation (place);
        //view.createDialog ();
    }
}
