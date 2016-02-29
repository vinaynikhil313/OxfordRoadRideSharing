package com.vinay.oxfordroadridesharing.main.view.pojo;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 29-02-2016.
 */
public class DirectionsResult {
    @Key("routes")
    public List<Route> routes;
}