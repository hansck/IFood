package com.tmpb.ifood.util;

import android.Manifest;

public class Constants {

	public static class General {
		public static String PREFERENCE = "prefs";
		public static String IMAGE = "image";
	}

	public static class APIKey {
		public static String PASSWORD = "password";
		public static String PHONE = "phone";
	}

	public static class User {
		public static String KEY = "key";
		public static String USER = "user";
		public static String ROLE = "role";
		public static String ADMIN = "admin";
		public static String MEMBER = "member";
		public static String NAME = "name";
		public static String EMAIL = "email";
		public static String PHOTO = "photo";
		public static String PHONE = "phone";
		public static String VERIFICATION_CODE = "verificationCode";
	}

	public static class News {
		public static String NEWS = "news";
		public static String REGISTRATION = "registration";
		public static String KEY = "newsKey";
	}

	public static class Storage {
		public static String IMAGES = "images";
	}

	public static class Permissions {
		public static final String[] CAMERA = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
	}

	public static class DateFormat {
		public static final String DATETIME = "yyyyMMddHHmmss";
		public static final String FULL_DATE = "EEEE, d MMMM yyyy";
		public static final String SHORT_DATE = "d/mm/yyyy";
	}
}