package com.vinay.oxfordroadridesharing.main.view;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.maps.android.PolyUtil;
import com.vinay.oxfordroadridesharing.main.view.pojo.DirectionsResult;
import com.vinay.oxfordroadridesharing.utils.Utilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 29-02-2016.
 */
public class DirectionsFetcher extends AsyncTask<URL, Integer, String> {

	static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private List<LatLng> latLngs = new ArrayList<LatLng>();

	private final String TAG = Utilities.getTag(this);

	private OnPointsGeneratedListener listener;

	private String src, dstn;

	public DirectionsFetcher(OnPointsGeneratedListener listener, String src, String dstn) {
		this.listener = listener;
		this.src = src;
		this.dstn = dstn;
	}

	protected String doInBackground(URL... urls) {
		try {
			HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) {
					request.setParser(new JsonObjectParser(JSON_FACTORY));
				}
			});

			GenericUrl url = new GenericUrl("http://maps.googleapis.com/maps/api/directions/json");
			Log.i(TAG, "SRC = " + src + " DSTN = " + dstn);
			url.put("origin", "place_id:ChIJOfaIOeKTyzsR_3fOS6AoKVM");
			url.put("destination", "place_id:ChIJAy4rZOKTyzsR5bLfC43rlgU");
			url.put("key", "AIzaSyDTab5b4kFZdRR0mid-p8QqMXmktyCYAic");
			url.put("sensor", false);

			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse httpResponse = request.execute();
			DirectionsResult directionsResult = httpResponse.parseAs(DirectionsResult.class);
			String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
			latLngs = PolyUtil.decode(encodedPoints);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
	}

	protected void onPostExecute(String result) {
		//clearMarkers();
		//addMarkersToMap(latLngs);
		if(latLngs.size() > 0) {
			listener.drawPath(latLngs);
			for(int i = 0; i < latLngs.size(); i++) {
				Log.i(TAG, latLngs.get(i).toString());
			}
		}
	}

}
