package com.zoway.parkmanage.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.zoway.parkmanage.bean.EscapeBean4Wsdl;
import com.zoway.parkmanage.bean.PayBean4Wsdl;

public class EscapeWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "Escape";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/Escape";

	public EscapeBean4Wsdl whenCarEscape(String recordNo, int workerId,
			String dirPath) {
		EscapeBean4Wsdl obj = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("recordNo", recordNo);
			rpc.addProperty("workerId", workerId);
			SoapObject soapPhotos = new SoapObject(nameSpace, "photos");
			File dir = new File(dirPath);
			if (dir.isDirectory()) {
				File[] fileArr = dir.listFiles();
				for (int i = 0; i < fileArr.length; i++) {
					File ff = fileArr[i];
					Bitmap bitmap = BitmapFactory
							.decodeStream(new FileInputStream(ff));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					String ss = Base64.encodeToString(baos.toByteArray(),
							Base64.NO_WRAP);
					soapPhotos.addProperty("base64Binary", ss);
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
				SoapObject object = (SoapObject) envelope.bodyIn;
				SoapPrimitive oj = (SoapPrimitive) object.getProperty(0);
				obj = new EscapeBean4Wsdl();
				Field f = obj.getClass().getDeclaredField("EscapeResult");
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
