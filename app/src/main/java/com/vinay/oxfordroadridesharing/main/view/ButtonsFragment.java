package com.vinay.oxfordroadridesharing.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vinay.oxfordroadridesharing.R;

/**
 * Created by Vinay Nikhil Pabba on 02-03-2016.
 */
public class ButtonsFragment extends Fragment {

	private Button mStartButton = null;
	private Button mCancelButton = null;

	private boolean isRideStarted = false;

	private OnExternalButtonClickedListener mExternalListener;

	public ButtonsFragment(OnExternalButtonClickedListener mExternalListener) {
		this.mExternalListener = mExternalListener;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.buttons_fragment, container, false);

		mStartButton = (Button) view.findViewById(R.id.startDriveButton);
		mCancelButton = (Button) view.findViewById(R.id.cancelDriveButton);

		mStartButton.setOnClickListener(listener);
		mCancelButton.setOnClickListener(listener);

		return view;
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.startDriveButton:
					if(!isRideStarted) {
						isRideStarted = true;
						mExternalListener.startRide();
						mStartButton.setText("Finish");
						mCancelButton.setVisibility(View.GONE);
					}
					else {
						mExternalListener.finishRide();
						isRideStarted = false;
						mStartButton.setText("Start");
						mCancelButton.setVisibility(View.VISIBLE);
					}
					break;

				case R.id.cancelDriveButton:
					mExternalListener.cancelRide();
					break;
			}
		}
	};

	void setButtonText(String text){
		if(mStartButton != null)
			mStartButton.setText(text);
		else
			Log.i("Buttons Fragment", "mStartButton is null");
	}

}
