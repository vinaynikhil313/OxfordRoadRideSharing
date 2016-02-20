package com.vinay.oxfordroadridesharing.start.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.vinay.oxfordroadridesharing.login.login.LoginActivity;
import com.vinay.oxfordroadridesharing.start.presenter.StartPagePresenter;
import com.vinay.oxfordroadridesharing.start.presenter.StartPagePreviousLoginChecker;
import com.vinay.oxfordroadridesharing.main.MainActivity;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.utils.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Vinay Nikhil Pabba on 16-01-2016.
 * Simple Start screen.
 * Waits for 5 seconds for the Facebook SDK to initialize.
 * Also validates the access token if already present and logs the user in directly without going
 * to the Login screen by using AuthenticateUser class
 */
public class StartPage extends Activity implements StartPageView{

    boolean openLoginPageFlag = true;
    ProgressBar progressBar;

    StartPagePresenter presenter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String TAG = StartPage.class.getSimpleName ();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        displayHashKey ();

        presenter = new StartPagePreviousLoginChecker (this);

        FacebookSdk.sdkInitialize (getApplicationContext (), presenter);

        sharedPreferences = getSharedPreferences (Constants.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit ();

        checkPreviousPasswordLogin ();

        setContentView (R.layout.start_page);

        progressBar = (ProgressBar) findViewById (R.id.progressBar);

    }

    void checkPreviousPasswordLogin(){
        Log.i (TAG, "Checking previous password com.example.benjaminlize.loginapp.GlobalLogin.login");
        //if logged in by email
        if(sharedPreferences.getString ("provider", "").equals ("password")){
            String token = sharedPreferences.getString ("accessToken", "");
            Log.i(TAG + " Token ", token);
            if(!token.equals ("")) {
                presenter.loginWithPassword (token);
                openLoginPageFlag = false;
            }

        }
    }

    @Override
    public void writeToSharedPreferences (String provider, String uid, String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit ();

        Log.i(TAG, provider + " " + uid + " " + accessToken);
        editor.putString ("provider", provider);
        editor.putString ("uid", uid);
        editor.putString ("accessToken", accessToken);
        editor.commit ();
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText (StartPage.this, message, Toast.LENGTH_SHORT).show ();
    }

    private void displayHashKey () {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.benjaminlize.loginapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray ());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    @Override
    protected void onResume () {
        super.onResume ();

        new Handler ().postDelayed (new Runnable () {

            @Override
            public void run () {
                if(openLoginPageFlag) {
                    openLoginPage ();
                }
            }

        }, 5000);
    }

    @Override
    public void openLoginPage () {
        Log.i(TAG, "Opening Login Page");
        startActivity (new Intent(StartPage.this, LoginActivity.class));
        finish();

    }

    @Override
    public void openMainPage () {
        Log.i(TAG, "Opening Main Page");
        startActivity (new Intent(StartPage.this, MainActivity.class));
        finish();
    }

    @Override
    public void disableLoginPage () {
        openLoginPageFlag = false;
    }

}
