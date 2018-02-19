package com.tmpb.ifood.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tmpb.ifood.R;
import com.tmpb.ifood.adapter.OrderItemsAdapter;
import com.tmpb.ifood.model.object.Order;
import com.tmpb.ifood.model.object.OrderItem;
import com.tmpb.ifood.model.object.OrderStatus;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.FirebaseDB;
import com.tmpb.ifood.util.ItemDecoration;
import com.tmpb.ifood.util.manager.OrderManager;
import com.tmpb.ifood.util.manager.UserManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tmpb.ifood.model.object.OrderStatus.OPEN;

/**
 * Created by Hans CK on 19-Feb-18.
 */

@EActivity(R.layout.activity_order_detail)
public class DetailOrderActivity extends AppCompatActivity {

	private String canteenKey;
	private List<OrderItem> items;
	private OrderItemsAdapter adapter;
	private String receiver, deliver, notes;
	private static final Random random = new Random();
	private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";

	@ViewById
	RecyclerView listOrderItems;
	@ViewById
	EditText editReceiver;
	@ViewById
	EditText editDeliver;
	@ViewById
	EditText editNotes;
	@ViewById
	TextView price;
	@ViewById
	TextView fee;
	@ViewById
	TextView total;
	@ViewById
	Button btnOrder;
	@ViewById
	Button btnCancel;
	@ViewById
	ProgressBar progressBar;

	@AfterViews
	void initLayout() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle data = getIntent().getExtras();
		if (data != null) {
			canteenKey = data.getString(Constants.Canteen.KEY);
		}
		items = OrderManager.getInstance().getItems();
		RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(this);
		listOrderItems.setLayoutManager(newsLayoutManager);
		listOrderItems.addItemDecoration(new ItemDecoration(1, Common.getInstance().dpToPx(this, 10), true));
		listOrderItems.setItemAnimator(new DefaultItemAnimator());
		adapter = new OrderItemsAdapter(this, items);
		listOrderItems.setAdapter(adapter);
		setView();
	}

	@Click(R.id.btnOrder)
	void onAdd() {
		if (UserManager.getInstance().getFirebaseUser() != null) {
			if (getData()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.dialog_add))
					.setPositiveButton(getString(R.string.yes), addNewsListener)
					.setNegativeButton(getString(R.string.no), addNewsListener).show();
			} else {
				Common.getInstance().showAlertToast(this, getString(R.string.field_empty));
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.dialog_login))
				.setPositiveButton(getString(R.string.dialog_continue), loginListener)
				.setNegativeButton(getString(R.string.cancel), loginListener).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_left, R.anim.exit_right);
	}

	private void setView() {
		int subTotal = OrderManager.getInstance().getTotalPrice();
		int deliveryFee = (int) (subTotal * 0.05);
		price.setText(Common.getInstance().getFormattedPrice(this, subTotal));
		fee.setText(Common.getInstance().getFormattedPrice(this, deliveryFee));
		total.setText(Common.getInstance().getFormattedPrice(this, subTotal + deliveryFee));
	}

	private boolean getData() {
		if (!editReceiver.getText().toString().isEmpty() && !editDeliver.getText().toString().isEmpty()) {
			receiver = editReceiver.getText().toString();
			deliver = editDeliver.getText().toString();
			notes = editNotes.getText().toString();
			return true;
		}
		return false;
	}

	private void setLoading(boolean loading) {
		progressBar.setVisibility(loading ? VISIBLE : GONE);
		btnOrder.setVisibility(loading ? GONE : VISIBLE);
		btnCancel.setVisibility(loading ? GONE : VISIBLE);
	}

	private String generateOrderId() {
		StringBuilder key = new StringBuilder(7);
		for (int i = 0; i < 7; i++) {
			key.append(CHARS.charAt(random.nextInt(CHARS.length())));
		}
		return key.toString();
	}

	private void goToLogin() {
		Intent intent = new Intent(this, LoginActivity_.class);
		startActivity(intent);
	}

	//region Firebase Calls
	private void uploadContent() {
		String menuKey = FirebaseDB.getInstance().getKey(Constants.Order.ORDER);
		Order order = new Order(canteenKey, generateOrderId(), UserManager.getInstance().getUserEmail(), receiver,
			deliver, notes, new Date(), OrderStatus.toInt(OPEN), items);
		FirebaseDB.getInstance().getDbReference(Constants.Order.ORDER).child(menuKey).setValue(order);
		setLoading(false);
		Common.getInstance().showAlertToast(this, getString(R.string.success_add));
	}
	//endregion

	//region Listeners
	DialogInterface.OnClickListener addNewsListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int choice) {
			switch (choice) {
				case DialogInterface.BUTTON_POSITIVE:
					setLoading(true);
					uploadContent();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		}
	};

	DialogInterface.OnClickListener loginListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int choice) {
			switch (choice) {
				case DialogInterface.BUTTON_POSITIVE:
					setLoading(true);
					goToLogin();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		}
	};
	//endregion
}
