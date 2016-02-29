package com.vinay.oxfordroadridesharing.main.view;

import com.google.android.gms.location.places.Place;

/**
 * Created by Vinay Nikhil Pabba on 27-02-2016.
 */
public interface MainActivityFragmentView{

    void moveSourceLocation(Place place);

    void markDestinationLocation(Place place);

    void createDialog();

}
