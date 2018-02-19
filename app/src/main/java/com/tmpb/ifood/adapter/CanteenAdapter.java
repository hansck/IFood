package com.tmpb.ifood.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmpb.ifood.R;
import com.tmpb.ifood.model.object.Canteen;
import com.tmpb.ifood.util.Constants;
import com.tmpb.ifood.util.ImageUtil;
import com.tmpb.ifood.util.OnListItemSelected;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Hans CK on 13-Jan-17.
 */

public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {

	private Context context;
	private List<Canteen> list;
	private OnListItemSelected listener;

	public class CanteenViewHolder extends RecyclerView.ViewHolder {
		public TextView name, location, schedule;
		public ImageView image;
		public CardView cardView;

		public CanteenViewHolder(View view) {
			super(view);
			name = (TextView) view.findViewById(R.id.name);
			location = (TextView) view.findViewById(R.id.location);
			schedule = (TextView) view.findViewById(R.id.schedule);
			image = (ImageView) view.findViewById(R.id.image);
			cardView = (CardView) view.findViewById(R.id.card_view);
		}
	}

	public CanteenAdapter(Context context, List<Canteen> list, OnListItemSelected listener) {
		this.context = context;
		this.list = list;
		this.listener = listener;
	}

	@Override
	public CanteenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_canteen, parent, false);
		return new CanteenViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final CanteenViewHolder holder, int position) {
		Canteen canteen = list.get(position);
		holder.name.setText(canteen.getName());
		holder.location.setText(canteen.getLocation());
		holder.schedule.setText(canteen.getSchedule());
		ImageUtil.getInstance().setImageResource(context, canteen.getPicture(), holder.image);
		holder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(holder.getAdapterPosition());
			}
		});
		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(holder.getAdapterPosition());
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}