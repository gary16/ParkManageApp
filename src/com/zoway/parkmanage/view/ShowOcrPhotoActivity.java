package com.zoway.parkmanage.view;

import java.io.File;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.R.id;
import com.zoway.parkmanage.R.layout;
import com.zoway.parkmanage.R.menu;
import com.zoway.parkmanage.image.BitmapHandle;
import com.zoway.parkmanage.image.HphmRegonize;
import com.zoway.parkmanage.utils.LogUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

public class ShowOcrPhotoActivity extends Activity {

	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private Handler handler = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			Intent intent = new Intent(ShowOcrPhotoActivity.this,
					OcrResultActivity.class);
			intent.putExtra("rcid", rcid);
			intent.putExtra("rcno", rcno);
			intent.putExtra("sno", sno);
			intent.putExtra("rt", rt);
			String s = (String) msg.obj;
			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(ShowOcrPhotoActivity.this, "识别车牌图片",
						"正在识别中", true, false);
				break;
			case 2:
				pDia.dismiss();
				intent.putExtra("s", s);
				ShowOcrPhotoActivity.this.startActivity(intent);
				break;
			case 3:
				pDia.dismiss();
				intent.putExtra("s", "X12345");
				ShowOcrPhotoActivity.this.startActivity(intent);
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
	}

	private class DoOcrThread implements Runnable {
		private Bitmap b1;

		public DoOcrThread(Bitmap bb) {
			b1 = bb;
		}

		public void run() {

			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);

			try {
				BitmapHandle bh = new BitmapHandle();
				bh.setSrcBitmap(b1);
				// 预处理
				Bitmap bBinar = bh.preTreateImg();
				// 返回7个图片
				Bitmap[] bArr = bh.getAllChrBitmap(bBinar);
				StringBuilder sb1 = new StringBuilder("");
				if (bArr.length > 0) {
					for (int i = 1; i < bArr.length; i++) {
						if (bArr[i] != null) {
							HphmRegonize hr = new HphmRegonize();
							String ss1 = hr.doOcr2(bArr[i]);
							sb1.append(ss1);
							LogUtils.i(ShowOcrPhotoActivity.class, ss1);
						} else {
							sb1.append("?");
						}
					}
				}
				Message msg1 = new Message();
				msg1.what = 2;
				msg1.obj = sb1.toString();
				handler.sendMessage(msg1);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg2 = new Message();
				msg2.what = 3;
				handler.sendMessage(msg2);
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityList.pushActivity(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_ocr_photo);
		ImageView iv1 = (ImageView) this.findViewById(R.id.imageView1);

		Intent intent = this.getIntent();
		String fn = intent.getStringExtra("fn");
		rcid = intent.getStringExtra("rcid");
		rcno = intent.getStringExtra("rcno");
		sno = intent.getStringExtra("sno");
		rt = intent.getStringExtra("rt");
		File f = new File(fn);
		if (f.exists()) {
			Bitmap mBitmap = BitmapFactory.decodeFile(fn);
			Log.d("ShowOcrPhotoActivity",
					mBitmap.getWidth() + "..." + mBitmap.getHeight());
			Matrix matrix = new Matrix();
			matrix.postRotate(90.0f);
			Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
			// Bitmap rectBitmap = BitmapHandle.getRectBitmap(mBitmap, 0.625f,
			// 0.15f);
			iv1.setImageBitmap(rotaBitmap);
			mBitmap.recycle();
			mBitmap = null;
			Thread t1 = new Thread(new DoOcrThread(rotaBitmap));
			t1.start();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocr_cap_photo, menu);
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
