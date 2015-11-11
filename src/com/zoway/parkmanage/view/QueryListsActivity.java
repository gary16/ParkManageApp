package com.zoway.parkmanage.view;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
import com.zoway.parkmanage.db.DbHelper;

public class QueryListsActivity extends Activity {

	private final SparseArray<ParkRecord> groups = new SparseArray<ParkRecord>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;
	private Button btnpayfees;
	private Button btnescapefees;
	private EditText edtquery;
	private String qryStr = "";
	private int qryStrLen = 0;
	private int curFlg = 1;
	private ProgressDialog pDia;
	private ParkRecord pr;
	private View root_ly;

	private Printer.Progress progress = new Printer.Progress() {

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			// Use this 5x7 dot and 1 times width, 2 times height
			format.setAscSize(Format.ASC_DOT5x7);
			format.setAscScale(Format.ASC_SC1x2);
			printer.setFormat(format);
			printer.printText("        收费凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.feedLine(1);
			printer.printText("车牌号码:粤" + pr.getHphm() + "\n");
			printer.feedLine(1);
			printer.printText("停车位置:南源路\n");
			printer.feedLine(1);
			printer.printText("停车时间:" + sdf.format(pr.getParktime()) + "\n");
			printer.feedLine(1);
			printer.printText("离开时间:" + sdf.format(pr.getLeavetime()) + "\n");
			printer.feedLine(1);

			long diff = pr.getLeavetime().getTime()
					- pr.getParktime().getTime();
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24))
					/ (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			printer.printText("停车时长:" + days + "日" + hours + "时" + minutes
					+ "分");
			printer.feedLine(1);
			printer.printText("停车费用:" + pr.getFees() + "\n");
			printer.feedLine(1);
			printer.printText("操作员:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.feedLine(5);
		}

		@Override
		public void onFinish(int arg0) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public void onCrash() {
			// TODO Auto-generated method stub
		}
	};

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia.dismiss();
				Toast.makeText(QueryListsActivity.this, "处理成功",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent intent = new Intent(QueryListsActivity.this,
						QueryListsActivity.class);
				QueryListsActivity.this.startActivity(intent);
				break;
			case 2:
				pDia.dismiss();
				Toast.makeText(QueryListsActivity.this, "处理不成功",
						Toast.LENGTH_LONG).show();
				break;
			}

		}
	};

	public void runOnUiThreadDelayed(Runnable r, int delayMillis) {
		handler.postDelayed(r, delayMillis);
	}

	/*
	 * To gain control of the device service, you need invoke this method before
	 * any device operation.
	 */
	public void bindDeviceService() {
		try {
			DeviceService.login(this);
		} catch (RequestException e) {
			// Rebind after a few milliseconds,
			// If you want this application keep the right of the device service
			runOnUiThreadDelayed(new Runnable() {
				@Override
				public void run() {
					bindDeviceService();
				}
			}, 300);
			e.printStackTrace();
		} catch (ServiceOccupiedException e) {
			e.printStackTrace();
		} catch (ReloginException e) {
			e.printStackTrace();
		} catch (UnsupportMultiProcess e) {
			e.printStackTrace();
		}
	}

	/**
	 * Release the right of using the device.
	 */
	public void unbindDeviceService() {
		DeviceService.logout();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		unbindDeviceService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindDeviceService();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_query_lists);
		ActivityList.pushActivity(this);
		lview = (ExpandableListView) this.findViewById(R.id.reclist);

		List<ParkRecord> li = DbHelper.queryRecordList("1", 100, null);
		for (int i = 0; i < li.size(); i++) {
			groups.append(i, li.get(i));
		}

		lview.setGroupIndicator(null);
		madapter = new MyExpandableListAdapter();
		lview.setAdapter(madapter);

		btnpayfees = (Button) this.findViewById(R.id.btnpayfees);
		btnpayfees.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curFlg == 2) {
					groups.clear();
					String curStr = edtquery.getText().toString();
					List<ParkRecord> li = DbHelper.queryRecordList("1", 100,
							curStr);
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
							curStr);
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
								100, curStr);
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
			btnprint.setOnClickListener(new ClickFun(groupPosition));
			// Button btndetail = new Button(QueryListsActivity.this);
			// RelativeLayout.LayoutParams lp2 = new
			// RelativeLayout.LayoutParams(
			// RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// lp2.addRule(RelativeLayout.RIGHT_OF, v1.getId());
			// btndetail.setId(3);
			// btndetail.setLayoutParams(lp2);
			// btndetail.setText("查看详情");
			// rl.addView(btndetail);
			rl.addView(v1);
			rl.addView(btnprint);

			return rl;
		}

		public class ClickFun implements OnClickListener {

			private int cgroupPosition;

			public ClickFun(int groupPosition) {
				cgroupPosition = groupPosition;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pr = groups.get(cgroupPosition);
				pDia = ProgressDialog.show(QueryListsActivity.this, "打印停车纸",
						"正在打印中", true, false);
				try {
					progress.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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
