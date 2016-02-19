package com.vinay.oxfordroadridesharing.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.vinay.oxfordroadridesharing.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vinay Nikhil Pabba on 15-01-2016.
 * Creating and Authenticating Users is done here using various methods present in Firebase API.
 * We can authenticate using email or Facebook for now. Google, Twitter and Github also supported by Firebase.
 * Unauthorization is also done here
 * The user is directed to MainActivity after authentication
 */
public class AuthenticateUser {

    private static final String TAG = AuthenticateUser.class.getSimpleName ();

    private static Firebase firebase = new Firebase(Constants.FIREBASE_REF);

    public static void authWithEmailPassword(String email, String password, Context context){

        firebase.authWithPassword (email, password, new AuthResultHandler (context));

    }

    public static void authWithFacebook(String token, Context context){

        firebase.authWithOAuthToken ("facebook", token, new AuthResultHandler (context));

    }

    public static void createUser(final String email, final String password, final String provider, final Context context){

        firebase.createUser (email, password, new Firebase.ValueResultHandler<Map<String, Object>> () {


            @Override
            public void onSuccess (Map<String, Object> stringObjectMap) {
                Log.i ("Authenticate Create", stringObjectMap.toString ());
                Toast.makeText (context, "User created", Toast.LENGTH_SHORT).show ();
                Map<String, Object> map = new HashMap<String, Object> ();
                map.put ("displayName", "user");
                map.put ("email", email);
                map.put ("provider", provider);
                firebase.child ("users").child ((String) stringObjectMap.get ("uid")).setValue (map);
                authWithEmailPassword (email, password, context);
            }

            @Override
            public void onError (FirebaseError firebaseError) {
                switch (firebaseError.getCode ()) {
                    case FirebaseError.EMAIL_TAKEN:
                        Toast.makeText (context, "Email Already exists", Toast.LENGTH_LONG).show ();
                        break;
                }
            }
        });

    }

    public static void unauth(){

        firebase.unauth ();

    }

    public static void authWithToken(String token, Context context){

        firebase.authWithCustomToken (token, new AuthResultHandler (context));

    }

    private static class AuthResultHandler implements Firebase.AuthResultHandler{

        Context context;
        private ProgressDialog progressDialog;

        AuthResultHandler(Context context){
            this.context = context;
        }

        @Override
        public void onAuthenticated (AuthData authData) {

            Intent i = new Intent(context, MainActivity.class);
            context.startActivity (i);
            ((Activity)context).finish ();

            Log.i(TAG + " Authentication by ", context.getClass ().getSimpleName ());

            progressDialog = new ProgressDialog (context);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressDialog.show ();

            Map<String, Object> map = new HashMap<String, Object> ();
            map.put ("provider", authData.getProvider ());
            map.put ("accessToken", authData.getToken ());
            map.putAll (authData.getProviderData ());
            SharedPreferences sharedPreferences = context.getSharedPreferences ("MyPref", Context.MODE_PRIVATE);

            Log.i(TAG + " Token ", authData.getToken ());
            Log.i (TAG + " Expires in ", authData.getExpires () + "");

            SharedPreferences.Editor editor = sharedPreferences.edit ();
            editor.putString ("provider", authData.getProvider ());
            editor.putString ("uid", authData.getUid ());
            if(map.containsKey ("isTemporaryPassword"))
                map.remove ("isTemporaryPassword");
            if(map.containsKey ("temporaryPassword"))
                map.remove ("temporaryPassword");
            if(map.containsKey ("cachedUserProfile"))
                map.remove ("cachedUserProfile");

            if(authData.getProvider ().equals ("password")){
                Log.i (TAG, map.keySet ().toString ());
                Log.i (TAG, map.values ().toString ());
                editor.putString ("email", (String) map.get ("email"));
                editor.putString ("accessToken", (String) map.get ("accessToken"));
            }

            editor.commit ();
            firebase.child ("users").child (authData.getUid ()).updateChildren (map);
            Log.i (TAG, "Logged in with provider - " + authData.getProvider ());
        }

        @Override
        public void onAuthenticationError (FirebaseError firebaseError) {

            Log.e(TAG + " Firebase Error", firebaseError.getMessage ());
            Log.e(TAG + " Firebase Error", context.getClass ().getSimpleName());

            switch(firebaseError.getCode ()){
                case FirebaseError.USER_DOES_NOT_EXIST:
                    Toast.makeText (context, "USER DOES NOT EXIST", Toast.LENGTH_LONG).show ();
                    break;

                case FirebaseError.INVALID_TOKEN:
                    Toast.makeText (context, "INVALID TOKEN", Toast.LENGTH_LONG).show ();
                    break;

                case FirebaseError.EXPIRED_TOKEN:
                    Toast.makeText (context, "EXPIRED TOKEN", Toast.LENGTH_LONG).show ();
                    break;


            }

        }

    }
}
