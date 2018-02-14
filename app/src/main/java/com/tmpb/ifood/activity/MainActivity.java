package com.tmpb.ifood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.tmpb.ifood.R;
import com.tmpb.ifood.fragment.CanteenFragment;
import com.tmpb.ifood.fragment.CanteenFragment_;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.ImageUtil;
import com.tmpb.ifood.util.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_main)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	@ViewById
	ProgressBar progressBar;
	@ViewById
	Toolbar toolbar;
	@ViewById
	DrawerLayout content;
	@ViewById
	NavigationView navigationView;

	private FragmentManager fm;
	private ActionBarDrawerToggle toggle;

	@AfterViews
	void initLayout() {
		setSupportActionBar(toolbar);
		UserManager.getInstance().initAuth(this);
		SharedPreferences preference = getSharedPreferences(Constants.General.PREFERENCE, Context.MODE_PRIVATE);
		UserManager.getInstance().setKeyStore(preference);
		UserManager.getInstance().initAuth(this);
		toggle = new ActionBarDrawerToggle(this, content, toolbar, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View v) {
			}

			public void onDrawerOpened(View v) {

			}
		};
		content.addDrawerListener(toggle);
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setItemIconTintList(null);
		setHomeChecked();

		fm = getSupportFragmentManager();
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				int nStack = fm.getBackStackEntryCount();
				if (nStack == 0) {
					setHeader();
					setDrawerState(true);
				} else {
					setDrawerState(false);
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				}
				invalidateOptionsMenu();
			}
		});
		setHeader();
	}

	public void setHomeChecked() {
		navigationView.getMenu().getItem(0).setChecked(true);
	}

	private void setHeader() {
		View header = navigationView.getHeaderView(0);
		CircleImageView profileImage = (CircleImageView) header.findViewById(R.id.profileImage);
		ImageUtil.getInstance().setImageProfile(this, UserManager.getInstance().getUserPhoto(), profileImage);
		TextView name = (TextView) header.findViewById(R.id.fullname);
		name.setText(UserManager.getInstance().getUserName());
		TextView email = (TextView) header.findViewById(R.id.email);
		email.setText(UserManager.getInstance().getUserEmail());
	}

	@Override
	public void onBackPressed() {
		if (content.isDrawerOpen(GravityCompat.START)) {
			content.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_dashboard_drawer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (toggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (id == android.R.id.home) {
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav_home:
				goToCanteen();
				break;
			case R.id.nav_history_order:
				goToHistoryOrder();
				break;
			case R.id.nav_signout:
				onSignOut();
				break;
			default:
				break;
		}
		content.closeDrawer(GravityCompat.START);
		return true;
	}

	//region Private Methods
	private void goToCanteen() {
		CanteenFragment_ fragment = new CanteenFragment_();
		navigateTo(fragment);
	}

	private void goToHistoryOrder() {
		Intent intent = new Intent(this, VerificationActivity_.class);
		startActivity(intent);
		finish();
	}

	private void goToLogin() {
		Intent intent = new Intent(MainActivity.this, LoginActivity_.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.enter_left, R.anim.exit_right);
	}

	private void setLoading(boolean loading) {
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
	}

	private void onSignOut() {
		UserManager.getInstance().getAuth().signOut();
		Auth.GoogleSignInApi.signOut(UserManager.getInstance().getClient()).setResultCallback(
			new ResultCallback<Status>() {
				@Override
				public void onResult(@NonNull Status status) {
					UserManager.getInstance().clearKeyStore();
					goToLogin();
				}
			});
	}

	private void setDrawerState(boolean enable) {
		if (enable) {
			content.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		} else {
			content.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}
		toggle.setDrawerIndicatorEnabled(enable);
		toggle.syncState();
		setSupportActionBar(toolbar);
	}

	private void navigateTo(Fragment fragment) {
		if (!isFinishing()) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
			ft.replace(R.id.contentFrame, fragment);
			ft.commit();
		}
	}

	private void navigateToWithBackstack(Fragment fragment) {
		if (!isFinishing()) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
			ft.replace(R.id.contentFrame, fragment);
			ft.addToBackStack(null);
			ft.commit();
		}
	}
	//endregion
}
