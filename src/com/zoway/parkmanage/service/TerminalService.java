package com.zoway.parkmanage.service;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.zoway.parkmanage.R;
import com.zoway.parkmanage.http.UnhandleParkinfoWsdl;
import com.zoway.parkmanage.view.TakeOcrPhotoActivity;

public class TerminalService extends Service {

	private NotificationManager nm;
	private int orderid = 0;
	private boolean flg1 = true;
	private boolean flg2 = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		orderid = 0;
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Thread t1 = new Thread(new CheckNewParkInfo());
		t1.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);

	}

	private class CheckNewParkInfo implements Runnable {

		@Override
		public void run() {
			while (flg1) {
				try {
					int noid = 0;
					UnhandleParkinfoWsdl ups = new UnhandleParkinfoWsdl();
					ArrayList<String[]> al = ups.getUnhandleList(4, 1, 0);
					if (al != null && al.size() > 0) {
						noid = Integer.parseInt(al.get(0)[0]);
						if (noid != orderid) {

							String rcid = al.get(0)[0];
							String rcno = al.get(0)[1];
							String sno = al.get(0)[3];
							String rt = al.get(0)[4].replace("T", "	");

							Intent it1 = new Intent(TerminalService.this,
									TakeOcrPhotoActivity.class);
							it1.putExtra("rcid", rcid);
							it1.putExtra("rcno", rcno);
							it1.putExtra("sno", sno);
							it1.putExtra("rt", rt);
							PendingIntent pt1 = PendingIntent.getActivity(
									TerminalService.this, 0, it1,
									PendingIntent.FLAG_CANCEL_CURRENT);

							Uri u = RingtoneManager
									.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							Notification noti = new Notification.Builder(
									TerminalService.this)
									.setContentTitle(
											String.format("车位位置:%s", sno))
									.setContentText(
											String.format("入库时间:%s", rt))
									.setSmallIcon(R.drawable.ic_launcher)
									.setSound(u).setContentIntent(pt1).build();

							noti.defaults |= Notification.DEFAULT_ALL;
							noti.flags |= Notification.FLAG_NO_CLEAR;

							nm.cancel(orderid);

							nm.notify(noid, noti);
							orderid = noid;
						}
					}
					Thread.sleep(2500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
