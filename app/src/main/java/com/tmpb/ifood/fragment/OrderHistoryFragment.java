package com.tmpb.ifood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tmpb.ifood.R;
import com.tmpb.ifood.activity.DetailOrderActivity_;
import com.tmpb.ifood.adapter.OrderHistoryAdapter;
import com.tmpb.ifood.model.object.Order;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.ConnectivityUtil;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.FirebaseDB;
import com.tmpb.ifood.util.ItemDecoration;
import com.tmpb.ifood.util.ListDivider;
import com.tmpb.ifood.util.OnListItemSelected;
import com.tmpb.ifood.util.manager.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EFragment(R.layout.fragment_order_history)
public class OrderHistoryFragment extends BaseFragment {

	private List<Order> orders = new ArrayList<>();
	private OrderHistoryAdapter adapter;

	@ViewById
	RecyclerView listOrder;
	@ViewById
	SwipeRefreshLayout swipeRefreshLayout;

	@AfterViews
	void initLayout() {
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.history_order));
		((AppCompatActivity) getActivity()).getSupportActionBar().show();

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
		listOrder.setLayoutManager(layoutManager);
		listOrder.addItemDecoration(new ListDivider(getActivity(), R.drawable.bg_divider_full));
		listOrder.setItemAnimator(new DefaultItemAnimator());

		adapter = new OrderHistoryAdapter(getActivity(), orders, orderListener);
		listOrder.setAdapter(adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
			swipeRefreshLayout.destroyDrawingCache();
			swipeRefreshLayout.clearAnimation();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (ConnectivityUtil.getInstance().isNetworkConnected()) {
			swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
			swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
			onRefreshListener.onRefresh();
		}
	}

	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void cancelRefresh() {
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	private void setCanteenList() {
		if (listOrder != null) {
			if (orders != null && orders.size() > 0) {
				adapter.notifyDataSetChanged();
				listOrder.setVisibility(VISIBLE);
			} else {
				listOrder.setVisibility(GONE);
			}
		}
		cancelRefresh();
	}

	//region Firebase Call
	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void loadOrders() {
		orders.clear();
		String email = UserManager.getInstance().getUserEmail();
		final DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.Order.ORDER);
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null) {
					for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
						Order order = postSnapshot.getValue(Order.class);
						order.setKey(postSnapshot.getKey());
						orders.add(order);
					}
					setCanteenList();
				}
				ref.removeEventListener(this);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				if (isAdded()) Common.getInstance().showAlertToast(getActivity(), getString(R.string.default_failed));
				cancelRefresh();
				ref.removeEventListener(this);
			}
		});
	}
	//endregion

	//region Listeners
	SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			swipeRefreshLayout.setRefreshing(true);
			orders.clear();
			loadOrders();
		}
	};

	OnListItemSelected orderListener = new OnListItemSelected() {
		@Override
		public void onClick(int position) {
			Intent intent = new Intent(getActivity(), DetailOrderActivity_.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(Constants.Order.ORDER, orders.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
		}
	};
	//endregion
}