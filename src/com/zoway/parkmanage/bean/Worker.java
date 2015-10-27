package com.zoway.parkmanage.bean;

public class Worker {
	private int WorkerId;
	private int CompanyId;
	private String WorkerNo;
	private String Password;
	private String WorkerName;
	private String Gender;
	private String MobileNo;
	private String Remark;

	public int getWorkerId() {
		return WorkerId;
	}

	public void setWorkerId(int workerId) {
		WorkerId = workerId;
	}

	public int getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public String getWorkerNo() {
		return WorkerNo;
	}

	public void setWorkerNo(String workerNo) {
		WorkerNo = workerNo;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getWorkerName() {
		return WorkerName;
	}

	public void setWorkerName(String workerName) {
		WorkerName = workerName;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getMobileNo() {
		return MobileNo;
	}

	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

}
