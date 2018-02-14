package com.tmpb.ifood.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tmpb.ifood.R;

/**
 * Created by Hans CK on 30-May-17.
 */

public class BaseFragment extends Fragment {

	protected void navigateFragment(int frame, Fragment fragment) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
		ft.replace(frame, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	protected void navigateFragmentWithoutBackstack(int frame, Fragment fragment) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
		ft.replace(frame, fragment);
		ft.commit();
	}
}
