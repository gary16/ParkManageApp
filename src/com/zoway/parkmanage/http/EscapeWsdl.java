package com.zoway.parkmanage.http;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage.bean.EscapeBean4Wsdl;
import com.zoway.parkmanage.bean.ParkBean4Wsdl;

public class EscapeWsdl {

	// 命名空间
		private String nameSpace = "http://tempuri.org/";
		// 调用的方法名称
		private String methodName = "Escape";
		// EndPoint
		private String endPoint = BaseUrl.BASETERMINATEURL;
		// SOAP Action
		private String soapAction = "http://tempuri.org/Escape";

		public EscapeBean4Wsdl whenCarIn(String recordNo, int terminalId,
				int workerId, int vehicleType, String vehicleNo) {
			EscapeBean4Wsdl obj = null;
			try {
				SoapObject rpc = new SoapObject(nameSpace, methodName);
				rpc.addProperty("recordNo", recordNo);
				rpc.addProperty("terminalId", terminalId);
				rpc.addProperty("workerId", workerId);
				rpc.addProperty("vehicleType", "小型汽车");
				rpc.addProperty("vehicleNo", vehicleNo);
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
				obj = new EscapeBean4Wsdl();
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
							s = s.substring(0, ii).replace("T", " ");
							f.set(obj, sdf.parse(s));
						}
					}
				}
				obj.setFlgflg(1);
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
