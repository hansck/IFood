package com.tmpb.ifood.model.object;

/**
 * Created by Hans CK on 19-Feb-18.
 */

public enum OrderStatus {

	OPEN, CANCELLED, IN_PROGRESS, COMPLETED, REJECTED;

	private static String[] title = {"Open", "Cancelled", "In Progress", "Completed", "Rejected"};

	public static OrderStatus fromInt(int x) {
		try {
			return OrderStatus.values()[x];
		} catch (Exception e) {
			return null;
		}
	}

	public static int toInt(OrderStatus t) {
		try {
			switch (t) {
				case OPEN:
					return 0;
				case CANCELLED:
					return 1;
				case IN_PROGRESS:
					return 2;
				case COMPLETED:
					return 3;
				case REJECTED:
					return 4;
				default:
					return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getTitle(int i) {
		try {
			return title[i];
		} catch (Exception e) {
			return "";
		}
	}

	public static String getTitleFromStatus(OrderStatus status) {
		try {
			return title[toInt(status)];
		} catch (Exception e) {
			return "";
		}
	}
}
