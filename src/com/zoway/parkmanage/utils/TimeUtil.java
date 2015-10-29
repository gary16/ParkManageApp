package com.zoway.parkmanage.utils;

import java.util.Date;

import com.zoway.parkmanage.bean.LoginBean4Wsdl;

public class TimeUtil {

	public static Date getTime() {
		long now = (new Date()).getTime();
		long diff = now - LoginBean4Wsdl.getLoginClientTime().getTime();
		long nowServer = LoginBean4Wsdl.getLoginServerTime().getTime() + diff;
		return new Date(nowServer);
	}

}
