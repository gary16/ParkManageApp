package com.zoway.parkmanage.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
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
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
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

import com.bw.spdev.PosDevCallBackController;
import com.bw.spdev.RspCode;
import com.odm.misc.MiscDevice;
import com.odm.misc.MiscUtil;
import com.zoway.parkmanage.R;
import com.zoway.parkmanage.http.RegisterVehicleInfoWsdl;
import com.zoway.parkmanage.image.Bmp1BitsBytesProcuder;
import com.zoway.parkmanage.utils.PathUtils;
import com.zoway.parkmanage.utils.PosUtils;

public class InputInfoActivity extends Activity implements OnClickListener {

	public final int REQIMG1 = 0X01;
	public final int REQIMG2 = 0X02;
	public final int REQIMG3 = 0X03;
	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private String hphm = null;
	PrintThread rth = null;
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
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(Integer.parseInt(rcid));
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			PosUtils.sp.SpDevRelease();
			PowerOffDevice();
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
			PosUtils.sp.SpDevSetAppContext(InputInfoActivity.this);
			Thread tt1 = new Thread(new UploadDataThread());
			tt1.start();
			rth = new PrintThread();
			rth.startRunning();
			rth.start();
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
		rt = intent.getStringExtra("rt");
		hphm = intent.getStringExtra("hphm");
		img_path = img_path + File.separator + rcid + File.separator;
		File fDir = new File(img_path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		img1 = (ImageButton) this.findViewById(R.id.parkimg1);
		img2 = (ImageButton) this.findViewById(R.id.parkimg2);
		img3 = (ImageButton) this.findViewById(R.id.parkimg3);
		infosure = (Button) this.findViewById(R.id.btninfosure);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtparktime.setText(rt);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		infosure.setOnClickListener(this);

		PowerOnDevice();
		PosUtils.InitDev();
		PosUtils.sys.SetCmdSendMaxWT(1000);
		PosUtils.sys.SetRspRecvMaxWT(1000);
		if (PosUtils.sys.SysGetSpVersion() == null) {
		} else {
			PosUtils.sys.SysUnLockFunction();
			PosUtils.sys.SetCmdSendMaxWT(1000);
			PosUtils.sys.SetRspRecvMaxWT(10000);
		}
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
				break;
			case REQIMG2:
				bitmapSelected2 = getReduceBitmap(Uri.fromFile(new File(
						img_path, "p2.jpg")));
				this.img2.setImageBitmap(bitmapSelected2);
				break;
			case REQIMG3:
				bitmapSelected3 = getReduceBitmap(Uri.fromFile(new File(
						img_path, "p3.jpg")));
				this.img3.setImageBitmap(bitmapSelected3);
				break;
			default:
				break;
			}
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

	void toPrintBmp() {
		PosUtils.sp.SpDevSetAppContext(getApplicationContext());
		// set the callback
		PosUtils.printer.Init(new DeviceResponseHandlerImpl(),
				getApplicationContext());
		PosUtils.printer.ClearPrintData();// clean the data
		PosUtils.printer.SetStep(1000);// set the step
		PosUtils.printer.SetPrinterPara((short) 1250);// set the gray
		Bmp1BitsBytesProcuder bp = new Bmp1BitsBytesProcuder();

		byte[] ptr1 = bp.create352pixQRImage(String.format(
				"http://cx.zoway.com.cn/Pay/detail/%s.do", rcno));
		PosUtils.printer.SetProperty(1);// 粗体
		PosUtils.printer.SetFontSize(30);
		PosUtils.printer.AddString("             路边停车收费凭条\n");
		PosUtils.printer.SetProperty(0);
		PosUtils.printer.SetFontSize(26);
		PosUtils.printer.AddString("\n");
		PosUtils.printer.AddString("商户名称:测试商户\n");
		PosUtils.printer.AddString("车牌号码:粤X12345\n");
		PosUtils.printer.AddString("停车位置:测试路段第" + sno + "车位\n");
		PosUtils.printer.AddString("停车时间:" + rt + "\n");
		PosUtils.printer.AddString("操作员:测试人员\n\n");
		PosUtils.printer
				.AddString("亲爱的车主，为了节约你宝贵的时间，支付停车款请用微信扫描以下二维码。 公众号添加和使用方法请查看凭条背面。");
		PosUtils.printer.AddBmpData(ptr1, ptr1.length, 20, 530);
		PosUtils.printer.Print();

	}

