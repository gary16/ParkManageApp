package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;

public class OcrResultActivity extends BaseActivity implements OnClickListener {
	private Spinner mSpinner;
	private Spinner hSpinner;
	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	// 停泊时间
	private String rt = null;
	private String fn = null;
	private int type = 0;
	private String s;
	private ParkRecord pr;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnressure:
			String txtHphm = txtocrreshphm.getText().toString().toUpperCase();
			String txthpzl = hSpinner.getSelectedItem().toString();
			Pattern regex = Pattern
					.compile("^[A-HJ-Z][A-HJ-NP-Z0-9][A-HJ-NP-Z0-9][A-HJ-NP-Z0-9][A-HJ-NP-Z0-9][A-HJ-NP-Z0-9]$");
			Matcher mth = regex.matcher(txtHphm);
			if (mth.matches()) {
				txtHphm = mSpinner.getSelectedItem().toString() + txtHphm;
				if (type == 1 || type == 4) {
					pr = DbHelper.queryRecordByHphm(txtHphm);
					if (pr == null || pr.getStatus() != 0) {
						Intent intent = new Intent(this,
								InputInfoActivity.class);
						intent.putExtra("rcid", rcid);
						intent.putExtra("rcno", rcno);
						intent.putExtra("sno", sno);
						intent.putExtra("rt", rt);
						intent.putExtra("type", type);
						intent.putExtra("fn", fn);
						intent.putExtra("hpzl", txthpzl);
						intent.putExtra("hphm", txtHphm);
						this.startActivity(intent);
					} else {
						Toast.makeText(OcrResultActivity.this,
								"该车牌已有停车记录，请先处理", Toast.LENGTH_LONG).show();
					}
				} else if (type == 2) {
					pr = DbHelper.queryRecordByHphm(txtHphm);
					if (pr == null || pr.getStatus() != 0) {
						Toast.makeText(OcrResultActivity.this, "该车牌没有停车记录",
								Toast.LENGTH_LONG).show();
					} else {
						Intent intent = new Intent(this, PaybillActivity.class);
						intent.putExtra("rcid", rcid);
						intent.putExtra("rcno", pr.getRecordno());
						intent.putExtra("sno", sno);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						rt = sdf.format(pr.getParktime());
						intent.putExtra("rt", rt);
						intent.putExtra("type", type);
						intent.putExtra("fn", fn);
						intent.putExtra("tid", pr.getTid());
						intent.putExtra("hpzl", txthpzl);
						intent.putExtra("hphm", txtHphm);
						this.startActivity(intent);
					}
				}
			} else {
				Toast.makeText(OcrResultActivity.this, "请按正确格式输入车牌",
						Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.btnrescancel:
			Intent intent1 = new Intent(this, TakeOcrPhotoActivity.class);
			intent1.putExtra("rcid", rcid);
			intent1.putExtra("rcno", rcno);
			intent1.putExtra("sno", sno);
			intent1.putExtra("rt", rt);
			intent1.putExtra("fn", fn);
			intent1.putExtra("type", type);
			this.startActivity(intent1);
			this.finish();
			break;
		}

	}

	private Button btnressure;
	private Button btnrescancel;
	private EditText txtocrreshphm;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_ocr_result);
		Intent intent = this.getIntent();
		s = intent.getStringExtra("s");
		rcid = intent.getStringExtra("rcid");
		rcno = intent.getStringExtra("rcno");
		sno = intent.getStringExtra("sno");
		rt = intent.getStringExtra("rt");
		fn = intent.getStringExtra("fn");
		type = intent.getIntExtra("type", 0);
		btnressure = (Button) this.findViewById(R.id.btnressure);
		btnrescancel = (Button) this.findViewById(R.id.btnrescancel);
		txtocrreshphm = (EditText) this.findViewById(R.id.txtocrreshphm);
		txtocrreshphm.setText(s);
		btnressure.setOnClickListener(this);
		btnrescancel.setOnClickListener(this);
		mSpinner = (Spinner) this.findViewById(R.id.comshengfen);
		hSpinner = (Spinner) this.findViewById(R.id.comhpzl);
		this.initView();
		this.initView2();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocr_result, menu);
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

	private void initView() {
		String[] curs = getResources().getStringArray(R.array.shengfen);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.province_spinner, curs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
	}

	private void initView2() {
		String[] curs = getResources().getStringArray(R.array.hpzl);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.province_spinner, curs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		hSpinner.setAdapter(adapter);
	}

}
