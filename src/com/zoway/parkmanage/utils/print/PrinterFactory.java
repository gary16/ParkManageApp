package com.zoway.parkmanage.utils.print;

import com.zoway.parkmanage.db.DbHelper;
import com.zoway.parkmanage.view.PrintActivity;

public class PrinterFactory {

	public static BasePrinter getPrinter(PrintActivity acti) {

		String strclass = DbHelper.getSettings("printer");
		BasePrinter bp = null;
		if (null != strclass && !strclass.equals("")) {
			if (strclass.equals("BlueToothPrinter")) {
				bp = new BlueToothPrinter(acti);
			} else {
				bp = new P990Printer(acti);
			}
		} else {
			bp = new P990Printer(acti);
		}
		// BasePrinter bp = new BlueToothPrinter(acti);
		return bp;
	}
}
