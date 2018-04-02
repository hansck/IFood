package com.tmpb.ifood.util.manager;

import com.tmpb.ifood.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans CK on 19-Feb-18.
 */

public class OrderManager {

	private List<OrderItem> items = new ArrayList<>();
	private int totalPrice;

	private static final OrderManager instance = new OrderManager();

	private OrderManager() {
	}

	public static OrderManager getInstance() {
		return instance;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public void addOrder(String menuKey, int price) {
		boolean exist = false;
		for (OrderItem item : items) {
			if (item.getMenuKey().equals(menuKey)) {
				item.addQty();
				totalPrice += price;
				exist = true;
				break;
			}
		}
		if (!exist) {
			items.add(new OrderItem(menuKey, 1));
			totalPrice += price;
		}
	}

	public void reduceOrder(String menuKey, int price) {
		for (OrderItem item : items) {
			if (item.getMenuKey().equals(menuKey)) {
				boolean exist = item.decreaseQty();
				totalPrice -= price;
				if (!exist) items.remove(item);
				break;
			}
		}
	}

	public int getItemsSize() {
		return items.size();
	}

	public int getTotalPrice() {
		return totalPrice;
	}
}
