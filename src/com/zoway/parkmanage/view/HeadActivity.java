package com.zoway.parkmanage.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zoway.parkmanage.R;

public class HeadActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_head);
		Intent i = this.getIntent();
		int status = i.getIntExtra("status", 0);
		if (status == 0) {
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
		} else {

			Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(MyIntent);

			ActivityManager am = (ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.killBackgroundProcesses("com.zoway.parkmanage");

			this.finish();

			System.exit(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.head, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
