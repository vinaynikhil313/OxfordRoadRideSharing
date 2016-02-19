package com.vinay.oxfordroadridesharing.login.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.login.forgot.view.ForgotPasswordActivity;
import com.vinay.oxfordroadridesharing.login.register.view.RegisterActivity;


/**
 * Created by Vinay Nikhil Pabba on 13-01-2016.
 * Main com.example.benjaminlize.loginapp.GlobalLogin.login screen
 */
public class LoginActivity extends AppCompatActivity {

    TextView registerText;
    TextView forgotPasswordText;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.login_activity_main);

        registerText = (TextView) findViewById (R.id.registerText);
        registerText.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                openRegistrationPage ();
            }
        });

        forgotPasswordText = (TextView) findViewById (R.id.forgotPasswordText);
        forgotPasswordText.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                openForgotPasswordPage();
            }
        });
    }

    private void openForgotPasswordPage () {
        startActivity (new Intent (LoginActivity.this, ForgotPasswordActivity.class));
    }

    public void openRegistrationPage(){
        startActivity (new Intent (LoginActivity.this, RegisterActivity.class));
    }

}
