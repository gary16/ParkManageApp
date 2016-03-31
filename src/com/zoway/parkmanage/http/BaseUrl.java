package com.zoway.parkmanage.http;

import com.zoway.parkmanage.db.DbHelper;

public class BaseUrl {

	public static String BASETERMINATEURL = "http://221.4.165.162:8093/WebServices/Terminal.asmx";

	static {
		String strurl = DbHelper.getSettings("baseurl");
		if (null != strurl && !strurl.equals("")) {
			BASETERMINATEURL = strurl;
		}
	}

}
