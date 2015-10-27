package com.zoway.parkmanage.service;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.bean.ParkBean4Wsdl;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.ParkWsdl;
import com.zoway.parkmanage.http.UnhandleParkinfoWsdl;
import com.zoway.parkmanage.view.TakeOcrPhotoActivity;

public class TerminalService extends Service {

	private boolean flg1 = true;
	private boolean flg2 = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//
		Thread t1 = new Thread(new UploadParkingRecord());
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

	private class UploadParkingRecord implements Runnable {
		@Override
		public void run() {
			while (flg1) {
				try {
					ParkWsdl wsdl = new ParkWsdl();
					List<ParkRecord> li = DbHelper.queryNeedUpload(20);
					for (int i = 0; i < li.size(); i++) {
						ParkRecord p = li.get(i);
						ParkBean4Wsdl p4 = wsdl.whenCarIn(p.getRecordno(),
								LoginBean4Wsdl.getTerminalId(), LoginBean4Wsdl
										.getWorker().getWorkerId(), 2, p
										.getHphm());
						if (p4.getFlgflg()==1) {
							DbHelper.updateUploadFlag(p.getTid(), 1);
						}
					}
					Thread.sleep(5000);
				} catch (Exception er) {

				}
			}
		}
	}

	// private class CheckNewParkInfo implements Runnable {
	//
	// private NotificationManager nm;
	// private int orderid = 0;
	//
	// @SuppressLint("NewApi")
	// @Override
	// public void run() {
	// orderid = 0;
	// nm = (NotificationManager)
	// getSystemService(Context.NOTIFICATION_SERVICE);
	// while (flg1) {
	// try {
	// int noid = 0;
	// UnhandleParkinfoWsdl ups = new UnhandleParkinfoWsdl();
	// ArrayList<String[]> al = ups.getUnhandleList(4, 1, 0);
	// if (al != null && al.size() > 0) {
	// noid = Integer.parseInt(al.get(0)[0]);
	// if (noid != orderid) {
	//
	// String rcid = al.get(0)[0];
	// String rcno = al.get(0)[1];
	// String sno = al.get(0)[3];
	// String rt = al.get(0)[4].replace("T", "	");
	//
	// Intent it1 = new Intent(TerminalService.this,
	// TakeOcrPhotoActivity.class);
	// it1.putExtra("rcid", rcid);
	// it1.putExtra("rcno", rcno);
	// it1.putExtra("sno", sno);
	// it1.putExtra("rt", rt);
	// PendingIntent pt1 = PendingIntent.getActivity(
	// TerminalService.this, 0, it1,
	// PendingIntent.FLAG_CANCEL_CURRENT);
	//
	// Uri u = RingtoneManager
	// .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	// Notification noti = new Notification.Builder(
	// TerminalService.this)
	// .setContentTitle(
	// String.format("车位位置:%s", sno))
	// .setContentText(
	// String.format("入库时间:%s", rt))
	// .setSmallIcon(R.drawable.ic_launcher)
	// .setSound(u).setContentIntent(pt1).build();
	//
	// noti.defaults |= Notification.DEFAULT_ALL;
	// noti.flags |= Notification.FLAG_NO_CLEAR;
	//
	// nm.cancel(orderid);
	//
	// nm.notify(noid, noti);
	// orderid = noid;
	// }
	// }
	// Thread.sleep(2500);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	// }

}
