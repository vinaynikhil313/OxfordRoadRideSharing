package com.vinay.oxfordroadridesharing.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.login.login.LoginActivity;
import com.vinay.oxfordroadridesharing.utils.AuthenticateUser;
import com.vinay.oxfordroadridesharing.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.logout :
                AuthenticateUser.unauth ();
                SharedPreferences sharedPreferences = getSharedPreferences (Constants.MY_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit ();
                //editor.remove ("uid");
                //editor.remove ("provider");
                editor.remove ("user");
                editor.commit ();
                LoginManager.getInstance ().logOut ();
                Intent i = new Intent (MainActivity.this, LoginActivity.class);
                startActivity (i);
                finish ();
                break;

            case R.id.settings :
                //  startActivity (new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
