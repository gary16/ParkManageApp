package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.bean.ParkRecord;

public class RecordInfoActivity extends PrintActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private Button btnsure4print;
	private ProgressDialog pDia;
	private ParkRecord pr;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_info);
		Intent intent = this.getIntent();
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		btnsure4print = (Button) this.findViewById(R.id.btnsure4print);
		pr = (ParkRecord) this.getIntent().getSerializableExtra("pr");
		btnsure4print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					RecordInfoActivity.this.basePrinter.doPrint2(pr.getHphm(),
							pr.getParktime(), pr.getLeavetime(), pr.getFees());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		txtcarnumber.setText(pr.getHphm());
		txtpark.setText(LoginBean4Wsdl.getParkName());
		txtparktime.setText(sdf.format(pr.getParktime()));
		txtleavetime.setText(sdf.format(pr.getLeavetime()));
		txtmoney.setText(pr.getFees() + "元");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_info, menu);
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
		Intent intent = new Intent(RecordInfoActivity.this,
				QueryListsActivity.class);
		RecordInfoActivity.this.startActivity(intent);
		RecordInfoActivity.this.finish();
		return true;
	}

}
