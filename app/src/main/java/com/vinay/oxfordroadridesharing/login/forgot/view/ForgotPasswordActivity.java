package com.vinay.oxfordroadridesharing.login.forgot.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vinay.oxfordroadridesharing.login.forgot.presenter.ForgotPasswordPresenter;
import com.vinay.oxfordroadridesharing.login.forgot.presenter.ForgotPasswordPresenterImpl;
import com.vinay.oxfordroadridesharing.login.login.LoginActivity;
import com.vinay.oxfordroadridesharing.R;


/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordView {

    ProgressDialog progressDialog;

    EditText email;
    Button resetButton;

    ForgotPasswordPresenter presenter;

    AlertDialog changePassword;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.forgot_password_main);

        presenter = new ForgotPasswordPresenterImpl (this);

        progressDialog = new ProgressDialog (this);
        progressDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate (true);

        //Disabling the Password EditText inside DetailsFragment
        Fragment details = getFragmentManager ().findFragmentById (R.id.forgotPasswordDetailsFragment);
        details.getView ().findViewById (R.id.passwordText).setVisibility (View.GONE);

        email = (EditText) findViewById (R.id.emailText);

        resetButton = (Button) findViewById (R.id.passwordResetButton);

        resetButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                presenter.resetPassword (email.getText ().toString ());
            }
        });



        changePassword = new AlertDialog.Builder (this)
                .setTitle ("Change your password")
                .setView (R.layout.change_password_fragment)
                .setPositiveButton ("Change", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {

                        Dialog d = (Dialog) dialog;
                        EditText oldPassword = (EditText) d.findViewById (R.id.oldPassword);
                        EditText newPassword = (EditText) d.findViewById (R.id.newPassword);

                        presenter.changePassword(email.getText ().toString (),
                                oldPassword.getText ().toString (), newPassword.getText ().toString ());
                    }
                })
                .create ();
    }

    @Override
    public void hideProgressDialog () {
        progressDialog.hide ();
    }

    @Override
    public void showProgressDialog (String message) {
        progressDialog.setMessage (message);
        progressDialog.show ();
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText (ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void showChangePasswordDialog () {
        changePassword.show ();
    }

    @Override
    public void hideChangePasswordDialog () {
        changePassword.hide ();
    }

    @Override
    public void openLoginPage () {
        startActivity (new Intent (ForgotPasswordActivity.this, LoginActivity.class));
        finish ();
    }
}
