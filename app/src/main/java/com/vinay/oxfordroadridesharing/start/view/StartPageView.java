package com.vinay.oxfordroadridesharing.start.view;

import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public interface StartPageView {

	void writeToSharedPreferences(User user);

	void showMessage(String message);

	void openMainPage();

	void openLoginPage();

	void disableLoginPage();

}
