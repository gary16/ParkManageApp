package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;

public class QueryListsActivity extends Activity {

	private final SparseArray<ParkRecord> groups = new SparseArray<ParkRecord>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;
	private Button malogout;
	private Button btnunhandled;
	private Button btnhandled;
	private Button btnquery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_query_lists);

		lview = (ExpandableListView) this.findViewById(R.id.reclist);

		List<ParkRecord> li = DbHelper.queryRecordList("1");
		for (int i = 0; i < li.size(); i++) {
			groups.append(i, li.get(i));
		}

		lview.setGroupIndicator(null);
		madapter = new MyExpandableListAdapter();
		lview.setAdapter(madapter);
		malogout = (Button) this.findViewById(R.id.malogout);
		btnunhandled = (Button) this.findViewById(R.id.btnunhandled);
		btnunhandled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				groups.clear();
				List<ParkRecord> li = DbHelper.queryRecordList("1");
				for (int i = 0; i < li.size(); i++) {
					groups.append(i, li.get(i));
				}

				madapter = new MyExpandableListAdapter();
				lview.setAdapter(madapter);
			}
		});
		btnhandled = (Button) this.findViewById(R.id.btnhandled);
		btnhandled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				groups.clear();
				List<ParkRecord> li = DbHelper.queryRecordList("2");
				for (int i = 0; i < li.size(); i++) {
					groups.append(i, li.get(i));
				}

				madapter = new MyExpandableListAdapter();
				lview.setAdapter(madapter);
			}
		});
		btnquery = (Button) this.findViewById(R.id.btnquery);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query_lists, menu);
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
			return 1;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			RelativeLayout rl = new RelativeLayout(QueryListsActivity.this);

			View v1 = new View(QueryListsActivity.this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(0,
					0);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			v1.setId(1);
			v1.setLayoutParams(lp);

			Button btnprint = new Button(QueryListsActivity.this);
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp1.addRule(RelativeLayout.LEFT_OF, v1.getId());
			btnprint.setId(2);
			btnprint.setText("补打凭条");
			btnprint.setLayoutParams(lp1);

			Button btndetail = new Button(QueryListsActivity.this);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp2.addRule(RelativeLayout.RIGHT_OF, v1.getId());
			btndetail.setId(3);
			btndetail.setLayoutParams(lp2);
			btndetail.setText("查看详情");

			rl.addView(v1);
			rl.addView(btnprint);
			rl.addView(btndetail);

			return rl;
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
			RelativeLayout rl = new RelativeLayout(QueryListsActivity.this);
			rl.setBackgroundResource(R.drawable.mainlistline);

			TextView tv1 = new TextView(QueryListsActivity.this);
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			tv1.setText(groups.get(groupPosition).getHphm());
			tv1.setTextSize(20);
			tv1.setId(1);

			TextView tv2 = new TextView(QueryListsActivity.this);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp2.addRule(RelativeLayout.BELOW, tv1.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH時mm分");
			tv2.setText("停车时间:"
					+ sdf.format(groups.get(groupPosition).getParktime()));
			tv2.setTextSize(18);
			tv2.setId(2);
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
