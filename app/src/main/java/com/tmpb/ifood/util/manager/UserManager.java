package com.tmpb.ifood.util.manager;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tmpb.ifood.R;
import com.tmpb.ifood.model.object.User;
import com.tmpb.ifood.util.Constants;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class UserManager implements GoogleApiClient.OnConnectionFailedListener {

	private static UserManager instance = new UserManager();
	private FirebaseAuth auth;
	private FirebaseUser firebaseUser;
	private GoogleApiClient googleApiClient;
	private GoogleSignInAccount account;
	private SharedPreferences keyStore;

	public UserManager() {

	}

	public void initAuth(final FragmentActivity activity) {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(activity.getString(R.string.default_web_client_id))
			.requestEmail()
			.build();

		if(googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(activity)
				.enableAutoManage(activity, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		}

		auth = FirebaseAuth.getInstance();
	}

	public static UserManager getInstance() {
		return instance;
	}

	public FirebaseAuth getAuth() {
		return auth;
	}

	public GoogleApiClient getClient() {
		return googleApiClient;
	}

	public FirebaseUser getFirebaseUser() {
		return firebaseUser;
	}

	public void setFirebaseUser(FirebaseUser firebaseUser) {
		this.firebaseUser = firebaseUser;
	}

	public boolean isAccountExist() {
		return (account != null);
	}

	public GoogleSignInAccount getAccount() {
		return account;
	}

	public SharedPreferences getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(SharedPreferences preferences) {
		this.keyStore = preferences;
	}

	public void setKeyStore(GoogleSignInAccount account) {
		this.account = account;
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putString(Constants.User.NAME, account.getDisplayName());
		editor.putString(Constants.User.EMAIL, account.getEmail());
		editor.putString(Constants.User.PHOTO, account.getPhotoUrl().toString());
		editor.apply();
	}

	public void setUserRole(boolean isAdmin) {
		SharedPreferences.Editor editor = keyStore.edit();
		if (isAdmin) {
			editor.putString(Constants.User.ROLE, Constants.User.ADMIN);
		} else {
			editor.putString(Constants.User.ROLE, Constants.User.MEMBER);
		}
		editor.apply();
	}

	public void setUser(User user) {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putString(Constants.User.KEY, user.getKey());
		editor.putString(Constants.User.NAME, user.getName());
		editor.putString(Constants.User.PHONE, user.getPhone());
		editor.apply();
	}

	public void clearKeyStore() {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.clear();
		editor.apply();
	}

	public void setUserPhone(String phoneNumber) {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putString(Constants.User.PHONE, phoneNumber);
		editor.apply();
	}

	public void setVerificationCode(String verificationCode) {
		SharedPreferences.Editor editor = keyStore.edit();
		editor.putString(Constants.User.VERIFICATION_CODE, verificationCode);
		editor.apply();
	}

	public String getUserKey() {
		return keyStore.getString(Constants.User.KEY, "");
	}

	public String getUserName() {
		return keyStore.getString(Constants.User.NAME, "");
	}

	public String getUserEmail() {
		return keyStore.getString(Constants.User.EMAIL, "");
	}

	public String getUserPhoto() {
		return keyStore.getString(Constants.User.PHOTO, "");
	}

	public String getUserRole() {
		return keyStore.getString(Constants.User.ROLE, "");
	}

	public String getUserPhone() {
		return keyStore.getString(Constants.User.PHONE, "");
	}

	public String getVerificationCode() {
		return keyStore.getString(Constants.User.VERIFICATION_CODE, "");
	}

	public User getUser() {
		return new User(getUserKey(), getUserEmail(), getUserName(), getUserPhone());
	}

//	public void setNewsList(List<News> list) {
//		String data = new Gson().toJson(list);
//		SharedPreferences.Editor editor = keyStore.edit();
//		editor.putString(Constants.News.NEWS, data).apply();
//	}
//
//	public List<News> getNewsList() {
//		String str = keyStore.getString(Constants.News.NEWS, "");
//		Type type = new TypeToken<List<News>>() {
//		}.getType();
//		return new Gson().fromJson(str, type);
//	}

	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e("AUTHENTICATION", "Connection Failed!");
	}
}
