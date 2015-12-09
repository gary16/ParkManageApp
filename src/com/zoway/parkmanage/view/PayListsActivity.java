package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.zoway.parkmanage.utils.TimeUtil;

public class PayListsActivity extends BaseActivity {

	private final SparseArray<ParkRecord> groups = new SparseArray<ParkRecord>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;
	private EditText edtquery;
	private String qryStr = "";
	private int qryStrLen = 0;
	private Button btnocrpay;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_lists);
		mInflater = this.getLayoutInflater();
		groups.clear();
		List<ParkRecord> li = DbHelper
				.queryRecordList("0", 100, "desc", qryStr);
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

				long diff = TimeUtil.getTime().getTime()
						- rec.getParktime().getTime();
				Intent intent = new Intent();
				intent.putExtra("hphm", rec.getHphm());
				Bundle b1 = new Bundle();
				b1.putSerializable("parktime", rec.getParktime());
				intent.putExtras(b1);
				intent.putExtra("tid", rec.getTid());
				intent.putExtra("rcno", rec.getRecordno());
				intent.putExtra("recordno", rec.getRecordno());
				intent.putExtra("fname", rec.getFilepath());
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
				String rt = sdf1.format(rec.getParktime());
				intent.putExtra("rt", rt);
				if (diff > 1800000) {
					intent.setClass(PayListsActivity.this,
							PaybillActivity.class);
					PayListsActivity.this.startActivity(intent);

				} else {
					intent.setClass(PayListsActivity.this,
							FeeFreeActivity.class);
					PayListsActivity.this.startActivity(intent);

				}

				return false;
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
						List<ParkRecord> li = DbHelper.queryRecordList("0",
								100, "desc", curStr);
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

		btnocrpay = (Button) this.findViewById(R.id.btnocrpay);
		btnocrpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PayListsActivity.this,
						TakeOcrPhotoActivity.class);
				intent.putExtra("type", 2);
				PayListsActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_lists, menu);
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
				holder.tv3 = (TextView) convertView
						.findViewById(R.id.list_existtime);
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

			long diff = TimeUtil.getTime().getTime()
					- groups.get(groupPosition).getParktime().getTime();
			int tday = (int) diff / (86400000);
			int thour = (int) (diff / (3600000)) % 24;
			int tmin = (int) (diff / (60000)) % 60;
			String texist = String.format("%2d日%2d时%2d分", tday, thour, tmin)
					.replace(" ", "0");
			if (diff < 1800000) {
				holder.tv3.setTextColor(Color.rgb(0, 103, 48));
			} else {
				holder.tv3.setTextColor(Color.rgb(0xff, 0x42, 0x38));
			}

			holder.tv3.setText(texist);
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
		public TextView tv3;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
	}

}
