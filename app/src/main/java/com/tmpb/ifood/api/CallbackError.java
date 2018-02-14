package com.tmpb.ifood.api;

/**
 * Created by Hans CK on 6/23/2016.
 */
public abstract class CallbackError implements Runnable {
	private Throwable throwable;

	public void setThrowable(Throwable _throwable) {
		throwable = _throwable;
	}

	public Throwable getThrowable() {
		return throwable;
	}
}
