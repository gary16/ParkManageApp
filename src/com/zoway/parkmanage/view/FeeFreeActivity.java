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

public class FeeFreeActivity extends PrintActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView lblcartype;
	private Button btnsure4bill;
	private Button btnprintqcode;
	private int tid;
	private String hphm;
	private String hpzl;
	private Date parktime;
	private String recordno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fee_free);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		lblcartype = (TextView) this.findViewById(R.id.lblcartype);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnprintqcode = (Button) this.findViewById(R.id.btnprintqcode);
		Intent ii = this.getIntent();
		hphm = ii.getStringExtra("hphm");
		hpzl = ii.getStringExtra("hpzl");
		parktime = (Date) ii.getSerializableExtra("parktime");
		recordno = ii.getStringExtra("recordno");
		tid = ii.getIntExtra("tid", 0);
		txtcarnumber.setText(hphm);
		lblcartype.setText(hpzl);
		txtpark.setText(LoginBean4Wsdl.getParkName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		txtparktime.setText(sdf.format(parktime));
		txtleavetime.setText(sdf.format(new Date()));
		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbHelper.setPayRecord(tid, recordno, hphm, 0);
				Toast.makeText(FeeFreeActivity.this, "修改免费成功",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(FeeFreeActivity.this,
						PayListsActivity.class);
				FeeFreeActivity.this.startActivity(i);
				FeeFreeActivity.this.finish();
			}
		});
		btnprintqcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				try {
					FeeFreeActivity.this.basePrinter.doPrint(hphm, parktime,
							recordno);
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

	public boolean afterPrint() {
		Intent intent = new Intent(FeeFreeActivity.this, PayListsActivity.class);
		FeeFreeActivity.this.startActivity(intent);
		FeeFreeActivity.this.finish();
		return true;
	}

}
