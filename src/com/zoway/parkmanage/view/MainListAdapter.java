package com.zoway.parkmanage.view;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.R.drawable;
import com.zoway.parkmanage.R.id;
import com.zoway.parkmanage.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public MainListAdapter(Activity act, SparseArray<Group> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return groups.get(arg0).children.get(arg1);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		String[] aa1 = children.split(",");

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mainlistdetails, null);
		}

		TextView text = null;
		text = (TextView) convertView.findViewById(R.id.txtlistinfo);
		text.setText(aa1[0]);
		TextView text2 = null;
		text2 = (TextView) convertView.findViewById(R.id.txtlistinfo2);
		text2.setText(aa1[1]);

		Button btn = (Button) convertView.findViewById(R.id.btninputdata);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, TakeOcrPhotoActivity.class);
				activity.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	// @Override
	// public View getGroupView(int groupPosition, boolean isExpanded,
	// View convertView, ViewGroup parent) {
	// // Layout parameters for the ExpandableListView
	//
	// LinearLayout ll = new LinearLayout(activity);
	// ll.setBackgroundResource(R.drawable.mainlistline);
	// ll.setGravity(Gravity.CENTER_VERTICAL);
	// ImageView iv1 = new ImageView(activity);
	// iv1.setImageDrawable(activity.getResources().getDrawable(
	// groups.get(groupPosition).drawableresid));
	// // iv1.setImageResource(groups.get(groupPosition).drawableresid);
	// LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// lp1.weight = 1;
	// iv1.setPadding(0, 0, 0, 3);
	// lp1.setMargins(1, 0, 0, 0);
	// iv1.setLayoutParams(lp1);
	// ll.addView(iv1);
	//
	// LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// lp2.setMargins(15, 0, 0, 0);
	// lp2.weight = 1;
	// TextView textView = new TextView(activity);
	// textView.setLayoutParams(lp2);
	// textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	// textView.setText(groups.get(groupPosition).string);
	// textView.setTextSize(25);
	// ll.addView(textView);
	//
	// LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// lp3.weight = 1;
	// ImageView iv2 = new ImageView(activity);
	// iv2.setImageDrawable(activity.getResources().getDrawable(
	// R.drawable.navigateright));
	// textView.setGravity(Gravity.LEFT);
	// iv2.setLayoutParams(lp2);
	// ll.addView(iv2);
	//
	// return ll;
	// }
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		RelativeLayout rl = new RelativeLayout(activity);
		rl.setBackgroundResource(R.drawable.mainlistline);

		ImageView iv1 = new ImageView(activity);
		iv1.setImageDrawable(activity.getResources().getDrawable(
				groups.get(groupPosition).drawableresid));
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		iv1.setPadding(0, 0, 0, 3);
		lp1.setMargins(1, 0, 0, 0);
		iv1.setLayoutParams(lp1);
		iv1.setId(0x12345678);
		rl.addView(iv1);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		lp2.addRule(RelativeLayout.RIGHT_OF, iv1.getId());
		lp2.setMargins(15, 0, 0, 0);

		TextView textView = new TextView(activity);
		textView.setText(groups.get(groupPosition).string);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		// textView.setPadding(0, 5, 0, 0);
		textView.setTextSize(25);
		textView.setLayoutParams(lp2);
		textView.setId(0x12345679);
		rl.addView(textView);

		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp3.addRule(RelativeLayout.ALIGN_TOP, iv1.getId());
		lp3.addRule(RelativeLayout.ALIGN_BOTTOM, iv1.getId());
		lp3.setMargins(0, 0, 10, 0);
		ImageView iv2 = new ImageView(activity);
		if (isExpanded) {
			iv2.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.navigatedown));
		} else {
			iv2.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.navigateright));
		}
		iv2.setScaleType(ScaleType.CENTER);
		iv2.setId(0x12345680);
		iv2.setLayoutParams(lp3);
		rl.addView(iv2);

		return rl;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupExpanded(groupPosition);
	}

}
