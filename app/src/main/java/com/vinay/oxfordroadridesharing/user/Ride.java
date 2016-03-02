package com.vinay.oxfordroadridesharing.user;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vinay Nikhil Pabba on 02-03-2016.
 */
public class Ride {

	private String id;
	private String driverUid;
	private boolean active;
	private LatLng currentLocation;
	private String route;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDriverUid() {
		return driverUid;
	}

	public void setDriverUid(String driverUid) {
		this.driverUid = driverUid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LatLng getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(LatLng currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
}
