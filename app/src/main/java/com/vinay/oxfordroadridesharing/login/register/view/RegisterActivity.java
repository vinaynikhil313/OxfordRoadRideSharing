package com.vinay.oxfordroadridesharing.login.register.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.login.register.presenter.RegisterPresenter;
import com.vinay.oxfordroadridesharing.login.register.presenter.RegisterPresenterImpl;
import com.vinay.oxfordroadridesharing.main.view.MainActivity;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;


/**
 * Created by Vinay Nikhil Pabba on 15-01-2016. Main Register Screen Contains the DetailsFragment and a button to create
 * user using the AuthenticateUser Class.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterActivityView {

	private Fragment details;

	private RegisterPresenter presenter;

	private EditText email;
	private EditText password;
	private EditText phoneNo;

	private ProgressDialog progressDialog;

	Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_main);

		presenter = new RegisterPresenterImpl(this);

		details = getFragmentManager().findFragmentById(R.id.registerDetailsFragment);

		email = (EditText) details.getView().findViewById(R.id.emailText);
		password = (EditText) details.getView().findViewById(R.id.passwordText);
		phoneNo = (EditText) findViewById(R.id.phoneNumber);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Registering User");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(true);

		register = (Button) findViewById(R.id.registerButton);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.createUser(email.getText().toString(), password.getText().toString(), phoneNo.getText().toString());
			}
		});

	}

	@Override
	public void hideProgressBar() {
		progressDialog.hide();
	}

	@Override
	public void showProgressBar() {
		progressDialog.show();
	}

	@Override
	public void openHomePage() {
		Toast.makeText(RegisterActivity.this, "Welcome to Oxford Road Ride Sharing", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(RegisterActivity.this, MainActivity.class));
		finish();
	}

	@Override
	public void registrationError(String message) {
		Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void writeToSharedPreferences(String uid, String token) {
		SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("provider", "password");
		Gson gson = new Gson();
		Log.i("EMAIL VIEW", uid + " " + token);
		editor.putString("uid", uid);
		editor.putString("accessToken", token);
		editor.commit();
		Log.i("Login UID", "The uid is - " + sharedPreferences.getString("uid", ""));
	}

	@Override
	public void writeToSharedPreferences(User user) {
		SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Gson gson = new Gson();
		String jsonString = gson.toJson(user);
		editor.putString("user", jsonString);
	}
}
