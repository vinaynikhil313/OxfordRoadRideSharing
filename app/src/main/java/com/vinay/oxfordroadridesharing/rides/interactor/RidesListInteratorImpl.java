package com.vinay.oxfordroadridesharing.rides.interactor;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vinay.oxfordroadridesharing.application.ORRSApp;
import com.vinay.oxfordroadridesharing.rides.presenter.OnDriversGeneratedListener;
import com.vinay.oxfordroadridesharing.user.Ride;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public class RidesListInteratorImpl implements RidesListInteractor, ValueEventListener {

	private Firebase mFirebase = ORRSApp.getFirebaseInstance();
	private OnDriversGeneratedListener listener;
	private List<User> mDriversList = new ArrayList<>();
	private List<Ride> mRidesList;

	@Override
	public void generateDriversList(List<Ride> ridesList, OnDriversGeneratedListener listener) {
		this.listener = listener;
		this.mRidesList = ridesList;
		for(int i = 0; i < mRidesList.size(); i++)
			mFirebase.child("users").child(mRidesList.get(i).getDriverUid()).addListenerForSingleValueEvent(this);
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		mDriversList.add(dataSnapshot.getValue(User.class));
		if(mDriversList.size() == mRidesList.size()){
			listener.onDriversListGenerated(mDriversList);
		}
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}
}
