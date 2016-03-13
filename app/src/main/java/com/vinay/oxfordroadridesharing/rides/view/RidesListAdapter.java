package com.vinay.oxfordroadridesharing.rides.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 05-03-2016.
 */
public class RidesListAdapter extends ArrayAdapter<User> {

	private List<User> mDriversList;

	public RidesListAdapter(Context context, List<User> objects) {
		super(context, 0, objects);
		this.mDriversList = objects;
	}

	@Override
	public User getItem(int position) {
		return mDriversList.get(position);
	}

	@Override
	public int getCount() {
		return mDriversList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		User mUser = getItem(position);
		if(mUser == null)
			return null;

		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.ride_item, parent);
		}

		return convertView;
	}
}
