package com.tmpb.ifood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tmpb.ifood.R;
import com.tmpb.ifood.util.ConnectivityUtil;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.manager.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

	private FirebaseAuth.AuthStateListener listener;
	private boolean first = true;

	@AfterViews
	void initLayout() {
		UserManager.getInstance().initAuth(this);
		ConnectivityUtil.getInstance().setConnectivityManager(getApplicationContext());
		SharedPreferences preference = getSharedPreferences(Constants.General.PREFERENCE, Context.MODE_PRIVATE);
		UserManager.getInstance().setKeyStore(preference);

		listener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				UserManager.getInstance().setFirebaseUser(firebaseAuth.getCurrentUser());
				if (first) {
					first = false;
					Intent intent = new Intent(MainActivity.this, HomeActivity_.class);
					startActivity(intent);
				}
				finish();
			}
		};
	}

	@Override
	public void onStart() {
		super.onStart();
		UserManager.getInstance().getAuth().addAuthStateListener(listener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (listener != null) {
			UserManager.getInstance().getAuth().removeAuthStateListener(listener);
		}
	}
}