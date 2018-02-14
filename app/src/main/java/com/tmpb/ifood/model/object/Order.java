package com.tmpb.ifood.model.object;

import java.util.Date;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class Order {

	private String key;
	private String canteenKey;
	private String userKey;
	private String menuKey;
	private String quantity;
	private Date date;
	private int status;

	public Order(String key, String canteenKey, String userKey, String menuKey, String quantity, Date date, int status) {
		this.key = key;
		this.canteenKey = canteenKey;
		this.userKey = userKey;
		this.menuKey = menuKey;
		this.quantity = quantity;
		this.date = date;
		this.status = status;
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

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
