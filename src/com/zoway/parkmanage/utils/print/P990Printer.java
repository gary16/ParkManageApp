package com.zoway.parkmanage.utils.print;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.landicorp.android.eptapi.utils.QrCode;
import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.view.PrintActivity;

public class P990Printer extends BasePrinter {

	public class P990Handler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO 接收消息并且去更新UI线程上的控件内容
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				P990Printer.this.printDia.dismiss();
				Toast.makeText(activity, "打印成功", Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				P990Printer.this.activity.afterPrint();
				break;
			}

		}
	}

	P990Printer(PrintActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.printHandler = new P990Handler();
	}

	@Override
	public void doPrint(String hphm, Date parktime, String recno) {
		// TODO Auto-generated method stub
		P990Printer.this.activity.beforePrint();
		progress = new Printpro(hphm, parktime, recno);
		this.printDia = ProgressDialog.show(this.activity, "打印停车纸", "正在打印中",
				true, false);
		try {
			progress.start();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doPrint2(String hphm, Date parktime, Date leavetime, float fare) {
		// TODO Auto-generated method stub
		P990Printer.this.activity.beforePrint();
		progress = new Printpro2(hphm, parktime, leavetime, fare);
		this.printDia = ProgressDialog.show(this.activity, "打印凭条", "正在打印中",
				true, false);
		try {
			progress.start();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doAfterResume() {
		// TODO Auto-generated method stub
		bindDeviceService();
	}

	@Override
	public void doAfterPause() {
		// TODO Auto-generated method stub
		unbindDeviceService();
	}

	public void runOnUiThreadDelayed(Runnable r, int delayMillis) {
		printHandler.postDelayed(r, delayMillis);
	}

	/**
	 * To gain control of the device service, you need invoke this method before
	 * any device operation.
	 */
	public void bindDeviceService() {
		try {
			DeviceService.login(activity);
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

	private Printer.Progress progress = null;

	private class Printpro extends Printer.Progress {

		private String hphm;
		private Date parktime;
		private String recno;

		public Printpro(String hphm, Date parktime, String recno) {
			this.hphm = hphm;
			this.parktime = parktime;
			this.recno = recno;
		}

		@Override
		public void doPrint(Printer printer) throws Exception {
			// TODO Auto-generated method stub
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String datetext = sdf.format(parktime);
			printer.printText("        路边停车凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.printText("商户名称:" + LoginBean4Wsdl.getCompanyName() + "\n");
			printer.printText("电话号码:26337118\n");
			printer.printText("车牌号码:" + hphm + "\n");
			printer.printText("停车位置:" + LoginBean4Wsdl.getParkName() + "\n");
			printer.printText("停车时间:" + datetext + "\n");
			printer.printText("操作员:"
					+ LoginBean4Wsdl.getWorker().getWorkerName() + "\n\n");
			printer.setAutoTrunc(false);
			printer.printText("敬爱的车主，请使用微信扫描下方二维码查询停车时长。");
			printer.printText("\n\n");

			String cUrl = String.format(
					"http://cx.zoway.com.cn:81/ParkRecord/show/%s.do", recno);
			printer.printQrCode(35, new QrCode(cUrl, QrCode.ECLEVEL_M), 312);
			printer.feedLine(4);
		}

		@Override
		public void onFinish(int arg0) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 1;
			printHandler.sendMessage(msg);
		}

		@Override
		public void onCrash() {
			// TODO Auto-generated method stub
		}
	}

	private class Printpro2 extends Printer.Progress {

		private Date parktime;
		private Date leavetime;
		private float fare;
		private String hphm;

		public Printpro2(String hphm, Date parktime, Date leavetime, float fare) {
			this.hphm = hphm;
			this.parktime = parktime;
			this.leavetime = leavetime;
			this.fare = fare;
		}

		@Override
		public void doPrint(Printer printer) throws Exception {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			Format format = new Format();
			// Use this 5x7 dot and 1 times width, 2 times height
			format.setAscSize(Format.ASC_DOT5x7);
			format.setAscScale(Format.ASC_SC1x2);
			printer.setFormat(format);
			printer.printText("        收费凭条\n");
			format.setAscScale(Format.ASC_SC1x1);
			printer.setFormat(format);
			printer.printText("\n");
			printer.feedLine(1);
			printer.printText("车牌号码:" + hphm + "\n");
			printer.feedLine(1);
			printer.printText("停车位置:" + LoginBean4Wsdl.getParkName() + "\n");
			printer.feedLine(1);
			printer.printText("停车时间:" + sdf.format(parktime) + "\n");
			printer.feedLine(1);
			printer.printText("离开时间:" + sdf.format(leavetime) + "\n");
			printer.feedLine(1);

			Date d1 = parktime;
			Date d2 = leavetime;
			long diff = d2.getTime() - d1.getTime();
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24))
					/ (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			printer.printText("停车时长:" + days + "日" + hours + "时" + minutes
					+ "分");
			printer.feedLine(1);
			printer.printText("停车费用:" + fare + "\n");
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
			printHandler.sendMessage(msg);
		}

		@Override
		public void onCrash() {
			// TODO Auto-generated method stub
		}
	}
}
