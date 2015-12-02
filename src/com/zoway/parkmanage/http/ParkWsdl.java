package com.zoway.parkmanage.http;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage.bean.ParkBean4Wsdl;

public class ParkWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Park";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/Park";

	public ParkBean4Wsdl whenCarIn(String recordNo, int terminalId,
			int workerId, int vehicleType, String vehicleNo, Date reachTime) {
		ParkBean4Wsdl obj = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("recordNo", recordNo);
			rpc.addProperty("terminalId", terminalId);
			rpc.addProperty("workerId", workerId);
			rpc.addProperty("vehicleType", "小型汽车");
			rpc.addProperty("vehicleNo", vehicleNo);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ss = sdf.format(reachTime).replace(" ", "T");
			rpc.addProperty("reachTime", ss);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(endPoint);
			try {
				transport.call(soapAction, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}

			SoapObject object = (SoapObject) envelope.bodyIn;
			String result = object.getProperty(0).toString();
			if (result.indexOf("Exception") > 0) {

			} else {

				SoapObject oj = (SoapObject) object.getProperty(0);
				obj = new ParkBean4Wsdl();
				Field[] fieldArr = obj.getClass().getDeclaredFields();
				for (int i = 0; i < fieldArr.length; i++) {
					Field f = fieldArr[i];
					f.setAccessible(true);
					if (f.getType() == String.class) {
						f.set(obj, oj.getPropertySafelyAsString(f.getName()));
					} else if (f.getType() == java.util.Date.class) {
						String s = oj.getPropertySafelyAsString(f.getName());
						if (!s.equals("")) {
							int ii = s.indexOf(".");
							if (ii > 0) {
								s = s.substring(0, ii).replace("T", " ");
							} else {
								s = s.replace("T", " ");
							}
							f.set(obj, sdf.parse(s));
						}
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}
}
