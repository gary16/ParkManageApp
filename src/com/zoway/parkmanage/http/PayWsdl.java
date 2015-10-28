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
			SoapPrimitive oj = (SoapPrimitive)object.getProperty(0);
			obj = new PayBean4Wsdl();
			Field f = obj.getClass().getDeclaredField("PayResult");
			f.setAccessible(true);
			f.set(obj, Boolean.parseBoolean(oj.toString()));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}

}
