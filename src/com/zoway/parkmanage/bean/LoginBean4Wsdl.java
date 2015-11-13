package com.zoway.parkmanage.bean;

import java.util.Date;

public class LoginBean4Wsdl {

	private final static Worker worker = new Worker();
	private static int AttendanceId;
	private static int TerminalId;
	private static int SectionId;
	private static String ParkName;
	private static String SectionName;
	private static String Exception;
	private static Date loginServerTime;
	private static Date loginClientTime;
	private static int CompanyId;
	private static String CompanyName;
	static {
		loginClientTime = new Date();
	}

	public static void setLoginServerTime(Date loginServerTime) {
		LoginBean4Wsdl.loginServerTime = loginServerTime;
	}

	public static void setLoginClientTime(Date loginClientTime) {
		LoginBean4Wsdl.loginClientTime = loginClientTime;
	}

	public static Worker getWorker() {
		return worker;
	}

	public static int getAttendanceId() {
		return AttendanceId;
	}

	public static Date getLoginServerTime() {
		return loginServerTime;
	}

	public static Date getLoginClientTime() {
		return loginClientTime;
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

	public static int getCompanyId() {
		return CompanyId;
	}

	public static void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public static String getCompanyName() {
		return CompanyName;
	}

	public static void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
}
