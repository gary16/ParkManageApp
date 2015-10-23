package com.zoway.parkmanage.bean;

import java.util.Date;

public class ParkRecord {

	int tid;
	int recoidid;
	String recordno;
	int parkid;
	String parkno;
	String hphm;
	String hphmcolor;
	Date parktime;
	Date leavetime;
	float fees;
	int status;
	String filepathlist;
	int isprint;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getRecoidid() {
		return recoidid;
	}
	public void setRecoidid(int recoidid) {
		this.recoidid = recoidid;
	}
	public String getRecordno() {
		return recordno;
	}
	public void setRecordno(String recordno) {
		this.recordno = recordno;
	}
	public int getParkid() {
		return parkid;
	}
	public void setParkid(int parkid) {
		this.parkid = parkid;
	}
	public String getParkno() {
		return parkno;
	}
	public void setParkno(String parkno) {
		this.parkno = parkno;
	}
	public String getHphm() {
		return hphm;
	}
	public void setHphm(String hphm) {
		this.hphm = hphm;
	}
	public String getHphmcolor() {
		return hphmcolor;
	}
	public void setHphmcolor(String hphmcolor) {
		this.hphmcolor = hphmcolor;
	}
	public Date getParktime() {
		return parktime;
	}
	public void setParktime(Date parktime) {
		this.parktime = parktime;
	}
	public Date getLeavetime() {
		return leavetime;
	}
	public void setLeavetime(Date leavetime) {
		this.leavetime = leavetime;
	}
	public float getFees() {
		return fees;
	}
	public void setFees(float fees) {
		this.fees = fees;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFilepathlist() {
		return filepathlist;
	}
	public void setFilepathlist(String filepathlist) {
		this.filepathlist = filepathlist;
	}
	public int getIsprint() {
		return isprint;
	}
	public void setIsprint(int isprint) {
		this.isprint = isprint;
	}

	
	
}
