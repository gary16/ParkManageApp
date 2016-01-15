package com.zoway.parkmanage.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// @Override
	// public void onAttachedToWindow() {
	// super.onAttachedToWindow();
	// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	// }
}
