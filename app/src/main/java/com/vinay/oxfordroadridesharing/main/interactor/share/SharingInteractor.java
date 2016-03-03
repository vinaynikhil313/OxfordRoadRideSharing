package com.vinay.oxfordroadridesharing.main.interactor.share;

import com.vinay.oxfordroadridesharing.main.presenter.OnResultGeneratedListener;

/**
 * Created by Vinay Nikhil Pabba on 03-03-2016.
 */
public interface SharingInteractor {

	void getRides(String src, String dstn, OnResultGeneratedListener listener);

}
