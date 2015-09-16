package com.zoway.parkmanage.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.LoginWsdl;
import com.zoway.parkmanage.service.TerminalService;

public class LoginActivity extends Activity implements OnClickListener {

	private TextView txtunam;
	private TextView txtpass;
	private Button btnloginsure;
	private Button btnlogincancel;

	private Handler loginHdlr = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(LoginActivity.this, "登录中", "请稍候",
						true, false);
				break;
			case 2:
				pDia.dismiss();
				Intent ii = new Intent(LoginActivity.this,
						TerminalService.class);
				ComponentName cnn = LoginActivity.this.startService(ii);

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);
				break;

			case 3:
				pDia.dismiss();
				Toast.makeText(LoginActivity.this, "用户名或者密码错误",
						Toast.LENGTH_LONG).show();
				break;
			case 4:
				pDia.dismiss();
				Toast.makeText(LoginActivity.this, "网络连接不佳，请检查网络连接",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	};

	private class LoginThread implements Runnable {

		private String devId = null;
		private String mgrId = null;
		private String pwd = null;

		public LoginThread() {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			devId = tm.getDeviceId();
			mgrId = LoginActivity.this.txtunam.getText().toString();
			pwd = LoginActivity.this.txtpass.getText().toString();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg1 = new Message();
			msg1.what = 1;
			loginHdlr.sendMessage(msg1);
			LoginWsdl lws = new LoginWsdl();
			// boolean b = lws.getLogin(devId, mgrId, pwd);
			int b = lws.getLogin("h", mgrId, pwd);
			if (b == 0) {
				msg1 = new Message();
				msg1.what = 2;
				loginHdlr.sendMessage(msg1);
			} else if (b == 1) {
				msg1 = new Message();
				msg1.what = 3;
				loginHdlr.sendMessage(msg1);
			} else if (b == 2) {
				msg1 = new Message();
				msg1.what = 4;
				loginHdlr.sendMessage(msg1);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnloginsure:
			if (txtunam.getText().equals("")) {
			} else if (txtpass.getText().equals("")) {
			} else {
				Thread t = new Thread(new LoginThread());
				t.start();
			}
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DbHelper.openDatabase(this);
		ActivityList.pushActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		btnloginsure = (Button) this.findViewById(R.id.btnloginsure);
		txtunam = (TextView) this.findViewById(R.id.txtunam);
		txtpass = (TextView) this.findViewById(R.id.txtpass);
		btnloginsure.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
