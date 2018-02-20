package com.tmpb.ifood.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmpb.ifood.R;
import com.tmpb.ifood.model.object.Order;
import com.tmpb.ifood.model.object.OrderStatus;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.OnListItemSelected;
import com.tmpb.ifood.util.manager.CanteenManager;

import java.util.List;

/**
 * Created by Hans CK on 13-Jan-17.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

	private Context context;
	private List<Order> list;
	private OnListItemSelected listener;

	public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
		public TextView orderId, status, canteen, date;
		public LinearLayout content;

		public OrderHistoryViewHolder(View view) {
			super(view);
			orderId = (TextView) view.findViewById(R.id.orderId);
			status = (TextView) view.findViewById(R.id.status);
			canteen = (TextView) view.findViewById(R.id.canteen);
			date = (TextView) view.findViewById(R.id.date);
			content = (LinearLayout) view.findViewById(R.id.content);
		}
	}

	public OrderHistoryAdapter(Context context, List<Order> list, OnListItemSelected listener) {
		this.context = context;
		this.list = list;
		this.listener = listener;
	}

	@Override
	public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
		return new OrderHistoryViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final OrderHistoryViewHolder holder, int position) {
		final Order order = list.get(position);
		holder.orderId.setText(order.getOrderId());
		holder.canteen.setText(CanteenManager.getInstance().getNameById(order.getCanteenKey()));
		holder.date.setText(Common.getInstance().formatDateFull(order.getDate()));
		setStatus(order.getStatus(), holder.status);
		holder.content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(holder.getAdapterPosition());
			}
		});
	}

	private void setStatus(int status, TextView view) {
		String title = OrderStatus.getTitle(status);
		Spannable span = new SpannableString(" " + title + " ");
		int color = 0;
		switch (OrderStatus.fromInt(status)) {
			case OPEN:
				color = ContextCompat.getColor(context, R.color.ic_open);
				break;
			case IN_PROGRESS:
				color = ContextCompat.getColor(context, R.color.ic_in_progress);
				break;
			case CANCELLED:
				color = ContextCompat.getColor(context, R.color.ic_cancel);
				break;
			case COMPLETED:
				color = ContextCompat.getColor(context, R.color.ic_done);
				break;
		}
		span.setSpan(new BackgroundColorSpan(color), 0, title.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(span);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}