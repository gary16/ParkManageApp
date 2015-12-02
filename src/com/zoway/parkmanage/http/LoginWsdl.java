package com.zoway.parkmanage.http;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage.bean.LoginBean4Wsdl;
import com.zoway.parkmanage.bean.Worker;

public class LoginWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Login";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/Login";

	public int getLogin(String terId, String mgrId, String pwd) {
		int b = 1;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("terminalNo", terId);
			rpc.addProperty("workerNo", mgrId);
			rpc.addProperty("password", pwd);

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
				LoginBean4Wsdl lb = new LoginBean4Wsdl();
				SoapObject o1 = (SoapObject) oj.getProperty(0);
				Worker wo = lb.getWorker();
				Field[] f1 = wo.getClass().getDeclaredFields();
				for (int i = 0; i < f1.length; i++) {
					Field f = f1[i];
					f.setAccessible(true);
					if (f.getType() == int.class) {
						f.set(wo, Integer.parseInt(o1
								.getPropertySafelyAsString(f.getName())));
					} else if (f.getType() == String.class) {
						f.set(wo, o1.getPropertySafelyAsString(f.getName()));
					}
				}

				Field[] f2 = lb.getClass().getDeclaredFields();
				for (int i = 0; i < f2.length; i++) {
					Field f = f2[i];
					f.setAccessible(true);
					if (f.getType() == int.class) {
						f.set(lb, Integer.parseInt(oj
								.getPropertySafelyAsString(f.getName())));
					} else if (f.getType() == String.class) {
						f.set(lb, oj.getPropertySafelyAsString(f.getName()));
					} else if (f.getType() == java.util.Date.class) {
						String s = oj.getPropertySafelyAsString(f.getName());
						if (!s.equals("")) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");

							int ii = s.indexOf(".");
							if (ii > 0) {
								s = s.substring(0, ii).replace("T", " ");
							} else {
								s = s.replace("T", " ");
							}
							f.set(lb, sdf.parse(s));
						} else {
							LoginBean4Wsdl.setLoginClientTime(new Date());
						}
					}
				}

				b = 0;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			b = 2;
			e1.printStackTrace();
		}
		return b;
	}
}
