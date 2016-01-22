package com.zoway.parkmanage.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.landicorp.android.eptapi.utils.QrCode;
import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.LeaveBean4Wsdl;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.LeaveWsdl;
import com.zoway.parkmanage.utils.TimeUtil;

public class PaybillActivity extends PrintActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private TextView lblcartype;
	private Button btnsure4bill;
	private Button btnsure4escape;
	private Button btnsure4print;
	private Button btnsure4ingore;
	private String hphm;
	private String hpzl;
	private Date parktime;
	private Date leavetime;
	private float fare;
	private int tid;
	private String rcno;
	private ProgressDialog pDia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paybill);
		Intent intent = this.getIntent();
		hphm = intent.getStringExtra("hphm");
		hpzl = intent.getStringExtra("hpzl");
		tid = intent.getIntExtra("tid", 0);
		rcno = intent.getStringExtra("rcno");
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		lblcartype = (TextView) this.findViewById(R.id.lblcartype);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnsure4escape = (Button) this.findViewById(R.id.btnsure4escape);
		btnsure4print = (Button) this.findViewById(R.id.btnsure4print);
		btnsure4ingore = (Button) this.findViewById(R.id.btnsure4ingore);
		txtcarnumber.setText(hphm);
		lblcartype.setText(hpzl);
		String parktimetext = intent.getStringExtra("rt");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			parktime = sdf1.parse(parktimetext);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		txtparktime.setText(sdf.format(parktime));
		txtpark.setText( LoginBean4Wsdl.getParkName());

		leavetime = TimeUtil.getTime();
		txtleavetime.setText(sdf.format(leavetime));
		Thread t1 = new Thread(new LeaveThread());
		t1.start();

		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbHelper.setPayRecord(tid, rcno, hphm, fare);
				try {
					PaybillActivity.this.basePrinter.doPrint2(hphm, parktime,
							leavetime, fare);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnsure4escape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbHelper.setEscapeRecord(tid, rcno, hphm, new Date());
				Toast.makeText(PaybillActivity.this, "修改逃费成功",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(PaybillActivity.this,
						PayListsActivity.class);
				PaybillActivity.this.startActivity(i);
			}
		});

		btnsure4print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					PaybillActivity.this.basePrinter.doPrint(hphm, parktime,
							rcno);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnsure4ingore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbHelper.setIgnoreRecord(tid, rcno, hphm, new Date());
				Toast.makeText(PaybillActivity.this, "修改走费成功",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(PaybillActivity.this,
						PayListsActivity.class);
				PaybillActivity.this.startActivity(i);
			}
		});

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
				txtmoney.setText(msg.obj.toString() + "元");
				try {
					fare = Float.parseFloat(msg.obj.toString());
				} catch (Exception e) {
					fare = -1;
				}
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
			LeaveBean4Wsdl lb = wsdl.whenCarLeave(rcno, LoginBean4Wsdl
					.getWorker().getWorkerId());

			if (lb != null) {
				msg1 = new Message();
				msg1.what = 2;
				msg1.obj = lb.getFare();
				fare = lb.getFare();
				leaveHdlr.sendMessage(msg1);
			} else {
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, PayListsActivity.class);
		this.startActivity(ii);
		this.finish();
	}

	public boolean afterPrint() {
		Intent intent = new Intent(PaybillActivity.this, PayListsActivity.class);
		PaybillActivity.this.startActivity(intent);
		PaybillActivity.this.finish();
		return true;
	}

}