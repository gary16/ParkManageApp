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
import android.widget.RelativeLayout;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_lists);

		groups.clear();
		List<ParkRecord> li = DbHelper.queryInOrOut30Min(1, 50, null);
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
				long l1 = TimeUtil.getTime().getTime();
				long l2 = rec.getParktime().getTime();
				Intent intent = new Intent();
				intent.putExtra("hphm", rec.getHphm());
				Bundle b1 = new Bundle();
				b1.putSerializable("parktime", rec.getParktime());
				intent.putExtras(b1);
				intent.putExtra("tid", rec.getTid());
				intent.putExtra("rcno", rec.getRecordno());
				intent.putExtra("fname", rec.getFilepath());
				intent.putExtra("hphm", rec.getHphm());
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
				String rt = sdf1.format(rec.getParktime());
				intent.putExtra("rt", rt);
				intent.setClass(PayListsActivity.this, PaybillActivity.class);
				PayListsActivity.this.startActivity(intent);
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
						List<ParkRecord> li = DbHelper.queryInOrOut30Min(1, 50,
								curStr);
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

			RelativeLayout rl = new RelativeLayout(PayListsActivity.this);
			rl.setId(1);
			rl.setBackgroundResource(R.drawable.mainlistline);

			TextView tv1 = new TextView(PayListsActivity.this);
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp1.setMargins(5, 5, 0, 2);
			String txt = groups.get(groupPosition).getHphm().toUpperCase();
			SpannableString hphmsp = new SpannableString(txt);

			if (!qryStr.equals("")) {
				int idx = txt.indexOf(qryStr);
				if (idx > -1) {
					hphmsp.setSpan(new ForegroundColorSpan(Color.RED), idx, idx
							+ qryStrLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			tv1.setText(hphmsp);
			tv1.setTextSize(20);
			tv1.setId(2);
			tv1.setLayoutParams(lp1);
			TextView tv2 = new TextView(PayListsActivity.this);
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
			tv2.setTextColor(Color.rgb(148, 81, 68));
			tv2.setPadding(0, 0, 0, 8);
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
	}

}
