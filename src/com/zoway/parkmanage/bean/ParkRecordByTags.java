package com.zoway.parkmanage.bean;

import java.io.Serializable;
import java.util.Date;

public class ParkRecordByTags implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int tid;
	private String recordno;
	private String parkno;
	private Date parktime;
	private int stauts;

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

	public String getParkno() {
		return parkno;
	}

	public void setParkno(String parkno) {
		this.parkno = parkno;
	}

	public Date getParktime() {
		return parktime;
	}

	public void setParktime(Date parktime) {
		this.parktime = parktime;
	}

	public int getStauts() {
		return stauts;
	}

	public void setStauts(int stauts) {
		this.stauts = stauts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
