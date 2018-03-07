package com.tmpb.ifood.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class Order implements Parcelable {

	private String key;
	private String canteenKey;
	private String orderId;
	private String custEmail;
	private String receiver;
	private String deliverTo;
	private String notes;
	private long date;
	private int status;
	private List<OrderItem> items = new ArrayList<>();

	public Order() {

	}

	public Order(String canteenKey, String orderId, String custEmail, String receiver, String deliverTo, String notes, long date, int status, List<OrderItem> items) {
		this.canteenKey = canteenKey;
		this.orderId = orderId;
		this.custEmail = custEmail;
		this.receiver = receiver;
		this.deliverTo = deliverTo;
		this.notes = notes;
		this.date = date;
		this.status = status;
		this.items = items;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCanteenKey() {
		return canteenKey;
	}

	public void setCanteenKey(String canteenKey) {
		this.canteenKey = canteenKey;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getDeliverTo() {
		return deliverTo;
	}

	public void setDeliverTo(String deliverTo) {
		this.deliverTo = deliverTo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	//region Parcelable
	public static final Creator<Order> CREATOR = new Creator<Order>() {
		@Override
		public Order createFromParcel(Parcel in) {
			return new Order(in);
		}

		@Override
		public Order[] newArray(int size) {
			return new Order[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(canteenKey);
		dest.writeString(orderId);
		dest.writeString(custEmail);
		dest.writeString(receiver);
		dest.writeString(deliverTo);
		dest.writeString(notes);
		dest.writeInt(status);
		dest.writeLong(date);
		dest.writeTypedList(items);
	}

	protected Order(Parcel in) {
		key = in.readString();
		canteenKey = in.readString();
		orderId = in.readString();
		custEmail = in.readString();
		receiver = in.readString();
		deliverTo = in.readString();
		notes = in.readString();
		status = in.readInt();
		date = in.readLong();
		items = in.createTypedArrayList(OrderItem.CREATOR);
	}
	//endregion
}
