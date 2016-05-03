package com.vinay.oxfordroadridesharing.login.register.presenter;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public interface OnRegisterFinishedListener {

	void onSuccess(String uid, String token);

	void onSuccess(User user);

	void onFailure(int errorCode);

}
