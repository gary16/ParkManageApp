package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;

public class UnhandledListActivity extends Activity {

	private final SparseArray<ParkRecord> groups = new SparseArray<ParkRecord>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityList.pushActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_unhandled_list);
		groups.clear();
		List<ParkRecord> li = DbHelper.queryRecordList("0");
		for (int i = 0; i < li.size(); i++) {
			groups.append(i, li.get(i));
		}
		lview = (ExpandableListView) this.findViewById(R.id.reclist);
		lview.setGroupIndicator(null);
		madapter = new MyExpandableListAdapter();
		lview.setAdapter(madapter);
		lview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				ParkRecord rec = groups.get(groupPosition);
				Intent intent = new Intent(UnhandledListActivity.this,
						FeeEvasionActivity.class);
				intent.putExtra("hphm", rec.getHphm());
				Bundle b1 = new Bundle();
				b1.putSerializable("parktime", rec.getParktime());
				intent.putExtras(b1);
				intent.putExtra("tid", rec.getTid());
				intent.putExtra("recid", rec.getRecoidid());
				intent.putExtra("fname", rec.getFilepath());
				UnhandledListActivity.this.startActivity(intent);
				return false;
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unhandled_list, menu);
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

	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return 0;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			return null;
		}

		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		public int getGroupCount() {
			return groups.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			RelativeLayout rl = new RelativeLayout(UnhandledListActivity.this);
			rl.setId(1);
			rl.setBackgroundResource(R.drawable.mainlistline);

			TextView tv1 = new TextView(UnhandledListActivity.this);
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			tv1.setText(groups.get(groupPosition).getHphm());
			tv1.setTextSize(20);
			tv1.setId(2);

			TextView tv2 = new TextView(UnhandledListActivity.this);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp2.addRule(RelativeLayout.BELOW, tv1.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH時mm分");
			tv2.setText("停车时间:"
					+ sdf.format(groups.get(groupPosition).getParktime()));
			tv2.setTextSize(18);
			tv2.setId(3);
			tv2.setLayoutParams(lp2);

			rl.addView(tv1);
			rl.addView(tv2);

			return rl;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}

}
