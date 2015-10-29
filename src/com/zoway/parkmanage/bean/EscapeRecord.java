package com.zoway.parkmanage.bean;

import java.util.Date;

public class EscapeRecord {

	private int tid;
	private String recordno;
	private String hphm;
	private Date escapetime;
	private Date uploadtime;
	private int uploadstatus;
	private String filepath;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

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

	public Date getEscapetime() {
		return escapetime;
	}

	public void setEscapetime(Date escapetime) {
		this.escapetime = escapetime;
	}

	public int getUploadstatus() {
		return uploadstatus;
	}

	public void setUploadstatus(int uploadstatus) {
		this.uploadstatus = uploadstatus;
	}

}
