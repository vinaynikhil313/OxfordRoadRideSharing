package com.vinay.oxfordroadridesharing.main.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vinay.oxfordroadridesharing.R;
import com.vinay.oxfordroadridesharing.user.User;

import java.util.List;

/**
 * Created by Vinay Nikhil Pabba on 24-04-2016.
 */
public class DriversListAdapter extends ArrayAdapter<User> {

	public DriversListAdapter(Context context, List<User> objects) {
		super(context, 0, objects);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.ride_item, parent, false);
		final User mUser = getItem(position);
		if(mUser.getPhoneNo() == null)
			mUser.setPhoneNo("1234567890");
		TextView name = (TextView) convertView.findViewById(R.id.displayName);
		TextView phoneNo = (TextView) convertView.findViewById(R.id.displayPhoneNo);
		name.setText(mUser.getDisplayName());
		phoneNo.setText((mUser.getPhoneNo()));
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				String uri = "tel:" + mUser.getPhoneNo();
				intent.setData(Uri.parse(uri));
				getContext().startActivity(intent);
			}
		});
		return convertView;
	}
}
