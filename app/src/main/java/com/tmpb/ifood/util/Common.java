package com.tmpb.ifood.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.tmpb.ifood.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hans CK on 19-Jan-17.
 */

public class Common {

	private static final int REQUEST_PERMISSIONS = 1;
	private SimpleDateFormat fullFormat = new SimpleDateFormat(Constants.DateFormat.FULL_DATE);
	private SimpleDateFormat shortFormat = new SimpleDateFormat(Constants.DateFormat.SHORT_DATE);
	private DecimalFormat decimalFormat = new DecimalFormat("#,###");
	private static Common instance = new Common();

	public static Common getInstance() {
		return instance;
	}

	private Common() {
	}

	public void showAlertToast(Activity activity, String text) {
		try {
			Snackbar snackbar = Snackbar.make(activity.getCurrentFocus(), text, Snackbar.LENGTH_LONG);
			TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
			tv.setMaxLines(5);
			snackbar.show();
		} catch (NullPointerException e) {
			View view = activity.getWindow().getDecorView().findViewById(R.id.content);
			if (view != null) Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
		} catch (IllegalArgumentException e) {
			View view = activity.getWindow().getDecorView().findViewById(R.id.content);
			if (view != null) Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
		}
	}

	public void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager i = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (activity.getCurrentFocus() != null) {
				i.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFormattedPrice(Context context, int price) {
		return context.getString(R.string.price_rp, decimalFormat.format(price));
	}

	public boolean verifyPermission(int[] grantResults) {
		if (grantResults.length < 1) {
			return false;
		}
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	public void requestRequiredPermissions(final Activity activity) {
		if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA) ||
			ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission
				.WRITE_EXTERNAL_STORAGE)) {
			Snackbar.make(activity.getCurrentFocus(), activity.getString(R.string.permission_required), Snackbar
				.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
				@RequiresApi(api = Build.VERSION_CODES.M)
				@Override
				public void onClick(View v) {
					activity.requestPermissions(Constants.Permissions.CAMERA, REQUEST_PERMISSIONS);
				}
			}).show();
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				activity.requestPermissions(Constants.Permissions.CAMERA, REQUEST_PERMISSIONS);
			}
		}
	}

	public int dpToPx(Context context, int dp) {
		Resources r = context.getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}

	public String formatDateFull(Date date) {
		return fullFormat.format(date);
	}

	public String formatDateShort(Date date) {
		return shortFormat.format(date);
	}
}
