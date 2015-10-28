package com.zoway.parkmanage.bean;

import java.util.Date;

public class PayRecord {

	int tid;
	String recordno;
	String hphm;
	float fare;
	Date uploadtime;
	int uploadstatus;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getRecordno() {
		return recordno;
	}
	public void setRecordno(String recordno) {
		this.recordno = recordno;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public float getFare() {
		return fare;
	}
	public void setFare(float fare) {
		this.fare = fare;
	}
	public Date getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}
	public int getUploadstatus() {
		return uploadstatus;
	}
	public void setUploadstatus(int uploadstatus) {
		this.uploadstatus = uploadstatus;
	}

	
	
}
