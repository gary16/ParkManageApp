package com.zoway.parkmanage.http;

import java.lang.reflect.Field;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
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

	public PayBean4Wsdl whenPay(int parkRecordId, int payType, int fare) {
		PayBean4Wsdl obj = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("parkRecordId", parkRecordId);
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

			obj = new PayBean4Wsdl();
			Field[] fieldArr = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fieldArr.length; i++) {
				Field f = fieldArr[i];
				f.setAccessible(true);
				f.set(obj, object.getProperty(f.getName()));
			}
			// long ParkRecordId = (Long) object.getProperty(0);
			// Date ReachTime = (Date) object.getProperty(1);
			// String Exception = object.getProperty(2).toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}

}
