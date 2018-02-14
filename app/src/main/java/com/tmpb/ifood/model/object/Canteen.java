package com.tmpb.ifood.model.object;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class Canteen {

	private String key;
	private String name;
	private String location;
	private String schedule;
	private String picture;

	public Canteen(String key, String name, String location, String schedule, String picture) {
		this.key = key;
		this.name = name;
		this.location = location;
		this.schedule = schedule;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
