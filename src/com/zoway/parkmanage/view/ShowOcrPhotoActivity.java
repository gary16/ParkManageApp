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
	private int nPlateLocate_Th;// ʶ����ֵ(ȡֵ��Χ0-9,5:Ĭ����ֵ0:����ɵ���ֵ9:���ϸ����ֵ)
	private int nOCR_Th;
	private int bIsAutoSlope;// �Ƿ�Ҫ��бУ��
	private int nSlopeDetectRange;// ��бУ���ķ�Χ(ȡֵ��Χ0-16)
	private int nContrast;// ������ָ��(ȡֵ��Χ0-9,��ģ��ʱ��Ϊ1;������ʱ��Ϊ9)
	private int bIsNight;// �Ƿ�ҹ��ģʽ��1�ǣ�0����
	private String szProvince;// ʡ��˳��
	private int individual;// �Ƿ������Ի�����:0�ǣ�1����
	private int tworowyellow;// ˫���ɫ�����Ƿ���:2�ǣ�3����
	private int armpolice;// �����侯�����Ƿ���:4�ǣ�5����
	private int tworowarmy;// ˫����ӳ����Ƿ���:6�ǣ�7����
	private int tractor; // ũ�ó������Ƿ���:8�ǣ�9����
	private int onlytworowyellow;// ֻʶ��˫������Ƿ���:10�ǣ�11����
	private int embassy;// ʹ�ݳ����Ƿ���:12�ǣ�13����
	private int onlylocation;// ֻ��λ�����Ƿ���:14�ǣ�15����
	private int armpolice2;// ˫���侯�����Ƿ���:16�ǣ�17����
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
				// "ʶ����ͼƬ",
				// "����ʶ����", true, false);
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
				// Toast.makeText(getApplicationContext(), "��֤��Ȩ���ʼ��ʧ��:" + nRet,
				// Toast.LENGTH_SHORT).show();

			} else {
				if (usepara == false) {
					recogBinder.setRecogArgu(lpFileName, imageformat,
							bGetVersion, bVertFlip, bDwordAligned);
				} else {
					System.out.println("usepara");
					// ����ʶ���õ��Ĳ���������ֱ�����ò�������ķ�ʽ����ʶ����������в���������鿴�ĵ�
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
			// ���ʶ�����
			if (recogBinder != null) {
				unbindService(recogConn);
			}
			// �û���ָ��lpFileNameʱɾ������ͼƬ
			if (null != pic && !pic.equals("")) {
				// System.out.println("null != lpFileName && !lpFileName.equals");
			} else {
				// System.out.println("lpFileName="+lpFileName);
				File picFile = new File(lpFileName);
				if (picFile.exists()) {
					picFile.delete();
				}
			}
			// ����ʶ����
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
