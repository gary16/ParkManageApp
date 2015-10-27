package com.zoway.parkmanage.bean;

import java.util.Date;

public class LeaveBean4Wsdl {

	private float Fare;
	private Date LeaveTime;
	private String Exception;
	private int flgflg=0;
	
	
	public int getFlgflg() {
		return flgflg;
	}

	public void setFlgflg(int flgflg) {
		this.flgflg = flgflg;
	}

	public float getFare() {
		return Fare;
	}

	public void setFare(float fare) {
		Fare = fare;
	}

	public Date getLeaveTime() {
		return LeaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		LeaveTime = leaveTime;
	}

	public String getException() {
		return Exception;
	}

	public void setException(String exception) {
		Exception = exception;
	}

}
