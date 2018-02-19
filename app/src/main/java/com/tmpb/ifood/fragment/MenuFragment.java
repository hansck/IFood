package com.tmpb.ifood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tmpb.ifood.R;
import com.tmpb.ifood.activity.DetailOrderActivity_;
import com.tmpb.ifood.adapter.MenuAdapter;
import com.tmpb.ifood.model.object.Canteen;
import com.tmpb.ifood.model.object.Menu;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.ConnectivityUtil;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.FirebaseDB;
import com.tmpb.ifood.util.ImageUtil;
import com.tmpb.ifood.util.ItemDecoration;
import com.tmpb.ifood.util.manager.MenuManager;
import com.tmpb.ifood.util.manager.OrderManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EFragment(R.layout.fragment_menu)
public class MenuFragment extends BaseFragment {

	private Canteen canteen;
	private List<Menu> menus = new ArrayList<>();
	private MenuAdapter adapter;

	@ViewById
	ImageView picture;
	@ViewById
	TextView name;
	@ViewById
	TextView location;
	@ViewById
	TextView schedule;
	@ViewById
	TextView totalPrice;
	@ViewById
	RelativeLayout buttonContainer;
	@ViewById
	RecyclerView listMenu;
	@ViewById
	Toolbar toolbar;
	@ViewById
	AppBarLayout appBarLayout;
	@ViewById
	CollapsingToolbarLayout collapsingToolbar;

	@AfterViews
	void initLayout() {
		Bundle data = this.getArguments();
		if (data != null) {
			canteen = data.getParcelable(Constants.Canteen.CANTEEN);
			setView();
		}

		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().hide();
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getActivity().onBackPressed();
			}
		});
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				if (verticalOffset <= toolbar.getHeight() - collapsingToolbar.getHeight()) {
					collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
					collapsingToolbar.setTitle(canteen.getName());
				} else {
					collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
					collapsingToolbar.setTitle("");
				}
			}
		});

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
		listMenu.setLayoutManager(layoutManager);
		listMenu.addItemDecoration(new ItemDecoration(1, Common.getInstance().dpToPx(getActivity(), 10), true));
		listMenu.setItemAnimator(new DefaultItemAnimator());
		adapter = new MenuAdapter(getActivity(), menus, MenuFragment.this);
		listMenu.setAdapter(adapter);

		if (ConnectivityUtil.getInstance().isNetworkConnected()) {
			menus.clear();
			loadMenu();
		}
	}

	@Click(R.id.buttonContainer)
	void onButtonClicked() {
		goToOrderDetail();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void setView() {
		name.setText(canteen.getName());
		location.setText(canteen.getLocation());
		schedule.setText(canteen.getSchedule());
		ImageUtil.getInstance().setImageResource(getActivity(), canteen.getPicture(), picture);
	}

	private void goToOrderDetail() {
		Intent intent = new Intent(getActivity(), DetailOrderActivity_.class);
		intent.putExtra(Constants.Canteen.KEY, canteen.getKey());
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
	}

	private void setMenuList() {
		if (listMenu != null) {
			if (menus != null && menus.size() > 0) {
				adapter.notifyDataSetChanged();
				listMenu.setVisibility(VISIBLE);
			} else {
				listMenu.setVisibility(GONE);
			}
		}
		MenuManager.getInstance().setMenus(menus);
	}

	public void showEstimatedPrice() {
		if (OrderManager.getInstance().getItemsSize() > 0) {
			buttonContainer.setVisibility(VISIBLE);
		} else {
			buttonContainer.setVisibility(GONE);
		}
		totalPrice.setText(Common.getInstance().getFormattedPrice(getActivity(), OrderManager.getInstance().getTotalPrice()));
	}

	//region Firebase Call
	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void loadMenu() {
		final DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.Menu.MENU);
		ref.orderByChild(Constants.Menu.CANTEEN_KEY).equalTo(canteen.getKey()).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					Menu menu = postSnapshot.getValue(Menu.class);
					menu.setKey(postSnapshot.getKey());
					menus.add(menu);
				}
				setMenuList();
				ref.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				if (isAdded()) Common.getInstance().showAlertToast(getActivity(), getString(R.string.default_failed));
				ref.removeEventListener(this);
			}
		});
	}
	//endregion
}