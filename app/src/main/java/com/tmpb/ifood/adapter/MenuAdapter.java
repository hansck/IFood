package com.tmpb.ifood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmpb.ifood.R;
import com.tmpb.ifood.fragment.MenuFragment;
import com.tmpb.ifood.model.Menu;
import com.tmpb.ifood.util.Common;
import com.tmpb.ifood.util.ImageUtil;
import com.tmpb.ifood.util.manager.OrderManager;

import java.util.List;

/**
 * Created by Hans CK on 13-Jan-17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

	private Context context;
	private List<Menu> list;
	private MenuFragment fragment;

	public class MenuViewHolder extends RecyclerView.ViewHolder {
		public TextView name, price, count;
		public ImageView image;
		public ImageButton btnMinus, btnPlus;

		public MenuViewHolder(View view) {
			super(view);
			name = (TextView) view.findViewById(R.id.name);
			price = (TextView) view.findViewById(R.id.price);
			count = (TextView) view.findViewById(R.id.count);
			image = (ImageView) view.findViewById(R.id.image);
			btnMinus = (ImageButton) view.findViewById(R.id.btnMinus);
			btnPlus = (ImageButton) view.findViewById(R.id.btnPlus);
		}
	}

	public MenuAdapter(Context context, List<Menu> list, MenuFragment fragment) {
		this.context = context;
		this.list = list;
		this.fragment = fragment;
	}

	@Override
	public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
		return new MenuViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MenuViewHolder holder, int position) {
		final Menu menu = list.get(position);
		holder.name.setText(menu.getName());
		holder.price.setText(Common.getInstance().getFormattedPrice(context, menu.getPrice()));
		ImageUtil.getInstance().setImageResource(context, menu.getPicture(), holder.image);

		holder.btnPlus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(holder.count.getText().toString());
				OrderManager.getInstance().addOrder(menu.getKey(), menu.getPrice());
				count++;
				holder.count.setText(String.valueOf(count));
				fragment.showEstimatedPrice();
			}
		});
		holder.btnMinus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = Integer.parseInt(holder.count.getText().toString());
				if (count > 0) {
					OrderManager.getInstance().reduceOrder(menu.getKey(), menu.getPrice());
					count--;
					holder.count.setText(String.valueOf(count));
					fragment.showEstimatedPrice();
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}
}