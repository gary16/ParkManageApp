package com.zoway.parkmanage.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
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

public class PaybillActivity extends BaseActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private Button btnsure4bill;
	private Button btnsure4escape;
	private Button btnsure4print;
	private Button btnsure4ingore;
	private String hphm;
	private Date parktime;
	private Date leavetime;
	private float fare;
	private int tid;
	private String rcno;
	private ProgressDialog pDia;

	private Printer.Progress progress = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
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
			printer.printText("车牌号码:" + hphm + "\n");
			printer.feedLine(1);
			printer.printText("停车位置:南源路\n");
			printer.feedLine(1);
			printer.printText("停车时间:" + sdf.format(parktime) + "\n");
			printer.feedLine(1);
			printer.printText("离开时间:" + sdf.format(leavetime) + "\n");
			printer.feedLine(1);

			Date d1 = parktime;
			Date d2 = leavetime;
			long diff = d2.getTime() - d1.getTime();
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
			DbHelper.setPayRecord(tid, rcno, hphm, fare);
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void onCrash() {
			// TODO Auto-generated method stub
		}
	};

	private Printer.Progress progress2 = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String datetext = sdf.format(parktime);
			printer.printText("        路边停车凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.printText("商户名称:" + LoginBean4Wsdl.getCompanyName() + "\n");
			printer.printText("电话号码:26337118\n");
			printer.printText("车牌号码:" + hphm + "\n");
			printer.printText("停车位置:" + LoginBean4Wsdl.getParkName() + "\n");
			printer.printText("停车时间:" + datetext + "\n");
			printer.printText("操作员:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.printText("敬爱的车主，请使用微信扫描下方二维码查询停车时长。");
			printer.printText("\n\n");

			String cUrl = String.format(
					"http://cx.zoway.com.cn:81/ParkRecord/show/%s.do", rcno);
			printer.printQrCode(35, new QrCode(cUrl, QrCode.ECLEVEL_M), 312);
			printer.feedLine(4);
		}

		@Override
		public void onFinish(int arg0) {
			// TODO Auto-generated method stub
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
						PayListsActivity.class);
				PaybillActivity.this.startActivity(intent);
				PaybillActivity.this.finish();
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
		setContentView(R.layout.activity_paybill);
		Intent intent = this.getIntent();
		hphm = intent.getStringExtra("hphm");
		tid = intent.getIntExtra("tid", 0);
		rcno = intent.getStringExtra("rcno");
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnsure4escape = (Button) this.findViewById(R.id.btnsure4escape);
		btnsure4print = (Button) this.findViewById(R.id.btnsure4print);
		btnsure4ingore = (Button) this.findViewById(R.id.btnsure4ingore);
		txtcarnumber.setText(hphm);

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
		txtpark.setText("南源路");

		leavetime = TimeUtil.getTime();
		txtleavetime.setText(sdf.format(leavetime));
		Thread t1 = new Thread(new LeaveThread());
		t1.start();

		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pDia = ProgressDialog.show(PaybillActivity.this, "打印收费回执",
						"正在打印中", true, false);
				try {
					progress.start();
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
				pDia = ProgressDialog.show(PaybillActivity.this, "打印停车凭条",
						"正在打印中", true, false);
				try {
					progress2.start();
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, PayListsActivity.class);
		this.startActivity(ii);
		this.finish();
	}

}