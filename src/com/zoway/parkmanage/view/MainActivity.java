package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.service.TerminalService;

public class MainActivity extends Activity {

	private ExpandableListView lview;
	private MainListAdapter madapter;
	private Button malogout;
	private TextView matxtlgtime;
//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityList.pushActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		SparseArray<Group> groups = createData();

		malogout = (Button) this.findViewById(R.id.malogout);
		lview = (ExpandableListView) this
				.findViewById(R.id.expandableListView1);

		lview.setGroupIndicator(null);
		madapter = new MainListAdapter(this, groups);
		lview.setAdapter(madapter);
		matxtlgtime = (TextView) this.findViewById(R.id.matxtlgtime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��");
		Date df = Calendar.getInstance().getTime();
		matxtlgtime.setText(sdf.format(df));
		lview.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {

			}
		});

		malogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancelAll();
				Intent ii = new Intent(MainActivity.this, TerminalService.class);
				MainActivity.this.stopService(ii);
				Intent MyIntent = new Intent(Intent.ACTION_MAIN);
				MyIntent.addCategory(Intent.CATEGORY_HOME);
				MainActivity.this.startActivity(MyIntent);
				DbHelper.closeDatabase();
				ActivityList.exitAllActivity();
				System.exit(0);
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
		Group group = new Group("�����ճ���", R.drawable.park);
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		groups.append(0, group);

		group = new Group("������", R.drawable.called);
		groups.append(1, group);

		group = new Group("�ӷѹ���", R.drawable.runfee);
		groups.append(2, group);

		group = new Group("����", R.drawable.settings);
		groups.append(3, group);

		return groups;
	}

	public SparseArray<Group> createData(ArrayList<String[]> al) {

		SparseArray<Group> groups = new SparseArray<Group>();
		Group group = new Group("�����ճ���", R.drawable.park);
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		group.children.add("��λ 123456,ʱ�� 8��30��15ʱ15��");
		groups.append(0, group);

		group = new Group("������", R.drawable.called);
		groups.append(1, group);

		group = new Group("�ӷѹ���", R.drawable.runfee);
		groups.append(2, group);

		group = new Group("����", R.drawable.settings);
		groups.append(3, group);

		return groups;
	}
}
