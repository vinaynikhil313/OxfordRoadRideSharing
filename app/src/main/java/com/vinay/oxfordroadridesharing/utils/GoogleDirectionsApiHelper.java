package com.vinay.oxfordroadridesharing.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Vinay Nikhil Pabba on 16-12-2015.
 */

public class GoogleDirectionsApiHelper {

	private final static String TAG = "DirectionsApiHelper";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler
			responseHandler) {
		Log.i(TAG, requestParams.toString());
		client.get(getBaseUrl(url), requestParams, responseHandler);
	}

	public static void post(RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
		Log.i(TAG, requestParams.toString());
		client.post(Constants.DIRECTIONS_API, requestParams, responseHandler);
	}

	private static String getBaseUrl(String url) {
		return Constants.SERVER_ADDRESS + url;
	}

}
