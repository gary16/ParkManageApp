package com.zoway.parkmanage.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class RegisterVehicleInfoWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "RegisterVehicleInfo";
	// EndPoint
	private String endPoint =BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/RegisterVehicleInfo";

	public boolean uploadData(int prid, int tid, int wid, String vtype,
			String hphm) {
		boolean flg = false;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("parkRecordId", prid);
			rpc.addProperty("terminalId", tid);
			rpc.addProperty("workerId", wid);
			rpc.addProperty("vehicleType", vtype);
			rpc.addProperty("vehicleNo", hphm);

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
			if (result.equals("true")) {
				flg = true;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return flg;
	}
}
