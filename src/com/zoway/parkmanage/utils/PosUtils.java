package com.zoway.parkmanage.utils;

import com.bw.spdev.IccReader;
import com.bw.spdev.MagCard;
import com.bw.spdev.Ped;
import com.bw.spdev.PiccReader;
import com.bw.spdev.Printer;
import com.bw.spdev.SpDev;
import com.bw.spdev.SysCmd;
import com.odm.misc.MiscDevice;

public class PosUtils {

	public static MiscDevice misPos = null;
	public static SpDev sp = null;
	public static IccReader icc = null;
	public static PiccReader picc = null;
	public static MagCard mag = null;
	public static Ped ped = null;
	public static Printer printer = null;
	public static SysCmd sys = null;

	public static void InitDev() {
		sp = SpDev.getInstance();
		sp.SpDevCreate();

		icc = IccReader.getInstance();
		picc = PiccReader.getInstance();
		mag = MagCard.getInstance();
		ped = Ped.getInstance();
		printer = Printer.getInstance();
		sys = SysCmd.getInstance();
	}

}
