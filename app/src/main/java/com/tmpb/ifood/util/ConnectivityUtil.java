package com.tmpb.ifood.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dimpos Sitorus on 6/30/2016.
 */
public class ConnectivityUtil {

	private static ConnectivityManager connectivityManager;

	public static ConnectivityUtil instance = new ConnectivityUtil();

	public ConnectivityUtil() {
		// Singleton, empty constructor
	}

	public void setConnectivityManager(Context context) {
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static ConnectivityUtil getInstance() {
		return instance;
	}

	public boolean isNetworkConnected() {
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public boolean isSuccess(int code) {
		return code >= 200 && code < 207;
	}
}
