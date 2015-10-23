package com.zoway.parkmanage.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.zoway.parkmanage.bean.LoginUser;
import com.zoway.parkmanage.http.RegisterVehicleInfoWsdl;
import com.zoway.parkmanage.utils.PathUtils;

public class InputInfoActivity extends Activity implements OnClickListener {

	public final int REQIMG1 = 0X01;
	public final int REQIMG2 = 0X02;
	public final int REQIMG3 = 0X03;
	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private String hphm = null;
	private String fn = null;
	private int type = 0;
	private String img_path = PathUtils.getSdPath();

	// size 48*48
	private ImageButton img1;
	private ImageButton img2;
	private ImageButton img3;
	private Button infosure;
	private Bitmap bitmapSelected1 = null;
	private Bitmap bitmapSelected2 = null;
	private Bitmap bitmapSelected3 = null;
	private ProgressDialog pDia;
	private TextView txtparktime;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia.dismiss();
				Toast.makeText(InputInfoActivity.this, "处理成功",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(InputInfoActivity.this,
						MainActivity.class);
				InputInfoActivity.this.startActivity(intent);
				break;
			case 2:

				break;
			}

		}
	};

	public void runOnUiThreadDelayed(Runnable r, int delayMillis) {
		handler.postDelayed(r, delayMillis);
	}

	/**
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

	private Printer.Progress progress = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			format.setAscSize(Format.ASC_DOT5x7);
			format.setAscScale(Format.ASC_SC1x2);
			printer.setFormat(format);
			printer.printText("        管理员凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.printText("车牌号码:粤X12345\n");
			printer.printText("停车位置:南源路\n");
			printer.printText("停车时间:" + rt + "\n");
			printer.feedLine(10);
			printer.printText("        路边停车凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.printText("商户名称:测试商户\n");
			printer.printText("车牌号码:粤X12345\n");
			printer.printText("停车位置:南源路\n");
			printer.printText("停车时间:" + rt + "\n");
			printer.printText("操作员:" + LoginUser.getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.printText("亲爱的车主，为了节约你宝贵的时间，支付停车款请用微信扫描以下二维码。 公众号添加和使用方法请查看凭条背面。");
			printer.printText("\n\n");

			String cUrl = String.format(
					"http://cx.zoway.com.cn/Pay/detail/%s.do", rcno);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		switch (arg0.getId()) {
		case R.id.parkimg1:
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img_path, "p1.jpg")));
			startActivityForResult(intent, REQIMG1);
			break;
		case R.id.parkimg2:
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img_path, "p2.jpg")));
			startActivityForResult(intent, REQIMG2);
			break;
		case R.id.parkimg3:
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img_path, "p3.jpg")));
			startActivityForResult(intent, REQIMG3);
			break;
		case R.id.btninfosure:
			pDia = ProgressDialog.show(InputInfoActivity.this, "打印停车纸",
					"正在打印中", true, false);

			try {
				File f = new File(fn);
				if (f.exists()) {

				}
				progress.start();
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Thread tt1 = new Thread(new UploadDataThread());
			// tt1.start();

			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityList.pushActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_input_info);
		Intent intent = this.getIntent();
		rcid = intent.getStringExtra("rcid");
		rcno = intent.getStringExtra("rcno");
		sno = intent.getStringExtra("sno");

		type = intent.getIntExtra("type", 0);
		if (type == 4) {
			rt = intent.getStringExtra("rt");
		} else {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
			rt = sdf1.format(new Date());
		}
		hphm = intent.getStringExtra("hphm");
		fn = intent.getStringExtra("fn");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		img_path = img_path + File.separator + sdf.format(new Date()) + hphm
				+ File.separator;
		File fDir = new File(img_path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		img1 = (ImageButton) this.findViewById(R.id.parkimg1);
		img2 = (ImageButton) this.findViewById(R.id.parkimg2);
		infosure = (Button) this.findViewById(R.id.btninfosure);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtparktime.setText(rt);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		infosure.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_info, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;
		else {
			switch (requestCode) {
			case REQIMG1:
				bitmapSelected1 = getReduceBitmap(Uri.fromFile(new File(
						img_path, "p1.jpg")));
				this.img1.setImageBitmap(bitmapSelected1);
				if (bitmapSelected1 != null) {
					bitmapSelected1 = null;
				}
				break;
			case REQIMG2:
				bitmapSelected2 = getReduceBitmap(Uri.fromFile(new File(
						img_path, "p2.jpg")));
				this.img2.setImageBitmap(bitmapSelected2);
				if (bitmapSelected2 != null) {
					bitmapSelected2 = null;
				}
				break;
			case REQIMG3:
				bitmapSelected3 = getReduceBitmap(Uri.fromFile(new File(
						img_path, "p3.jpg")));
				this.img3.setImageBitmap(bitmapSelected3);
				if (bitmapSelected3 != null) {
					bitmapSelected3 = null;
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

	// void toPrintBmp() {
	// PosUtils.sp.SpDevSetAppContext(getApplicationContext());
	// // set the callback
	// PosUtils.printer.Init(new DeviceResponseHandlerImpl(),
	// getApplicationContext());
	// PosUtils.printer.ClearPrintData();// clean the data
	// PosUtils.printer.SetStep(1000);// set the step
	// PosUtils.printer.SetPrinterPara((short) 1250);// set the gray
	// Bmp1BitsBytesProcuder bp = new Bmp1BitsBytesProcuder();
	//
	// byte[] ptr1 = bp.create352pixQRImage(String.format(
	// "http://cx.zoway.com.cn/Pay/detail/%s.do", rcno));
	// PosUtils.printer.SetProperty(1);// 粗体
	// PosUtils.printer.SetFontSize(30);
	// PosUtils.printer.AddString("             路边停车收费凭条\n");
	// PosUtils.printer.SetProperty(0);
	// PosUtils.printer.SetFontSize(26);
	// PosUtils.printer.AddString("\n");
	// PosUtils.printer.AddString("商户名称:测试商户\n");
	// PosUtils.printer.AddString("车牌号码:粤X12345\n");
	// PosUtils.printer.AddString("停车位置:测试路段第" + sno + "车位\n");
	// PosUtils.printer.AddString("停车时间:" + rt + "\n");
	// PosUtils.printer.AddString("操作员:测试人员\n\n");
	// PosUtils.printer
	// .AddString("亲爱的车主，为了节约你宝贵的时间，支付停车款请用微信扫描以下二维码。 公众号添加和使用方法请查看凭条背面。");
	// PosUtils.printer.AddBmpData(ptr1, ptr1.length, 20, 530);
	// PosUtils.printer.Print();
	//
	// }

	private class UploadDataThread implements Runnable {
		@Override
		public void run() {
			RegisterVehicleInfoWsdl rv = new RegisterVehicleInfoWsdl();
			rv.uploadData(Integer.parseInt(rcid), 2, 23, "小型汽车", "粤X123456");
		}
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
