package com.vinay.oxfordroadridesharing.login.login.email.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vinay.oxfordroadridesharing.login.login.email.presenter.EmailLoginPresenter;
import com.vinay.oxfordroadridesharing.login.login.email.presenter.EmailLoginPresenterImpl;
import com.vinay.oxfordroadridesharing.main.MainActivity;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.utils.Constants;


/**
 * Created by Vinay Nikhil Pabba on 14-01-2016.
 * Fragment containing the fields on the Login Screen.
 * Check the credentials entered for logging in using the AuthenticateUser Class and logs in the user.
 * Can also open the Register page when clicked on the text
 */
public class LoginActivityFragment extends Fragment implements EmailLoginFragmentView {

    Button loginButton;
    EditText email;
    EditText password;
    ProgressDialog progressDialog;

    private EmailLoginPresenter emailLoginPresenter;

    private static final String TAG = LoginActivityFragment.class.getSimpleName ();

    View viewGroup;

    public LoginActivityFragment(){

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewGroup = inflater.inflate (R.layout.login_fragment_main, container, false);

        emailLoginPresenter = new EmailLoginPresenterImpl (this);

        email = (EditText) viewGroup.findViewById (R.id.emailText);
        password = (EditText) viewGroup.findViewById (R.id.passwordText);

        progressDialog = new ProgressDialog (getContext ());
        progressDialog.setMessage ("Logging you in");
        progressDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate (true);

        loginButton = (Button) viewGroup.findViewById (R.id.loginButton);
        loginButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Log.i (TAG, "Login button Clicked");
                emailLoginPresenter.authenticateCredentials (email.getText ().toString (), password.getText ().toString ());
            }
        });
        return viewGroup;

    }

    @Override
    public void emailError(){
        Toast.makeText (getContext (), "Email Error!", Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void passwordError(){
        Toast.makeText (getContext (), "Password Error!", Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void openMainPage () {
        Toast.makeText (getContext (), "Login Successful", Toast.LENGTH_SHORT).show ();
        startActivity (new Intent(getContext (), MainActivity.class));
        ((Activity) getContext ()).finish ();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
    }

    @Override
    public void writeToSharedPreferences (String uid, String token) {
        SharedPreferences sharedPreferences = getContext ().getSharedPreferences (Constants.MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit ();
        editor.putString ("provider", "password");
        Log.i ("EMAIL VIEW", uid + " " + token);
        editor.putString ("uid", uid);
        editor.putString ("accessToken", token);
        editor.commit ();
        Log.i("Login UID", "The uid is - " + sharedPreferences.getString ("uid", ""));
    }

    @Override
    public void hideProgressDialog () {
        progressDialog.hide ();
    }

    @Override
    public void showProgressDialog () {
        progressDialog.show ();
    }
}
