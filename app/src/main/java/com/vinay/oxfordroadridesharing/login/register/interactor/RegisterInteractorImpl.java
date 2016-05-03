package com.vinay.oxfordroadridesharing.login.register.interactor;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vinay.oxfordroadridesharing.login.register.presenter.OnRegisterFinishedListener;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.ApiHelper;
import com.vinay.oxfordroadridesharing.utils.UpdateFirebaseLogin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class RegisterInteractorImpl implements RegisterInteractor,
		Firebase.ValueResultHandler<Map<String, Object>>,
		Firebase.AuthResultHandler,
		ValueEventListener{

	String email;
	String password;
	String phoneNo;

	OnRegisterFinishedListener listener;

	Firebase firebase = ApiHelper.getFirebaseInstance();

	@Override
	public void registerUser(String email, String password, String phoneNo, OnRegisterFinishedListener listener) {
		firebase.createUser(email, password, this);
		this.email = email;
		this.password = password;
		this.phoneNo = phoneNo;
		this.listener = listener;
	}

	@Override
	public void onError(FirebaseError firebaseError) {
		switch(firebaseError.getCode()) {
			case FirebaseError.EMAIL_TAKEN:
				listener.onFailure(FirebaseError.EMAIL_TAKEN);
				break;
		}
	}

	@Override
	public void onSuccess(Map<String, Object> stringObjectMap) {
		Log.i("Authenticate Create", stringObjectMap.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("displayName", "user");
		map.put("email", email);
		map.put("provider", "password");
		map.put("phoneNo", phoneNo);
		firebase.child("users").child((String) stringObjectMap.get("uid")).setValue(map);
		firebase.authWithPassword(email, password, this);
	}

	@Override
	public void onAuthenticated(AuthData authData) {
		//listener.onSuccess(authData.getUid(), authData.getToken());

		UpdateFirebaseLogin.updateFirebase(authData);

		firebase.child ("users").child (authData.getUid ()).addListenerForSingleValueEvent (this);

	}

	@Override
	public void onAuthenticationError(FirebaseError firebaseError) {
		listener.onFailure(firebaseError.getCode());
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		User user = dataSnapshot.getValue (User.class);
		Log.i("EMAIL INTERACTOR", "UID + " + user.getUid ());
		listener.onSuccess (user);
	}

	@Override
	public void onCancelled(FirebaseError firebaseError) {

	}
}
