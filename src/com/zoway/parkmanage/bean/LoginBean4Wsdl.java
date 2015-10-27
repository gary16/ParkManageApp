package com.zoway.parkmanage.bean;

public class LoginBean4Wsdl {

	private final static Worker worker = new Worker();
	private static int AttendanceId;
	private static int TerminalId;
	private static int SectionId;
	private static String ParkName;
	private static String SectionName;
	private static  String Exception;
	public static Worker getWorker() {
		return worker;
	}
	public static int getAttendanceId() {
		return AttendanceId;
	}
	public static int getTerminalId() {
		return TerminalId;
	}
	public static int getSectionId() {
		return SectionId;
	}
	public static String getParkName() {
		return ParkName;
	}
	public static String getSectionName() {
		return SectionName;
	}
	public static String getException() {
		return Exception;
	}

	

}
