package com.zoway.parkmanage.view;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.R.id;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class DrawRectView extends View {

	private Activity activity;

	public DrawRectView(Context context) {
		super(context);
		this.activity = (Activity) context;
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		DisplayMetrics dm = new DisplayMetrics();
		FrameLayout fr = (FrameLayout) activity
				.findViewById(R.id.camera_preview);
		int h1 = fr.getHeight() / 2;
		int w1 = fr.getWidth() / 2;
		int h2 = (int) (h1 * 0.15);
		int w2 = (int) (w1 * 0.625);
		int h = h1 - h2;
		int w = w1 - w2;
		Log.d("...", "h1:" + h1 + ",w1:" + w1 + ",h2*2:" + h2 * 2 + ",w2*2:"
				+ w2 * 2 + ",h:" + h + ",w:" + w);

		float stoke = 5f;
		Paint p = new Paint();
		p.setStyle(Paint.Style.STROKE);// ÊµÐÄ¾ØÐÎ¿ò
		p.setStrokeWidth(stoke);
		p.setColor(Color.RED);

		canvas.drawRect(new RectF(w - stoke, h - stoke, w + w2 * 2 + stoke, h
				+ h2 * 2 + stoke), p);

	}

}
