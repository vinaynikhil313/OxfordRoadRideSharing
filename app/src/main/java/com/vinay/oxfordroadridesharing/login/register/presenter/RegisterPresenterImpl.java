package com.vinay.oxfordroadridesharing.login.register.presenter;

import com.firebase.client.FirebaseError;
import com.vinay.oxfordroadridesharing.login.register.interactor.RegisterInteractor;
import com.vinay.oxfordroadridesharing.login.register.interactor.RegisterInteractorImpl;
import com.vinay.oxfordroadridesharing.login.register.view.RegisterActivity;
import com.vinay.oxfordroadridesharing.login.register.view.RegisterActivityView;
import com.vinay.oxfordroadridesharing.user.User;

/**
 * Created by Vinay Nikhil Pabba on 27-01-2016.
 */
public class RegisterPresenterImpl implements RegisterPresenter, OnRegisterFinishedListener {

	RegisterInteractor interactor;
	RegisterActivityView view;

	public RegisterPresenterImpl(RegisterActivity view) {

		this.view = view;
		interactor = new RegisterInteractorImpl();

	}

	@Override
	public void createUser(String email, String password, String phoneNo) {
		view.showProgressBar();
		if(validate(email, password, phoneNo))
			interactor.registerUser(email, password, phoneNo, this);
	}

	public boolean validate(String email, String password, String phoneNo) {

		boolean valid = true;

		if(email.isEmpty() || ! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			view.hideProgressBar();
			view.registrationError("Enter a valid email address");
			valid = false;
		}

		if(password.isEmpty() || password.length() < 5 || password.length() > 20) {
			view.hideProgressBar();
			view.registrationError("Between 4 and 20 alphanumeric characters");
			valid = false;
		}

		if(phoneNo.isEmpty() || phoneNo.length() != 10){
			view.hideProgressBar();
			view.registrationError("Incorrect phone number");
			valid = false;
		}

		return valid;
	}

	@Override
	public void onFailure(int errorCode) {
		view.hideProgressBar();
		switch(errorCode) {
			case FirebaseError.EMAIL_TAKEN:
				view.hideProgressBar();
				view.registrationError("Email already taken\nTry logging in");
				break;

			case FirebaseError.INVALID_EMAIL:
				view.hideProgressBar();
				view.registrationError("Invalid email. Please try again");
				break;

			default:
				view.hideProgressBar();
				view.registrationError("Error in creating user - " + errorCode);
		}
	}

	@Override
	public void onSuccess(String uid, String token) {
		view.writeToSharedPreferences(uid, token);
		view.hideProgressBar();
		view.openHomePage();
	}

	@Override
	public void onSuccess(User user) {
		view.writeToSharedPreferences(user);
		view.hideProgressBar();
		view.openHomePage();
	}
}
