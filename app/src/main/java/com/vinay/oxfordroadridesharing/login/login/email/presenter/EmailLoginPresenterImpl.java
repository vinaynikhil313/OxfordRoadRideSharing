package com.vinay.oxfordroadridesharing.login.login.email.presenter;

import android.util.Log;

import com.vinay.oxfordroadridesharing.login.login.email.interactor.EmailLoginInteractor;
import com.vinay.oxfordroadridesharing.login.login.email.interactor.EmailLoginInteractorImpl;
import com.vinay.oxfordroadridesharing.login.login.email.view.EmailLoginFragmentView;
import com.vinay.oxfordroadridesharing.login.login.email.view.LoginActivityFragment;
import com.vinay.oxfordroadridesharing.user.User;


/**
 * Created by Vinay Nikhil Pabba on 21-01-2016.
 */
public class EmailLoginPresenterImpl implements EmailLoginPresenter, OnEmailLoginFinishedListener {

    private EmailLoginFragmentView view;
    private EmailLoginInteractor interactor;

    public EmailLoginPresenterImpl (LoginActivityFragment loginActivityFragment){
        this.view = loginActivityFragment;
        interactor = new EmailLoginInteractorImpl ();
    }

    @Override
    public void authenticateCredentials (String email, String password) {

        view.showProgressDialog ();
        if(view != null)
            interactor.authenticateWithEmail (email, password, this);
        //AuthenticateUser.authWithEmailPassword (email, password, loginActivityFragment.getFragment ().getContext ());
    }

    @Override
    public void onEmailError () {
        view.hideProgressDialog ();
        view.emailError ();
    }

    @Override
    public void onPasswordError () {
        view.hideProgressDialog ();
        view.passwordError ();
    }

    @Override
    public void onSuccess (User user) {
        view.writeToSharedPreferences (user);
        Log.i ("EMAIL PRESENTER", "UID = " + user.getId ());
        view.hideProgressDialog ();
        //if(user.getPreferences ().size () > 0)
            view.openMainPage ();
        //else{}
            //view.openPreferencesPage ();
    }
}