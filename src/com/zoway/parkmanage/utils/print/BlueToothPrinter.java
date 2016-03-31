package com.zoway.parkmanage.utils.print;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import bluetooth.sdk.BluetoothManager;
import cn.jelly.util.QRCodeInfo;

import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.view.PrintActivity;

public class BlueToothPrinter extends BasePrinter {

	/* ȡ��Ĭ�ϵ����������� */
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice btDevice;
	private BluetoothManager m_BluetoothManager = new BluetoothManager();
	private final String ALREADY_OPEN = "com.example.print.BlueToothPrinter.ALREADY_OPEN";
	// 0 close 1 open
	private int connectStauts = 0;
	private String hphm;
	private Date parktime;
	private Date leavetime;
	private float fare;
	private String recno;
	private int actionval;

	BlueToothPrinter(PrintActivity activity) {
		super(activity);
		this.printHandler = new BlueToothPHandler();
		// TODO Auto-generated constructor stub
	}

	public class BlueToothPHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO ������Ϣ����ȥ����UI�߳��ϵĿؼ�����
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				BlueToothPrinter.this.printDia.dismiss();
				Toast.makeText(activity, "�����豸�쳣", Toast.LENGTH_LONG).show();
				break;
			case 2:
				BlueToothPrinter.this.printDia.dismiss();
				Toast.makeText(activity, "û��ƥ��������豸", Toast.LENGTH_LONG).show();
				break;
			case 3:
				BlueToothPrinter.this.printDia = ProgressDialog.show(
						BlueToothPrinter.this.activity, "��ӡͣ����", "���ڴ�ӡ��", true,
						false);
				try {
					if (btDevice != null) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy��MM��dd�� HHʱmm��");
						if (actionval == 1) {
							m_BluetoothManager.SetPrintModel(1, 1);
							String s = "  ·��ͣ��ƾ��\n\n";
							m_BluetoothManager.PrintData(s);
							m_BluetoothManager.SetPrintModel(0, 0);
							StringBuffer sb1 = new StringBuffer("�̻�����:"
									+ LoginBean4Wsdl.getCompanyName() + "\n\n ");
							sb1.append("���ƺ���:" + hphm + "\n\n");
							sb1.append("ͣ��λ��:" + LoginBean4Wsdl.getParkName()
									+ "\n\n");
							sb1.append("ͣ��ʱ��:" + sdf.format(parktime) + "\n\n");
							sb1.append("����Ա:"
									+ LoginBean4Wsdl.getWorker()
											.getWorkerName()
									+ "\n\n�����ĳ�������ʹ��΢��ɨ���·���ά���ѯͣ��ʱ����\n\n");
							m_BluetoothManager.PrintData(sb1.toString());
							QRCodeInfo codeInfo = new QRCodeInfo();

							codeInfo.setlMargin(Integer.valueOf(12));
							codeInfo.setmSide(Integer.valueOf(1));
							String s2 = "http://zparking.zoway.com.cn/ParkRecord/show/"
									+ recno + ".do";
							String tempStr = codeInfo.GetQRCode(s2);
							// String tempStr =
							// "13 51 00 18 15 02 FE A3 F8 82 3A 08 BA D2 E8 BA 02 E8 BA 1A E8 82 FA 08 FE AB F8 00 48 00 A3 21 28 5C 56 D8 0A DC D0 CD F5 60 B3 D9 38 00 E4 80 FE A6 A0 82 28 F0 BA 67 F0 BA 17 80 BA 9C F8 82 16 40 FE FF A8 00 0A";
							m_BluetoothManager.PrintbarcodeB(tempStr);
							m_BluetoothManager.PrintData("\n\n\n");

						} else if (actionval == 2) {
							m_BluetoothManager.SetPrintModel(1, 1);
							String s = "  �շ�ƾ��\n\n";
							m_BluetoothManager.PrintData(s);
							m_BluetoothManager.SetPrintModel(0, 0);
							StringBuffer sb1 = new StringBuffer("���ƺ���:" + hphm
									+ "\n\n");
							sb1.append("ͣ��λ��:" + LoginBean4Wsdl.getParkName()
									+ "\n\n");
							sb1.append("ͣ��ʱ��:" + sdf.format(parktime) + "\n\n");
							sb1.append("�뿪ʱ��:" + sdf.format(leavetime) + "\n\n");

							Date d1 = parktime;
							Date d2 = leavetime;
							long diff = d2.getTime() - d1.getTime();
							long days = diff / (1000 * 60 * 60 * 24);

							long hours = (diff - days * (1000 * 60 * 60 * 24))
									/ (1000 * 60 * 60);
							long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
									* (1000 * 60 * 60))
									/ (1000 * 60);
							sb1.append("ͣ��ʱ��:" + days + "��" + hours + "ʱ"
									+ minutes + "��\n\n");
							sb1.append("ͣ������:" + fare + "\n\n");
							sb1.append("����Ա:"
									+ LoginBean4Wsdl.getWorker()
											.getWorkerName() + "\n");
							m_BluetoothManager.PrintData(sb1.toString());
							m_BluetoothManager.PrintData("\n\n\n");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				BlueToothPrinter.this.printDia.dismiss();
				Toast.makeText(activity, "��ӡ�ɹ�", Toast.LENGTH_LONG).show();
				break;
			case 4:
				BlueToothPrinter.this.printDia.dismiss();
				Toast.makeText(activity, "���Ӵ�ӡ�豸ʧ��", Toast.LENGTH_LONG).show();

				break;
			}
			BlueToothPrinter.this.activity.afterPrint();
		}
	}

	@Override
	public void doPrint(String hphm, Date parktime, String recno, int fromWhere) {
		// TODO Auto-generated method stub
		if (mBtAdapter == null) {
			Toast.makeText(activity, "���豸��֧������", Toast.LENGTH_LONG).show();
			BlueToothPrinter.this.activity.afterPrint();
		} else {
			this.hphm = hphm;
			this.parktime = parktime;
			this.recno = recno;
			this.actionval = 1;
			BlueToothPrinter.this.printDia = ProgressDialog.show(
					BlueToothPrinter.this.activity, "��ʼ������", "���ڳ�ʼ����", true,
					false);
			if (!mBtAdapter.isEnabled()) {
				mBtAdapter.enable();
			} else {
				Intent intent = new Intent(ALREADY_OPEN);
				this.activity.sendBroadcast(intent);
			}
		}
	}

	@Override
	public void doPrint2(String hphm, Date parktime, Date leavetime,
			float fare, int fromWhere) {
		// TODO Auto-generated method stub
		if (mBtAdapter == null) {
			Toast.makeText(activity, "���豸��֧������", Toast.LENGTH_LONG).show();
			BlueToothPrinter.this.activity.afterPrint();
		} else {
			this.hphm = hphm;
			this.parktime = parktime;
			this.leavetime = leavetime;
			this.fare = fare;
			this.actionval = 2;
			BlueToothPrinter.this.printDia = ProgressDialog.show(
					BlueToothPrinter.this.activity, "��ʼ������", "���ڳ�ʼ����", true,
					false);
			if (!mBtAdapter.isEnabled()) {
				mBtAdapter.enable();
			} else {
				Intent intent = new Intent(ALREADY_OPEN);
				this.activity.sendBroadcast(intent);
			}
		}
	}

	@Override
	public void doAfterCreate() {

		IntentFilter fintent = new IntentFilter();
		fintent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.activity.registerReceiver(mReceiver, fintent);

		fintent = new IntentFilter();
		fintent.addAction(ALREADY_OPEN);
		this.activity.registerReceiver(mReceiver, fintent);

	}

	@Override
	public void doAfterDestory() {
		// TODO Auto-generated method stub
		Thread t2 = new Thread(new ShutdownClientThread());
		t2.start();
		this.activity.unregisterReceiver(mReceiver);
	};

	// �����ͻ���
	private class ConnectClientThread implements Runnable {
		public void run() {
			try {
				// ����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
				m_BluetoothManager.ConnectServer();
				connectStauts = 1;
				Message msg = new Message();
				msg.what = 3;
				BlueToothPrinter.this.printHandler.sendMessage(msg);
			} catch (IOException e) {
				Log.e("connect", "", e);
				Message msg = new Message();
				msg.what = 4;
				BlueToothPrinter.this.printHandler.sendMessage(msg);
			}
		}
	}

	// �����ͻ���
	private class ShutdownClientThread implements Runnable {
		public void run() {
			m_BluetoothManager.shutdownClient();
			if (mBtAdapter.isEnabled())
				mBtAdapter.disable();
		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean flg = false;
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int a1 = intent.getExtras()
						.getInt(BluetoothAdapter.EXTRA_STATE);
				if (a1 == BluetoothAdapter.STATE_ON) {
					pairDevices();
				}

			} else if (action.equals(ALREADY_OPEN)) {
				pairDevices();
			} else {
				return;
			}
		}
	};

	private void pairDevices() {

		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String devicename = device.getName();
				String devicemac = device.getAddress();
				if (devicename.indexOf("MSP") > -1) {
					btDevice = device;
					break;
				}
			}
		}
		if (btDevice != null) {
			if (connectStauts == 0) {
				m_BluetoothManager.setServerAddress(btDevice.getAddress());
				Thread t1 = new Thread(new ConnectClientThread());
				t1.start();
			}
		} else {
			Message msg = new Message();
			msg.what = 2;
			BlueToothPrinter.this.printHandler.sendMessage(msg);
		}

	}
}
