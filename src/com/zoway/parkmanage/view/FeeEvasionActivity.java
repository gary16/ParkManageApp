package com.zoway.parkmanage.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.image.BitmapHandle;

public class FeeEvasionActivity extends Activity {

	private ImageButton btnTakeEvapto;
	private Button btnsure4bill;
	private int tid;
	private String hphm;
	private Date parktime;
	private String recordno;
	private String img_path;
	private final int REQIMG = 0x12345678;
	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fee_evasion);
		ActivityList.pushActivity(this);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		btnTakeEvapto = (ImageButton) this.findViewById(R.id.btnTakeEvapto);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		Intent ii = this.getIntent();
		hphm = ii.getStringExtra("hphm");
		parktime = (Date) ii.getSerializableExtra("parktime");
		img_path = ii.getStringExtra("fname");
		recordno = ii.getStringExtra("recordno");
		tid = ii.getIntExtra("tid", 0);
		txtcarnumber.setText("粤" + hphm);
		txtpark.setText("南源路");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		txtparktime.setText(sdf.format(parktime));

		btnTakeEvapto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(img_path, "p4ori.jpg")));
				startActivityForResult(intent, REQIMG);

			}
		});
		btnsure4bill.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbHelper.setEscapeRecord(tid, recordno, hphm, new Date());
				Toast.makeText(FeeEvasionActivity.this, "修改逃费成功",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(FeeEvasionActivity.this,
						UnhandledListActivity.class);
				FeeEvasionActivity.this.startActivity(i);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, UnhandledListActivity.class);
		this.startActivity(ii);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;
		else {
			switch (requestCode) {
			case REQIMG:
				try {
					InputStream is = getContentResolver().openInputStream(
							Uri.fromFile(new File(img_path, "p4ori.jpg")));
					Bitmap bitmapSelected1 = BitmapHandle.getReduceBitmap(is,
							false, 5, 90);
					this.btnTakeEvapto.setImageBitmap(bitmapSelected1);
					BitmapHandle.writeJpgFromBitmap(img_path + File.separator
							+ "p4.jpg", bitmapSelected1, 90);
					File f = new File(img_path + File.separator + "p4ori.jpg");
					if (f.exists()) {
						f.delete();
					}
					if (bitmapSelected1 != null) {
						bitmapSelected1 = null;
					}
				} catch (Exception er) {
					er.printStackTrace();
				}

				break;
			default:
				break;
			}
			System.gc();
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private Bitmap getReduceBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inJustDecodeBounds = false;

			options.inSampleSize = 5;
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fee_evasion, menu);
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
}
