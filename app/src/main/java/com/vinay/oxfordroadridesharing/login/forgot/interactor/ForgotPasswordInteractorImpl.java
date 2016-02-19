package com.vinay.oxfordroadridesharing.login.forgot.interactor;

import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.vinay.oxfordroadridesharing.login.forgot.presenter.OnPasswordResetFinishedListener;
import com.vinay.oxfordroadridesharing.utils.Constants;

/**
 * Created by Vinay Nikhil Pabba on 30-01-2016.
 */
public class ForgotPasswordInteractorImpl implements ForgotPasswordInteractor {

    String TAG = ForgotPasswordInteractorImpl.class.getSimpleName();

    OnPasswordResetFinishedListener listener;

    Firebase firebase = new Firebase(Constants.FIREBASE_REF);

    @Override
    public void sendResetEmail (String email, OnPasswordResetFinishedListener listener) {
        this.listener = listener;

        firebase.resetPassword (email, new ResetPasswordResultHandler (Constants.RESET));
    }

    @Override
    public void changePassword (String email, String oldPassword, String newPassword) {
        Log.i(TAG, email+" "+oldPassword+" "+newPassword);
        firebase.changePassword (email, oldPassword, newPassword, new ResetPasswordResultHandler (Constants.CHANGE));
    }

    private class ResetPasswordResultHandler implements Firebase.ResultHandler{

        int flag;

        ResetPasswordResultHandler(int flag){
            this.flag = flag;
        }

        @Override
        public void onError (FirebaseError firebaseError) {
            listener.onFailure (firebaseError.getMessage ());
        }

        @Override
        public void onSuccess () {
            Log.i (TAG + flag, "OnSuccess");
            listener.onSuccess (flag);
        }
    }
}
