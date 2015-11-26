package com.zoway.parkmanage.view;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wintone.plateid.PlateCfgParameter;
import com.wintone.plateid.PlateRecognitionParameter;
import com.wintone.plateid.RecogService;
import com.zoway.parkmanage.R;
import com.zoway.parkmanage.image.BitmapHandle;
import com.zoway.parkmanage.image.HphmRegonize;
import com.zoway.parkmanage.utils.LogUtils;

public class ShowOcrPhotoActivity extends BaseActivity {

	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private String fn = null;
	private int type = 0;
	public RecogService.MyBinder recogBinder;
	public int iInitPlateIDSDK;
	public int nRet;
	private boolean usepara;
	private String lpFileName;
	private String pic;
	private String devcode;
	private String datefile;
	private int imageformat = 1;
	int bVertFlip = 0;
	int bDwordAligned = 1;
	boolean bGetVersion = false;
	private int ReturnAuthority = -1;
	private int nPlateLocate_Th;// 识别阈值(取值范围0-9,5:默认阈值0:最宽松的阈值9:最严格的阈值)
	private int nOCR_Th;
	private int bIsAutoSlope;// 是否要倾斜校正
	private int nSlopeDetectRange;// 倾斜校正的范围(取值范围0-16)
	private int nContrast;// 清晰度指数(取值范围0-9,最模糊时设为1;最清晰时设为9)
	private int bIsNight;// 是否夜间模式：1是；0不是
	private String szProvince;// 省份顺序
	private int individual;// 是否开启个性化车牌:0是；1不是
	private int tworowyellow;// 双层黄色车牌是否开启:2是；3不是
	private int armpolice;// 单层武警车牌是否开启:4是；5不是
	private int tworowarmy;// 双层军队车牌是否开启:6是；7不是
	private int tractor; // 农用车车牌是否开启:8是；9不是
	private int onlytworowyellow;// 只识别双层黄牌是否开启:10是；11不是
	private int embassy;// 使馆车牌是否开启:12是；13不是
	private int onlylocation;// 只定位车牌是否开启:14是；15不是
	private int armpolice2;// 双层武警车牌是否开启:16是；17不是
	String[] fieldvalue = new String[14];
	private Handler handler = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(ShowOcrPhotoActivity.this,
					OcrResultActivity.class);
			intent.putExtra("rcid", rcid);
			intent.putExtra("rcno", rcno);
			intent.putExtra("sno", sno);
			intent.putExtra("rt", rt);
			intent.putExtra("type", type);
			intent.putExtra("fn", fn);
			String s = (String) msg.obj;
			switch (msg.what) {
			case 1:
				// pDia = ProgressDialog.show(ShowOcrPhotoActivity.this,
				// "识别车牌图片",
				// "正在识别中", true, false);
				break;
			case 2:
				// pDia.dismiss();
				intent.putExtra("s", s);
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

	public ServiceConnection recogConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			recogConn = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			recogBinder = (RecogService.MyBinder) service;
			int inRet = recogBinder.getInitPlateIDSDK();
			// nRet = recogBinder.getInitPlateIDSDK();
			inRet = 0;
			if (inRet != 0) {
				// Toast.makeText(getApplicationContext(), "验证授权或初始化失败:" + nRet,
				// Toast.LENGTH_SHORT).show();

			} else {
				if (usepara == false) {
					recogBinder.setRecogArgu(lpFileName, imageformat,
							bGetVersion, bVertFlip, bDwordAligned);
				} else {
					System.out.println("usepara");
					// 设置识别用到的参数，采用直接设置参数对象的方式设置识别参数，所有参数定义请查看文档
					PlateCfgParameter cfgparameter = new PlateCfgParameter();
					cfgparameter.armpolice = armpolice;
					cfgparameter.armpolice2 = armpolice2;
					cfgparameter.bIsAutoSlope = bIsAutoSlope;
					cfgparameter.bIsNight = bIsNight;
					cfgparameter.embassy = embassy;
					cfgparameter.individual = individual;
					cfgparameter.nContrast = nContrast;
					cfgparameter.nOCR_Th = nOCR_Th;
					cfgparameter.nPlateLocate_Th = nPlateLocate_Th;
					cfgparameter.nSlopeDetectRange = nSlopeDetectRange;
					cfgparameter.onlylocation = onlylocation;
					cfgparameter.tworowyellow = tworowyellow;
					cfgparameter.tworowarmy = tworowarmy;
					if (szProvince == null)
						szProvince = "";
					cfgparameter.szProvince = szProvince;
					cfgparameter.onlytworowyellow = onlytworowyellow;
					cfgparameter.tractor = tractor;
					recogBinder.setRecogArgu(cfgparameter, imageformat,
							bVertFlip, bDwordAligned);
				}
				nRet = recogBinder.getnRet();
				// fieldvalue = recogBinder.doRecog(pic, width, height);
				PlateRecognitionParameter prp = new PlateRecognitionParameter();
				prp.dataFile = datefile;
				prp.devCode = devcode;
				prp.pic = lpFileName;
				fieldvalue = recogBinder.doRecogDetail(prp);
				nRet = recogBinder.getnRet();
			}
			// 解绑识别服务。
			if (recogBinder != null) {
				unbindService(recogConn);
			}
			// 用户不指定lpFileName时删除所拍图片
			if (null != pic && !pic.equals("")) {
				// System.out.println("null != lpFileName && !lpFileName.equals");
			} else {
				// System.out.println("lpFileName="+lpFileName);
				File picFile = new File(lpFileName);
				if (picFile.exists()) {
					picFile.delete();
				}
			}
			// 返回识别结果
			// Log.i(TAG, "pic="+pic);
			// Intent intentReturn = new Intent();
			// intentReturn.putExtra("ReturnAuthority", ReturnAuthority);
			// intentReturn.putExtra("nRet", nRet);
			// intentReturn.putExtra("ReturnLPFileName", lpFileName);
			// intentReturn.putExtra("fieldvalue", fieldvalue);
			// setResult(Activity.RESULT_OK, intentReturn);
			// finish();
			Message msg1 = new Message();
			msg1.what = 2;
			if (fieldvalue[0] != null) {
				int len = fieldvalue[0].length() > 7 ? 7 : fieldvalue[0]
						.length();
				msg1.obj = fieldvalue[0].substring(1, len);
			} else {
				msg1.obj = "?????";
			}
			handler.sendMessage(msg1);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int ii = 0;
		if (resultCode == Activity.RESULT_OK) {

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_ocr_photo);
		ImageView iv1 = (ImageView) this.findViewById(R.id.imageView1);

		Intent intent = this.getIntent();
		fn = intent.getStringExtra("fn");
		rcid = intent.getStringExtra("rcid");
		rcno = intent.getStringExtra("rcno");
		sno = intent.getStringExtra("sno");
		rt = intent.getStringExtra("rt");
		type = intent.getIntExtra("type", 0);
		File f = new File(fn);
		lpFileName = fn;
		pic = lpFileName;
		if (f.exists()) {
			Message msg1 = new Message();
			msg1.what = 1;
			handler.sendMessage(msg1);
			Intent recogIntent = new Intent(this, RecogService.class);
			bindService(recogIntent, recogConn, Service.BIND_AUTO_CREATE);
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
