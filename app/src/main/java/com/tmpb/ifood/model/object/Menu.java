package com.tmpb.ifood.model.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class Menu implements Parcelable {

	private String key;
	private String name;
	private int price;
	private String canteenKey;
	private String picture;

	public Menu() {

	}

	public Menu(String name, int price, String canteenKey) {
		this.name = name;
		this.price = price;
		this.canteenKey = canteenKey;
	}

	public Menu(String key, String name, int price, String canteenKey, String picture) {
		this.key = key;
		this.name = name;
		this.price = price;
		this.canteenKey = canteenKey;
		this.picture = picture;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCanteenKey() {
		return canteenKey;
	}

	public void setCanteenKey(String canteenKey) {
		this.canteenKey = canteenKey;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	//region Parcelable
	public static final Creator<Menu> CREATOR = new Creator<Menu>() {
		@Override
		public Menu createFromParcel(Parcel in) {
			return new Menu(in);
		}

		@Override
		public Menu[] newArray(int size) {
			return new Menu[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(name);
		dest.writeInt(price);
		dest.writeString(canteenKey);
		dest.writeString(picture);
	}

	protected Menu(Parcel in) {
		key = in.readString();
		name = in.readString();
		price = in.readInt();
		canteenKey = in.readString();
		picture = in.readString();
	}
	//endregion
}
