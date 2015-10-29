package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.LeaveBean4Wsdl;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.LeaveWsdl;

public class PaybillActivity extends Activity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private Button btnsure4bill;
	private String hphm;
	private String parkingtime;
	private String leavetime;
	private float fare;
	private int tid;
	private String rcno;
	private ProgressDialog pDia;
	private Printer.Progress progress = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			format.setAscSize(Format.ASC_DOT5x7);
			format.setAscScale(Format.ASC_SC1x2);
			printer.setFormat(format);
			printer.printText("        收费凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.feedLine(1);
			printer.printText("车牌号码:粤" + hphm + "\n");
			printer.feedLine(1);
			printer.printText("停车位置:南源路\n");
			printer.feedLine(1);
			printer.printText("停车时间:" + parkingtime + "\n");
			printer.feedLine(1);
			printer.printText("离开时间:" + leavetime + "\n");
			printer.feedLine(1);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			Date d1 = sdf.parse(parkingtime);
			Date d2 = sdf.parse(leavetime);
			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24))
					/ (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			printer.printText("停车时长:" + days + "日" + hours + "时" + minutes
					+ "分");
			printer.feedLine(1);
			printer.printText("停车费用:" + fare + "\n");
			printer.feedLine(1);
			printer.printText("操作员:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.feedLine(5);
		}

		@Override
		public void onFinish(int arg0) {
			// TODO Auto-generated method stub
			String ltt = leavetime.replace("年", "").replace("月", "")
					.replace("日", "").replace("时", "").replace("分", "")
					.replace(" ", "");
			DbHelper.setPayRecord(tid, rcno, hphm, fare, ltt, new Date());
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void onCrash() {
			// TODO Auto-generated method stub
		}
	};

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia.dismiss();
				Toast.makeText(PaybillActivity.this, "处理成功", Toast.LENGTH_LONG)
						.show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(PaybillActivity.this,
						MainActivity.class);
				PaybillActivity.this.startActivity(intent);
				break;
			case 2:
				pDia.dismiss();
				Toast.makeText(PaybillActivity.this, "处理不成功", Toast.LENGTH_LONG)
						.show();
				break;
			}

		}
	};

	public void runOnUiThreadDelayed(Runnable r, int delayMillis) {
		handler.postDelayed(r, delayMillis);
	}

	/*
	 * To gain control of the device service, you need invoke this method before
	 * any device operation.
	 */
	public void bindDeviceService() {
		try {
			DeviceService.login(this);
		} catch (RequestException e) {
			// Rebind after a few milliseconds,
			// If you want this application keep the right of the device service
			runOnUiThreadDelayed(new Runnable() {
				@Override
				public void run() {
					bindDeviceService();
				}
			}, 300);
			e.printStackTrace();
		} catch (ServiceOccupiedException e) {
			e.printStackTrace();
		} catch (ReloginException e) {
			e.printStackTrace();
		} catch (UnsupportMultiProcess e) {
			e.printStackTrace();
		}
	}

	/**
	 * Release the right of using the device.
	 */
	public void unbindDeviceService() {
		DeviceService.logout();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paybill);
		ActivityList.pushActivity(this);
		Intent intent = this.getIntent();
		hphm = intent.getStringExtra("hphm");
		tid = intent.getIntExtra("tid", 0);
		parkingtime = intent.getStringExtra("rt");
		rcno = intent.getStringExtra("rcno");
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		txtcarnumber.setText(hphm);
		txtparktime.setText(parkingtime);
		txtpark.setText("南源路");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		leavetime = sdf.format(new Date());
		txtleavetime.setText(leavetime);
		Thread t1 = new Thread(new LeaveThread());
		t1.start();
		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pDia = ProgressDialog.show(PaybillActivity.this, "打印停车纸",
						"正在打印中", true, false);
				try {
					progress.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		unbindDeviceService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindDeviceService();
	}

}
