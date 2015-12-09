package com.zoway.parkmanage.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zoway.parkmanage.bean.EscapeBean4Wsdl;
import com.zoway.parkmanage.bean.EscapeRecord;
import com.zoway.parkmanage.bean.IgnoreBean4Wsdl;
import com.zoway.parkmanage.bean.IgnoreRecord;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.bean.ParkBean4Wsdl;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.bean.PayBean4Wsdl;
import com.zoway.parkmanage.bean.PayRecord;
import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.http.EscapeWsdl;
import com.zoway.parkmanage.http.IgnoreWsdl;
import com.zoway.parkmanage.http.ParkWsdl;
import com.zoway.parkmanage.http.PayWsdl;

public class TerminalService extends Service {

	private boolean flg1 = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Thread t1 = new Thread(new UploadTask());
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

	private class UploadTask implements Runnable {

		@Override
		public void run() {

			while (flg1) {
				try {
					uploadParkingRecord();
					uploadPayRecord();
					uploadIgnoreRecord();
					uploadEscapeRecord();
					Thread.sleep(12000);
				} catch (Exception er) {

				}
			}
		}

		private void uploadParkingRecord() {
			ParkWsdl wsdl = new ParkWsdl();
			List<ParkRecord> li = DbHelper.queryNeedUpload(20);
			for (int i = 0; i < li.size(); i++) {
				ParkRecord p = li.get(i);
				ParkBean4Wsdl p4 = wsdl.whenCarIn(p.getRecordno(),
						LoginBean4Wsdl.getTerminalId(), LoginBean4Wsdl
								.getWorker().getWorkerId(), 2, p.getHphm(), p
								.getParktime());
				if (p4 != null) {
					DbHelper.updateUploadFlag(p.getTid(), 1);
				}
			}
		}

		private void uploadPayRecord() {
			PayWsdl wsdl = new PayWsdl();
			List<PayRecord> li = DbHelper.queryNeedUploadPay(20);
			for (int i = 0; i < li.size(); i++) {
				PayRecord p = li.get(i);
				PayBean4Wsdl p4 = wsdl.whenPay(p.getRecordno(), "现金",
						(int) p.getFare());
				if (p4 != null) {
					DbHelper.updateUploadPayFlag(p.getTid(), 1);
				}
			}
		}

		private void uploadEscapeRecord() {
			EscapeWsdl wsdl = new EscapeWsdl();
			List<EscapeRecord> li = DbHelper.queryNeedUploadEvasion(10);
			for (int i = 0; i < li.size(); i++) {
				EscapeRecord er = li.get(i);
				EscapeBean4Wsdl eb = wsdl.whenCarEscape(er.getRecordno(),
						LoginBean4Wsdl.getWorker().getWorkerId(),
						er.getFilepath());
				if (eb != null & eb.isEscapeResult()) {
					DbHelper.updateUploadEscapeFlag(er.getTid(), 1);
				}
			}
		}

		private void uploadIgnoreRecord() {
			IgnoreWsdl wsdl = new IgnoreWsdl();
			List<IgnoreRecord> li = DbHelper.queryNeedUploadIgnore(10);
			for (int i = 0; i < li.size(); i++) {
				IgnoreRecord er = li.get(i);
				IgnoreBean4Wsdl ib = wsdl.whenCarIngore(er.getRecordno(),
						LoginBean4Wsdl.getWorker().getWorkerId());
				if (ib != null & ib.isIgnoreResult()) {
					DbHelper.updateUploadIgnoreFlag(er.getTid(), 1);
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
