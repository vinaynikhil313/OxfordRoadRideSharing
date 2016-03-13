package com.vinay.oxfordroadridesharing.user;

import java.io.Serializable;

/**
 * Created by Vinay Nikhil Pabba on 02-03-2016.
 */
public class Ride implements Serializable{

	private String id;
	private String driverUid;
	private boolean active;
	private double currentLocationLat;
	private double currentLocationLng;
	private String route;
	private String bounds;

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

	public double getCurrentLocationLat() {
		return currentLocationLat;
	}

	public void setCurrentLocationLat(double currentLocationLat) {
		this.currentLocationLat = currentLocationLat;
	}

	public double getCurrentLocationLng() {
		return currentLocationLng;
	}

	public void setCurrentLocationLng(double currentLocationLng) {
		this.currentLocationLng = currentLocationLng;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getBounds() {
		return bounds;
	}

	public void setBounds(String bounds) {
		this.bounds = bounds;
	}
}
