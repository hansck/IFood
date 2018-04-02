package com.tmpb.ifood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hans CK on 19-Feb-18.
 */

public class OrderItem implements Parcelable {

	private String menuKey;
	private int quantity;

	public OrderItem() {

	}

	public OrderItem(String menuKey, int quantity) {
		this.menuKey = menuKey;
		this.quantity = quantity;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void addQty() {
		quantity++;
	}

	public boolean decreaseQty() {
		quantity--;
		return quantity != 0;
	}

	//region Parcelable
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
		@Override
		public OrderItem createFromParcel(Parcel in) {
			return new OrderItem(in);
		}

		@Override
		public OrderItem[] newArray(int size) {
			return new OrderItem[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(menuKey);
		dest.writeInt(quantity);
	}

	protected OrderItem(Parcel in) {
		menuKey = in.readString();
		quantity = in.readInt();
	}
	//endregion
}
