package com.tmpb.ifood.util.manager;

import com.tmpb.ifood.model.object.Canteen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans CK on 19-Feb-18.
 */

public class CanteenManager {

	private List<Canteen> canteens = new ArrayList<>();

	private static final CanteenManager instance = new CanteenManager();

	private CanteenManager() {

	}

	public static CanteenManager getInstance() {
		return instance;
	}

	public List<Canteen> getCanteens() {
		return canteens;
	}

	public void setCanteens(List<Canteen> items) {
		this.canteens = canteens;
	}
}
