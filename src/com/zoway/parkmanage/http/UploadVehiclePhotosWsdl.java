package com.zoway.parkmanage.http;

import java.io.FileNotFoundException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class UploadVehiclePhotosWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "UploadVehiclePhotos";
	// EndPoint
	private String endPoint = "http://59.39.7.122:8083/WebServices/Terminal.asmx";
	// SOAP Action
	private String soapAction = "http://tempuri.org/UploadVehiclePhotos";

	public boolean uploadPhoto(int rcid, String[] phoArr) {
		boolean b = false;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("parkRecordId", rcid);

			SoapObject soapPhotos = new SoapObject(nameSpace, "photos");
			for (int i = 0; i < phoArr.length; i++) {
				if (!phoArr[i].isEmpty()) {
					soapPhotos.addProperty("base64Binary", phoArr[i]);
				}
			}
			rpc.addSoapObject(soapPhotos);

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
				b = true;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return b;
	}
}
