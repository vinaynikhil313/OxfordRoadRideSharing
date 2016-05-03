package com.vinay.oxfordroadridesharing.start.presenter;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface OnTokenLoginFinishedListener {

	void onLoginSuccessful(User user);

	void onLoginUnsuccessful();

}
