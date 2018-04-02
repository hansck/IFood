package com.tmpb.ifood.model;

import java.util.Comparator;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class User {

	private String key;
	private String email;
	private String name;
	private String phone;

	public User() {

	}

	public User(String email, String name, String phone) {
		this.email = email;
		this.name = name;
		this.phone = phone;
	}

	public User(String key, String email, String name, String phone) {
		this.key = key;
		this.email = email;
		this.name = name;
		this.phone = phone;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static Comparator<User> sortAscending = new Comparator<User>() {
		@Override
		public int compare(User lhs, User rhs) {
			return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
		}
	};
}
