package com.zoway.parkmanage.view;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityList {

	private static ArrayList<Activity> ll = new ArrayList<Activity>();

	public static void pushActivity(Activity act) {
		ll.add(act);
	}

	public static void exitAllActivity() {
		for (Activity act : ll) {
			act.finish();
		}
		ll = new ArrayList<Activity>();
	}

}
