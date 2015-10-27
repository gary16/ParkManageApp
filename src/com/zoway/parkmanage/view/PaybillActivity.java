package com.zoway.parkmanage.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.LeaveBean4Wsdl;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.LeaveWsdl;
import com.zoway.parkmanage.http.LoginWsdl;
import com.zoway.parkmanage.service.TerminalService;

public class PaybillActivity extends Activity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private String hphm;
	private ParkRecord pr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paybill);
		ActivityList.pushActivity(this);
		Intent intent = this.getIntent();
		hphm = intent.getStringExtra("hphm");
		pr = DbHelper.queryRecord(hphm);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		txtcarnumber.setText(hphm);
		txtpark.setText("南源路");
		txtpark.setp
	}

	private Handler leaveHdlr = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(PaybillActivity.this, "连接服务器中",
						"请稍候", true, false);
				break;
			case 2:
				pDia.dismiss();

				break;
			case 3:
				pDia.dismiss();
				Toast.makeText(PaybillActivity.this, "网络连接不佳，请检查网络连接",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	};

	private class LeaveThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg1 = new Message();
			msg1.what = 1;
			leaveHdlr.sendMessage(msg1);
			LeaveWsdl wsdl = new LeaveWsdl();
			LeaveBean4Wsdl lb = wsdl.whenCarLeave(pr.getRecordno(),
					LoginBean4Wsdl.getWorker().getWorkerId());
			int b = lb.getFlgflg();
			if (b == 0) {
				msg1 = new Message();
				msg1.what = 2;
				leaveHdlr.sendMessage(msg1);
			} else if (b == 1) {
				msg1 = new Message();
				msg1.what = 3;
				leaveHdlr.sendMessage(msg1);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paybill, menu);
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
