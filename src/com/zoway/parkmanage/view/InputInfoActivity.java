package com.zoway.parkmanage.view;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
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
import com.zoway.parkmanage.utils.PathUtils;
import com.zoway.parkmanage.utils.TimeUtil;

public class InputInfoActivity extends PrintActivity implements OnClickListener {

	public final int REQIMG1 = 0X01;
	public final int REQIMG2 = 0X02;
	public final int REQIMG3 = 0X03;
	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private Date parkTime = null;
	private String hphm = null;
	private String hpzl = null;
	private String fn = null;
	private int type = 0;
	private String img_path = PathUtils.getSdPath();

	// size 48*48
	private ImageButton img1;
	private Button infosure;
	private Button btninfocancel;
	private Bitmap bitmapSelected1 = null;
	private Bitmap bitmapOcr = null;
	private ProgressDialog pDia;
	private TextView txtparktime;
	private TextView txtcarnumber;
	private TextView txtcartype;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.parkimg1:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img_path, "p1ori.jpg")));
			startActivityForResult(intent, REQIMG1);
			break;
		case R.id.btninfosure:

			try {
				InputStream is = getContentResolver().openInputStream(
						Uri.fromFile(new File(fn)));
				bitmapOcr = BitmapHandle.getReduceBitmap(is, false, 5, 0);
				BitmapHandle.writeJpgFromBitmap(img_path + File.separator
						+ "p2.jpg", bitmapOcr, 90);

				DbHelper.insertRecord(rcno, hphm, hpzl, "blue", parkTime,
						img_path, 0, 0, 0);
				File f = new File(fn);
				if (f.exists()) {
					f.delete();
				}
				this.basePrinter.doPrint(hphm, parkTime, rcno, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btninfocancel:
			this.onBackPressed();
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_info);
		Intent intent = this.getIntent();
		rcid = intent.getStringExtra("rcid");

		sno = intent.getStringExtra("sno");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		type = intent.getIntExtra("type", 0);
		if (type == 4) {
			rt = intent.getStringExtra("rt").replace(" ", "").replace("\t", "")
					.replace("-", "").replace(":", "");
			try {
				parkTime = sdf.parse(rt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rcno = intent.getStringExtra("rcno");
		} else {
			parkTime = TimeUtil.getTime();
			String uuid = java.util.UUID.randomUUID().toString();
			rcno = uuid;
		}
		hphm = intent.getStringExtra("hphm");
		hpzl = intent.getStringExtra("hpzl");
		fn = intent.getStringExtra("fn");

		img_path = img_path + File.separator + sdf.format(parkTime) + hphm
				+ File.separator;
		File fDir = new File(img_path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		img1 = (ImageButton) this.findViewById(R.id.parkimg1);
		infosure = (Button) this.findViewById(R.id.btninfosure);
		btninfocancel = (Button) this.findViewById(R.id.btninfocancel);

		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		txtparktime.setText(sdf1.format(parkTime));
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtcartype = (TextView) this.findViewById(R.id.txtcartype);
		txtcarnumber.setText(hphm);
		txtcartype.setText(hpzl);
		infosure.setOnClickListener(this);
		btninfocancel.setOnClickListener(this);

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
				try {
					InputStream is = getContentResolver().openInputStream(
							Uri.fromFile(new File(img_path, "p1ori.jpg")));
					bitmapSelected1 = BitmapHandle.getReduceBitmap(is, false,
							5, 90);
					this.img1.setScaleType(ScaleType.FIT_XY);
					this.img1.setImageBitmap(bitmapSelected1);
					BitmapHandle.writeJpgFromBitmap(img_path + File.separator
							+ "p1.jpg", bitmapSelected1, 90);
					File f = new File(img_path + File.separator + "p1ori.jpg");
					if (f.exists()) {
						f.delete();
					}

				} catch (Exception er) {
					er.printStackTrace();
				} finally {
				}
				break;
			default:
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		// unbindDeviceService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// bindDeviceService();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmapSelected1 != null && !bitmapSelected1.isRecycled()) {
			bitmapSelected1.recycle();
			bitmapSelected1 = null;

		}
		if (bitmapOcr != null && !bitmapOcr.isRecycled()) {
			bitmapOcr.recycle();
			bitmapOcr = null;
		}
		setContentView(R.layout.view_null);
		System.gc();
	}

	public boolean afterPrint() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(0xfedcba09);
		Intent intent = new Intent(InputInfoActivity.this, MainActivity.class);
		InputInfoActivity.this.startActivity(intent);
		InputInfoActivity.this.finish();
		return true;
	}

}
