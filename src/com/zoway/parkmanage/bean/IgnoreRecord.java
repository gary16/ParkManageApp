package com.zoway.parkmanage.bean;

import java.util.Date;

public class IgnoreRecord {

	private int tid;
	private String recordno;
	private String hphm;
	private Date ignoretime;
	private Date uploadtime;
	private int uploadstatus;

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

	public Date getIgnoretime() {
		return ignoretime;
	}

	public void setIgnoretime(Date ignoretime) {
		this.ignoretime = ignoretime;
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
