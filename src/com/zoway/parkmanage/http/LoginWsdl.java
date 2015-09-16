package com.zoway.parkmanage.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage.utils.LogUtils;

import android.util.Log;

public class LoginWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Login";
	// EndPoint
	private String endPoint = "http://59.39.7.122:8083/WebServices/Terminal.asmx";
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
