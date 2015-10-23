package com.zoway.parkmanage.bean;

public class LoginUser {

	public static int getWorkerId() {
		return workerId;
	}

	public static int getCompanyId() {
		return companyId;
	}

	public static String getWorkerNo() {
		return workerNo;
	}

	public static String getWorkerName() {
		return workerName;
	}

	public static int getAttendanceId() {
		return attendanceId;
	}

	public static int getTerminalId() {
		return terminalId;
	}

	public static int getSectionId() {
		return sectionId;
	}

	public static boolean isHasLog() {
		return hasLog;
	}

	private static int workerId = 0;
	private static int companyId = 0;
	private static String workerNo;
	private static String workerName="≤‚ ‘»À‘±";
	private static int attendanceId = 0;
	private static int terminalId = 0;
	private static int sectionId = 0;
	private static boolean hasLog = false;

	public static void login(int workerId, int companyId, String workerNo,
			String workerName, int attendanceId, int terminalId, int sectionId) {
		LoginUser.workerId = workerId;
		LoginUser.companyId = companyId;
		LoginUser.workerNo = workerNo;
		LoginUser.workerName = workerName;
		LoginUser.attendanceId = attendanceId;
		LoginUser.terminalId = terminalId;
		LoginUser.sectionId = sectionId;
		hasLog = true;
	}

	public static void logout() {
		LoginUser.workerId = 0;
		LoginUser.companyId = 0;
		LoginUser.workerNo = null;
		LoginUser.workerName = null;
		LoginUser.attendanceId = 0;
		LoginUser.terminalId = 0;
		LoginUser.sectionId = 0;
		hasLog = false;
	}
}
