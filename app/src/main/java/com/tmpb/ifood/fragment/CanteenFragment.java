package com.tmpb.ifood.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tmpb.ifood.R;
import com.tmpb.ifood.activity.MainActivity_;
import com.tmpb.ifood.adapter.CanteenAdapter;
import com.tmpb.ifood.model.object.Canteen;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.ConnectivityUtil;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.FirebaseDB;
import com.tmpb.ifood.util.ItemDecoration;
import com.tmpb.ifood.util.OnListItemSelected;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.IgnoreWhen;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EFragment(R.layout.fragment_canteen)
public class CanteenFragment extends BaseFragment {

	private List<Canteen> canteens = new ArrayList<>();
	private CanteenAdapter adapter;
	private boolean isFirstEnter = true;

	@ViewById
	RecyclerView listCanteen;
	@ViewById
	SwipeRefreshLayout swipeRefreshLayout;
//	@ViewById
//	RelativeLayout noItemLayout;

	@AfterViews
	void initLayout() {
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.home));
		((AppCompatActivity) getActivity()).getSupportActionBar().show();
		RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(getActivity());
		listCanteen.setLayoutManager(newsLayoutManager);
		listCanteen.addItemDecoration(new ItemDecoration(1, Common.getInstance().dpToPx(getActivity(), 10), true));
		listCanteen.setItemAnimator(new DefaultItemAnimator());

		adapter = new CanteenAdapter(getActivity(), canteens, canteenListener);
		listCanteen.setAdapter(adapter);

		if (ConnectivityUtil.getInstance().isNetworkConnected() && isFirstEnter) {
			swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
			swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
			onRefreshListener.onRefresh();
			isFirstEnter = false;
		}
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
		swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
		swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
		((MainActivity_) getActivity()).setHomeChecked();
	}

	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void cancelRefresh() {
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	private void setCanteenList() {
		if (listCanteen != null) {
			if (canteens != null && canteens.size() > 0) {
				adapter.notifyDataSetChanged();
				listCanteen.setVisibility(VISIBLE);
			} else {
				listCanteen.setVisibility(GONE);
			}
		}
		cancelRefresh();
	}

	//region Firebase Call
	@IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
	void loadCanteen() {
		final DatabaseReference ref = FirebaseDB.getInstance().getDbReference(Constants.News.NEWS);
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					Canteen canteen = postSnapshot.getValue(Canteen.class);
					canteen.setKey(postSnapshot.getKey());
					canteens.add(canteen);
				}
				setCanteenList();
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
			loadCanteen();
		}
	};

	OnListItemSelected canteenListener = new OnListItemSelected() {
		@Override
		public void onClick(int position) {
//			NewsParent parent = parents.get(position);
//			if (parent instanceof Constants.News) {
//				DetailNewsFragment_ fragment = new DetailNewsFragment_();
//				Bundle bundle = new Bundle();
//				bundle.putParcelable(Constants.News.NEWS, (News) parent);
//				fragment.setArguments(bundle);
//				navigateFragment(R.id.contentFrame, fragment);
//			}
		}
	};
	//endregion
}