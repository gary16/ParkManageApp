package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;

public class QueryListsActivity extends BaseActivity {

	private final SparseArray<ParkRecord> groups = new SparseArray<ParkRecord>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;
	private Button btnpayfees;
	private Button btnescapefees;
	private EditText edtquery;
	private String qryStr = "";
	private int qryStrLen = 0;
	private int curFlg = 1;
	private LayoutInflater mInflater;

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
		setContentView(R.layout.activity_query_lists);
		lview = (ExpandableListView) this.findViewById(R.id.reclist);
		mInflater = this.getLayoutInflater();
		List<ParkRecord> li = DbHelper.queryRecordList("1", 100, "asc", null);
		for (int i = 0; i < li.size(); i++) {
			groups.append(i, li.get(i));
		}

		lview.setGroupIndicator(null);
		madapter = new MyExpandableListAdapter();
		lview.setAdapter(madapter);
		lview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				ParkRecord rec = groups.get(groupPosition);
				if (curFlg == 1) {
					Intent i = new Intent(QueryListsActivity.this,
							RecordInfoActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("pr", rec);
					i.putExtras(b);
					QueryListsActivity.this.startActivity(i);
				}
				return false;
			}
		});
		btnpayfees = (Button) this.findViewById(R.id.btnpayfees);
		btnpayfees.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curFlg == 2) {
					groups.clear();
					String curStr = edtquery.getText().toString();
					List<ParkRecord> li = DbHelper.queryRecordList("1", 100,
							"asc", curStr);
					for (int i = 0; i < li.size(); i++) {
						groups.append(i, li.get(i));
					}

					madapter = new MyExpandableListAdapter();
					lview.setAdapter(madapter);
					curFlg = 1;
					btnpayfees.setBackgroundColor(0xffcccc99);
					btnpayfees.setTextColor(0xff777777);
					btnescapefees.setBackgroundColor(0xfff9c164);
					btnescapefees.setTextColor(0xffffffff);
				}
			}
		});

		btnescapefees = (Button) this.findViewById(R.id.btnescapefees);
		btnescapefees.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curFlg == 1) {
					groups.clear();
					String curStr = edtquery.getText().toString();
					List<ParkRecord> li = DbHelper.queryRecordList("2", 100,
							"asc", curStr);
					for (int i = 0; i < li.size(); i++) {
						groups.append(i, li.get(i));
					}

					madapter = new MyExpandableListAdapter();
					lview.setAdapter(madapter);
					curFlg = 2;
					btnescapefees.setBackgroundColor(0xffcccc99);
					btnescapefees.setTextColor(0xff777777);
					btnpayfees.setBackgroundColor(0xfff9c164);
					btnpayfees.setTextColor(0xffffffff);
				}
			}
		});
		edtquery = (EditText) this.findViewById(R.id.edtquery);
		edtquery.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					String curStr = edtquery.getText().toString().toUpperCase();
					if (!curStr.equals(qryStr)) {
						groups.clear();
						String status = curFlg + "";
						List<ParkRecord> li = DbHelper.queryRecordList(status,
								100, "asc", curStr);
						for (int i = 0; i < li.size(); i++) {
							groups.append(i, li.get(i));
						}

						madapter = new MyExpandableListAdapter();
						lview.setAdapter(madapter);
						qryStr = curStr;
						qryStrLen = qryStr.length();
					}
					edtquery.clearFocus();
					return true;
				} else {
					return false;
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview_hphm, null);
				holder = new ViewHolder();
				holder.tv1 = (TextView) convertView
						.findViewById(R.id.list_txthphm);
				holder.tv2 = (TextView) convertView
						.findViewById(R.id.list_txtparktime);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String txt = groups.get(groupPosition).getHphm().toUpperCase();
			SpannableString hphmsp = new SpannableString(txt);

			if (!qryStr.equals("")) {
				int idx = txt.indexOf(qryStr);
				if (idx > -1) {
					hphmsp.setSpan(new ForegroundColorSpan(Color.RED), idx, idx
							+ qryStrLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}

			holder.tv1.setText(hphmsp);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH時mm分");
			holder.tv2.setText("停车时间:"
					+ sdf.format(groups.get(groupPosition).getParktime()));
			return convertView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}
	}

	private static class ViewHolder {
		public TextView tv1;
		public TextView tv2;
	}

}
