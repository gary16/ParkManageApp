package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.service.TerminalService;

public class MainActivity extends BaseActivity {

	private ExpandableListView lview;
	private MainListAdapter madapter;
	private Button malogout;
	private ImageButton btnmainremark;
	private ImageButton btnmainpay;
	private ImageButton btnmainescape;
	private ImageButton btnmainquery;
	private TextView matxtlgtime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SparseArray<Group> groups = createData();
		malogout = (Button) this.findViewById(R.id.malogout);
		btnmainremark = (ImageButton) this.findViewById(R.id.btnmainremark);
		btnmainpay = (ImageButton) this.findViewById(R.id.btnmainpay);
		btnmainescape = (ImageButton) this.findViewById(R.id.btnmainescape);
		btnmainquery = (ImageButton) this.findViewById(R.id.btnmainquery);

		matxtlgtime = (TextView) this.findViewById(R.id.matxtlgtime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		Date df = Calendar.getInstance().getTime();
		matxtlgtime.setText(sdf.format(df));

		malogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancelAll();
				Intent ii = new Intent(MainActivity.this, TerminalService.class);
				MainActivity.this.stopService(ii);

				Intent MyIntent = new Intent(MainActivity.this,
						HeadActivity.class);
				MyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MyIntent.putExtra("status", 1);

				MainActivity.this.startActivity(MyIntent);

			}
		});

		btnmainremark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TakeOcrPhotoActivity.class);
				intent.putExtra("type", 1);
				MainActivity.this.startActivity(intent);
			}
		});
		btnmainpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(MainActivity.this,
				// TakeOcrPhotoActivity.class);
				// intent.putExtra("type", 2);
				Intent intent = new Intent(MainActivity.this,
						PayListsActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		btnmainescape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						UnhandledListActivity.class);
				intent.putExtra("type", 3);
				MainActivity.this.startActivity(intent);
			}
		});
		btnmainquery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						QueryListsActivity.class);
				intent.putExtra("fresh", 0);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public SparseArray<Group> createData() {

		SparseArray<Group> groups = new SparseArray<Group>();
		Group group = new Group("待拍照车辆", R.drawable.park);
		group.children.add("车位 123456,时间 8月30号15时15分");
		group.children.add("车位 123456,时间 8月30号15时15分");
		group.children.add("车位 123456,时间 8月30号15时15分");
		groups.append(0, group);

		group = new Group("被呼叫", R.drawable.called);
		groups.append(1, group);

		group = new Group("逃费管理", R.drawable.runfee);
		groups.append(2, group);

		group = new Group("设置", R.drawable.settings);
		groups.append(3, group);

		return groups;
	}

	public SparseArray<Group> createData(ArrayList<String[]> al) {

		SparseArray<Group> groups = new SparseArray<Group>();
		Group group = new Group("待拍照车辆", R.drawable.park);
		group.children.add("车位 123456,时间 8月30号15时15分");
		group.children.add("车位 123456,时间 8月30号15时15分");
		group.children.add("车位 123456,时间 8月30号15时15分");
		groups.append(0, group);

		group = new Group("被呼叫", R.drawable.called);
		groups.append(1, group);

		group = new Group("逃费管理", R.drawable.runfee);
		groups.append(2, group);

		group = new Group("设置", R.drawable.settings);
		groups.append(3, group);

		return groups;
	}

}
