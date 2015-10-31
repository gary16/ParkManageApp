package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.db.DbHelper;

public class FeeFreeActivity extends Activity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private Button btnsure4bill;
	private int tid;
	private String hphm;
	private Date parktime;
	private String recordno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityList.pushActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fee_free);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		Intent ii = this.getIntent();
		hphm = ii.getStringExtra("hphm");
		parktime = (Date) ii.getSerializableExtra("parktime");
		recordno = ii.getStringExtra("recordno");
		tid = ii.getIntExtra("tid", 0);
		txtcarnumber.setText("粤" + hphm);
		txtpark.setText("南源路");
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
						UnhandledListActivity.class);
				FeeFreeActivity.this.startActivity(i);
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
}
