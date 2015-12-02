package com.zoway.parkmanage.http;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage.bean.LeaveBean4Wsdl;
import com.zoway.parkmanage.bean.PayBean4Wsdl;

public class PayWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Pay";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/Pay";

	public PayBean4Wsdl whenPay(String recordNo, String payType, int fare) {
		PayBean4Wsdl obj = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("recordNo", recordNo);
			rpc.addProperty("payType", payType);
			rpc.addProperty("fare", fare);
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
			SoapObject oj = (SoapObject) object.getProperty(0);
			obj = new PayBean4Wsdl();

			Field[] fieldArr = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fieldArr.length; i++) {
				Field f = fieldArr[i];
				f.setAccessible(true);
				if (f.getType() == String.class) {
					f.set(obj, oj.getPropertySafelyAsString(f.getName()));
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
						f.set(obj, sdf.parse(s));
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
