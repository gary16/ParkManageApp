package com.zoway.parkmanage.view;

import java.util.ArrayList;
import java.util.List;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.R.id;
import com.zoway.parkmanage.R.layout;
import com.zoway.parkmanage.R.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class QueryListsActivity extends Activity {

	private final SparseArray<Group> groups = new SparseArray<Group>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;
	private Button malogout;
	private Button btnunhandled;
	private Button btnhandled;
	private Button btnquery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_lists);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Group group = new Group("粤X12345 2015年8月12日 9点50分");
		groups.append(0, group);

		group = new Group("粤X54321 2015年9月12日 10点21分");
		groups.append(1, group);
		group = new Group("粤XJQ001 2015年9月11日 14点21分");
		groups.append(2, group);

		lview = (ExpandableListView) this.findViewById(R.id.reclist);

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
				Group group = new Group("粤X12345 2015年8月12日 9点50分");
				groups.append(0, group);

				group = new Group("粤X54321 2015年9月12日 10点21分");
				groups.append(1, group);
				group = new Group("粤XJQ001 2015年9月11日 14点21分");
				groups.append(2, group);
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
				Group group = new Group("粤XPP123 2015年7月2日 8点50分");
				groups.append(0, group);
				group = new Group("粤X78323 2015年5月23日 10点01分");
				groups.append(1, group);
				group = new Group("粤XPC777 2015年9月11日 15点21分");
				groups.append(2, group);
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
			return groups.get(groupPosition).children.get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).children.size();
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
			TextView tv1 = new TextView(QueryListsActivity.this);
			tv1.setText(groups.get(groupPosition).string);
			return tv1;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}

	private class Group {
		public String string;
		public final List<String> children = new ArrayList<String>();

		public Group(String string) {
			this.string = string;
		}
	}
}
