package com.zoway.parkmanage.http;

import java.lang.reflect.Field;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.zoway.parkmanage.bean.IgnoreBean4Wsdl;

public class IgnoreWsdl {
	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Ignore";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/Ignore";

	public IgnoreBean4Wsdl whenCarIngore(String recordNo, int workerId) {
		IgnoreBean4Wsdl obj = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("recordNo", recordNo);
			rpc.addProperty("workerId", workerId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(endPoint);
			try {
				transport.call(soapAction, envelope);
				SoapObject object = (SoapObject) envelope.bodyIn;
				SoapPrimitive oj = (SoapPrimitive) object.getProperty(0);
				obj = new IgnoreBean4Wsdl();
				Field f = obj.getClass().getDeclaredField("IgnoreResult");
				f.setAccessible(true);
				f.set(obj, Boolean.parseBoolean(oj.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}

}
