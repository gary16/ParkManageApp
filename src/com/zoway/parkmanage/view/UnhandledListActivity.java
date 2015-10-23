package com.zoway.parkmanage.view;

import java.util.ArrayList;
import java.util.List;

import com.zoway.parkmanage.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class UnhandledListActivity extends Activity {

	private final SparseArray<Group> groups = new SparseArray<Group>();
	private ExpandableListView lview;
	private MyExpandableListAdapter madapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unhandled_list);
		Group group = new Group("��X67890 2015��8��7�� 9��50��");
		groups.append(0, group);

		group = new Group("��XOK123 2015��4��12�� 9��21��");
		groups.append(1, group);
		group = new Group("��XJQ888 2015��9��11�� 13��21��");
		groups.append(2, group);

		lview = (ExpandableListView) this.findViewById(R.id.reclist);

		lview.setGroupIndicator(null);
		madapter = new MyExpandableListAdapter();
		lview.setAdapter(madapter);
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
			TextView tv1 = new TextView(UnhandledListActivity.this);
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
