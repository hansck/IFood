package com.tmpb.ifood.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hans CK on 14-Feb-18.
 */

public class FirebaseDB {

	private static FirebaseDB instance = new FirebaseDB();
	private FirebaseDatabase db = FirebaseDatabase.getInstance();

	public FirebaseDB() {
		db.setPersistenceEnabled(true);
	}

	public static FirebaseDB getInstance() {
		return instance;
	}

	public DatabaseReference getDbReference(String ref) {
		return db.getReference(ref);
	}

	public String getKey(String ref) {
		return getDbReference(ref).push().getKey();
	}
}
