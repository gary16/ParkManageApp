package com.zoway.parkmanage.utils.print;

import com.zoway.parkmanage.view.PrintActivity;

public class PrinterFactory {

	public static BasePrinter getPrinter(PrintActivity acti) {

		BasePrinter bp = new P990Printer(acti);
		// BasePrinter bp = new BlueToothPrinter(acti);
		return bp;
	}
}
