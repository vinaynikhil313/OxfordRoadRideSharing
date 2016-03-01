package com.vinay.oxfordroadridesharing.utils;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Nikhil Pabba on 21-01-2016.
 */
public class UpdateFirebaseLogin {

	private static final Firebase firebase = new Firebase(Constants.FIREBASE_REF);


	public static void updateFirebase(AuthData authData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("provider", authData.getProvider());
		map.put("accessToken", authData.getToken());
		map.put("uid", authData.getUid());
		map.putAll(authData.getProviderData());
		if(map.containsKey("isTemporaryPassword"))
			map.remove("isTemporaryPassword");
		if(map.containsKey("temporaryPassword"))
			map.remove("temporaryPassword");
		if(map.containsKey("cachedUserProfile"))
			map.remove("cachedUserProfile");
		if(map.containsKey("id"))
			map.remove("id");

		firebase.child("users").child(authData.getUid()).updateChildren(map);
	}

	public static void unauth() {

		firebase.unauth();

	}

}
