package com.zoway.parkmanage.application;

import android.app.Application;

import com.zoway.parkmanage.exception.CrashHandler;

public class CrashApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

}
