package com.vinay.oxfordroadridesharing.login.login.email;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinay.oxfordroadridesharing.R;


/**
 * Created by Vinay Nikhil Pabba on 15-01-2016.
 * Contains the EditText fields email and password.
 * Used while logging in and registering
 */
public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate (R.layout.details_fragment, container, false);

        return viewGroup;
    }

}
