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
import android.view.Window;
import android.view.WindowManager;
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
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.db.DbHelper;

public class FeeFreeActivity extends BaseActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private Button btnsure4bill;
	private Button btnprintqcode;
	private int tid;
	private String hphm;
	private Date parktime;
	private String recordno;
	private ProgressDialog pDia;
	private Printer.Progress progress = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��");
			String datetext = sdf.format(parktime);
			printer.printText("        ·��ͣ��ƾ��\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.printText("�̻�����:" + LoginBean4Wsdl.getCompanyName() + "\n");
			printer.printText("���ƺ���:" + hphm + "\n");
			printer.printText("ͣ��λ��:��Դ·\n");
			printer.printText("ͣ��ʱ��:" + datetext + "\n");
			printer.printText("����Ա:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.printText("�����ĳ�������ʹ��΢��ɨ���·���ά���ѯͣ��ʱ����");
			printer.printText("\n\n");

			String cUrl = String
					.format("http://cx.zoway.com.cn:81/ParkRecord/show/%s.do",
							recordno);
			printer.printQrCode(35, new QrCode(cUrl, QrCode.ECLEVEL_M), 312);
			printer.feedLine(5);
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
			// TODO ������Ϣ����ȥ����UI�߳��ϵĿؼ�����
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia.dismiss();
				Toast.makeText(FeeFreeActivity.this, "��ӡ�ɹ�", Toast.LENGTH_LONG)
						.show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(FeeFreeActivity.this,
						UnhandledListActivity.class);
				FeeFreeActivity.this.startActivity(intent);
				break;
			case 2:
				pDia.dismiss();
				Toast.makeText(FeeFreeActivity.this, "��ӡ���ɹ�", Toast.LENGTH_LONG)
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fee_free);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnprintqcode = (Button) this.findViewById(R.id.btnprintqcode);
		Intent ii = this.getIntent();
		hphm = ii.getStringExtra("hphm");
		parktime = (Date) ii.getSerializableExtra("parktime");
		recordno = ii.getStringExtra("recordno");
		tid = ii.getIntExtra("tid", 0);
		txtcarnumber.setText(hphm);
		txtpark.setText("��Դ·");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��");
		txtparktime.setText(sdf.format(parktime));
		txtleavetime.setText(sdf.format(new Date()));
		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbHelper.setPayRecord(tid, recordno, hphm, 0);
				Toast.makeText(FeeFreeActivity.this, "�޸���ѳɹ�",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(FeeFreeActivity.this,
						UnhandledListActivity.class);
				FeeFreeActivity.this.startActivity(i);
				FeeFreeActivity.this.finish();
			}
		});
		btnprintqcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				pDia = ProgressDialog.show(FeeFreeActivity.this, "��ӡƾ��",
						"���ڴ�ӡ��", true, false);
				try {
					progress.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fee_free, menu);
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
