package com.zoway.parkmanage.bean;

import java.util.Date;

public class ParkBean4Wsdl {
	private long ParkRecordId;
	private Date ReachTime;
	private String Exception;

	public long getParkRecordId() {
		return ParkRecordId;
	}

	public void setParkRecordId(long parkRecordId) {
		ParkRecordId = parkRecordId;
	}

	public Date getReachTime() {
		return ReachTime;
	}

	public void setReachTime(Date reachTime) {
		ReachTime = reachTime;
	}

	public String getException() {
		return Exception;
	}

	public void setException(String exception) {
		Exception = exception;
	}

}
