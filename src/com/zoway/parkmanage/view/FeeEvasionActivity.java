package com.zoway.parkmanage.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.zoway.parkmanage.image.BitmapHandle;

public class FeeEvasionActivity extends BaseActivity {

	private ImageButton btnTakeEvapto;
	private Button btnsure4bill;
	private Button btnprintqcode;
	private int tid;
	private String hphm;
	private Date parktime;
	private String recordno;
	private String img_path;
	private final int REQIMG = 0x12345678;
	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private ProgressDialog pDia;

	private Printer.Progress progress = new Printer.Progress() {

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
			printer.printText("停车位置:南源路\n");
			printer.printText("停车时间:" + datetext + "\n");
			printer.printText("操作员:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.printText("敬爱的车主，请使用微信扫描下方二维码查询停车时长。");
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
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia.dismiss();
				Toast.makeText(FeeEvasionActivity.this, "打印成功",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(FeeEvasionActivity.this,
						UnhandledListActivity.class);
				FeeEvasionActivity.this.startActivity(intent);
				break;
			case 2:
				pDia.dismiss();
				Toast.makeText(FeeEvasionActivity.this, "打印不成功",
						Toast.LENGTH_LONG).show();
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
		setContentView(R.layout.activity_fee_evasion);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		btnTakeEvapto = (ImageButton) this.findViewById(R.id.btnTakeEvapto);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnprintqcode = (Button) this.findViewById(R.id.btnprintqcode);
		Intent ii = this.getIntent();
		hphm = ii.getStringExtra("hphm");
		parktime = (Date) ii.getSerializableExtra("parktime");
		img_path = ii.getStringExtra("fname");
		recordno = ii.getStringExtra("recordno");
		tid = ii.getIntExtra("tid", 0);
		txtcarnumber.setText(hphm);
		txtpark.setText("南源路");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		txtparktime.setText(sdf.format(parktime));

		btnTakeEvapto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(img_path, "p4ori.jpg")));
				startActivityForResult(intent, REQIMG);

			}
		});
		btnsure4bill.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbHelper.setEscapeRecord(tid, recordno, hphm, new Date());
				Toast.makeText(FeeEvasionActivity.this, "修改逃费成功",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(FeeEvasionActivity.this,
						UnhandledListActivity.class);
				FeeEvasionActivity.this.startActivity(i);
			}
		});
		btnprintqcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				pDia = ProgressDialog.show(FeeEvasionActivity.this, "打印凭条",
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, UnhandledListActivity.class);
		this.startActivity(ii);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;
		else {
			switch (requestCode) {
			case REQIMG:
				try {
					InputStream is = getContentResolver().openInputStream(
							Uri.fromFile(new File(img_path, "p4ori.jpg")));
					Bitmap bitmapSelected1 = BitmapHandle.getReduceBitmap(is,
							false, 5, 90);
					this.btnTakeEvapto.setImageBitmap(bitmapSelected1);
					BitmapHandle.writeJpgFromBitmap(img_path + File.separator
							+ "p4.jpg", bitmapSelected1, 90);
					File f = new File(img_path + File.separator + "p4ori.jpg");
					if (f.exists()) {
						f.delete();
					}
					if (bitmapSelected1 != null) {
						bitmapSelected1 = null;
					}
				} catch (Exception er) {
					er.printStackTrace();
				}

				break;
			default:
				break;
			}
			System.gc();
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private Bitmap getReduceBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inJustDecodeBounds = false;

			options.inSampleSize = 5;
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fee_evasion, menu);
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
