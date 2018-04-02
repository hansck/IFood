package com.tmpb.ifood.util.manager;

import com.tmpb.ifood.model.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans CK on 19-Feb-18.
 */

public class MenuManager {

	private List<Menu> menus = new ArrayList<>();

	private static final MenuManager instance = new MenuManager();

	private MenuManager() {

	}

	public static MenuManager getInstance() {
		return instance;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
}
