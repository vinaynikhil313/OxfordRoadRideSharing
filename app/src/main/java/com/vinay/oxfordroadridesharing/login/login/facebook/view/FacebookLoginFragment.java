package com.vinay.oxfordroadridesharing.login.login.facebook.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.vinay.oxfordroadridesharing.main.MainActivity;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.login.login.facebook.presenter.FacebookLoginPresenterImpl;
import com.vinay.oxfordroadridesharing.user.User;
import com.vinay.oxfordroadridesharing.utils.Constants;

/**
 * Created by Vinay Nikhil Pabba on 14-01-2016.
 * Contains the Facebook com.example.benjaminlize.loginapp.GlobalLogin.login button and its related callbacks.
 */
public class FacebookLoginFragment extends Fragment implements FacebookLoginFragmentView{

    ProgressDialog progressDialog;

    CallbackManager callbackManager;
    LoginButton facebookLoginButton;
    private static final String TAG = FacebookLoginFragment.class.getSimpleName ();

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate (R.layout.login_facebook_fragment, container, false);

        progressDialog = new ProgressDialog (getContext ());
        progressDialog.setMessage ("Logging you in");
        progressDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate (true);

        callbackManager = CallbackManager.Factory.create ();

        facebookLoginButton = (LoginButton) viewGroup.findViewById (R.id.facebookLoginButton);
        facebookLoginButton.setFragment (FacebookLoginFragment.this);
        facebookLoginButton.setReadPermissions ("public_profile email");

        Log.i (TAG, "Facebook Login Button created");

        facebookLoginButton.registerCallback (callbackManager, new FacebookLoginPresenterImpl (this));

        return viewGroup;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        callbackManager.onActivityResult (requestCode, resultCode, data);
    }

    @Override
    public void onError () {
        Toast.makeText (getContext (), "Facebook Login Error!!", Toast.LENGTH_LONG).show ();
    }

    @Override
    public void openMainPage () {
        Toast.makeText (getContext (), "Login Successful", Toast.LENGTH_SHORT).show ();
        startActivity (new Intent(getContext (), MainActivity.class));
        ((Activity) getContext ()).finish ();
    }

    @Override
    public void writeToSharedPrefernces (User user) {
        SharedPreferences sharedPreferences = getContext ().getSharedPreferences (Constants.MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit ();
        Gson gson = new Gson ();
        String userJson = gson.toJson (user);
        editor.putString ("user", userJson);
        editor.commit ();
    }

    @Override
    public void hideProgressDialog () {
        progressDialog.hide ();
    }

    @Override
    public void showProgressDialog () {
        progressDialog.hide ();
    }
}
