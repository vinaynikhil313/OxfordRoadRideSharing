package com.vinay.oxfordroadridesharing.main.view.pojo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vinay Nikhil Pabba on 01-03-2016.
 */
public class PlaceDetails{

	private CharSequence name;
	private String id;
	private CharSequence address;
	private LatLng latLng;

	public CharSequence getName() {
		return name;
	}

	public void setName(CharSequence name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CharSequence getAddress() {
		return address;
	}

	public void setAddress(CharSequence address) {
		this.address = address;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public PlaceDetails(String id, CharSequence name, CharSequence address, LatLng latLng) {
		this.name = name;
		this.id = id;
		this.address = address;
		this.latLng = latLng;
	}

	@Override
	public String toString() {
		return "ID : " + getId()
				+ "\nName : " + getName()
				+ "\nAddress : " + getAddress()
				+ "\nLatLng : " + getLatLng().toString();
	}
}