	class DeviceResponseHandlerImpl extends PosDevCallBackController {
		@Override
		public int onPrinterPrint(int status) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

				}
			});

			switch (status) {
			case RspCode.RSPOK:
				Log.e(null, "print finish");
				break;
			case RspCode.RET_PRINTER_ERR_COMERR:
				Log.e(null, "RET_PRINTER_ERR_COMERR");
				break;
			case RspCode.RET_PRINTER_ERR_BMPLOST:
				Log.e(null, "RET_PRINTER_ERR_BMPLOST");
				break;
			case RspCode.RET_PRINTER_ERR_NOPAPER:
				Log.e(null, "RET_PRINTER_ERR_NOPAPER");
				break;
			case RspCode.RET_PRINTER_ERR_HT:
				Log.e(null, "RET_PRINTER_ERR_HT");
				break;

			case RspCode.RET_PRINTER_ERR_OTHER:
				Log.e(null, "RET_PRINTER_ERR_OTHER");
				break;
			default:
				break;
			}
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
			return 0;
		}
	}

	public byte[] readFromRaw() {
		String res = "";
		try {

			InputStream in = getResources().openRawResource(R.raw.test2);

			int length = in.available();

			byte[] buffer = new byte[length];

			in.read(buffer);

			in.close();
			return buffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private class UploadDataThread implements Runnable {

		@Override
		public void run() {
			RegisterVehicleInfoWsdl rv = new RegisterVehicleInfoWsdl();
			rv.uploadData(Integer.parseInt(rcid), 2, 23, "小型汽车", "粤X123456");
		}
	}

	private class PrintThread extends Thread {
		private boolean mStopRunning = false;

		// private AlertDialog show;
		public void stopRunning() {
			mStopRunning = true;
		}

		public void startRunning() {
			mStopRunning = false;
		}

		public void run() {
			Looper.prepare();
			super.run();

			// toPrint();
			// or
			toPrintBmp();

			Looper.loop();
			rth = null;

		}
	};

	void PowerOnDevice() {
		Class<?> c;

		String postty = null;
		try {

			// Log.e(null,"1");
			c = Class.forName("android.os.SystemProperties");

			Method get = c.getMethod("get", String.class);
			postty = (String) get.invoke(c, "ro.config.postty");

			if (postty == null || postty.isEmpty() == true) {
				PosUtils.misPos = new MiscDevice(MiscUtil.POS_MISC_DEV,
						MiscUtil.POS_MISC_IO);
				PosUtils.misPos.setPinHigh(MiscUtil.POS_PIN_PWR);
				PosUtils.misPos.setPinHigh(MiscUtil.POS_PIN_PWD);
				return;
			} else if (postty.length() == 0) {
				PosUtils.misPos = new MiscDevice(MiscUtil.POS_MISC_DEV,
						MiscUtil.POS_MISC_IO);
				PosUtils.misPos.setPinHigh(MiscUtil.POS_PIN_PWR);
				PosUtils.misPos.setPinHigh(MiscUtil.POS_PIN_PWD);
				return;

			} else {
				return;
			}
		} catch (Exception e) {

		}
	}

	void PowerOffDevice() {
		Class<?> c;

		String postty = null;
		try {

			// Log.e(null,"1");
			c = Class.forName("android.os.SystemProperties");

			Method get = c.getMethod("get", String.class);
			postty = (String) get.invoke(c, "ro.config.postty");

			if (postty == null || postty.isEmpty() == true) {

				PosUtils.misPos.setPinLow(MiscUtil.POS_PIN_PWR);
				PosUtils.misPos.setPinLow(MiscUtil.POS_PIN_PWD);

				return;
			} else if (postty.length() == 0) {
				PosUtils.misPos.setPinLow(MiscUtil.POS_PIN_PWR);
				PosUtils.misPos.setPinLow(MiscUtil.POS_PIN_PWD);
				return;

			} else {
				return;
			}
		} catch (Exception e) {

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

}
