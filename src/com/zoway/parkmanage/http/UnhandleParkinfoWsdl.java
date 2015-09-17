package com.zoway.parkmanage.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.XPath;
import org.dom4j.tree.DefaultElement;

public class UnhandleParkinfoWsdl {

	private final String URL = BaseUrl.BASETERMINATEURL
			+ "?op=GetUnhandledParkRecords";

	// sectionid µØ¶ÎId
	private String assemblePostTxt(int sectionId, int pageSize, int pageIndex) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb1.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb1.append("<soap:Body>");
		sb1.append("<GetUnhandledParkRecords xmlns=\"http://tempuri.org/\">");
		sb1.append("<sectionId>%d</sectionId>");
		sb1.append("<pageSize>%d</pageSize>");
		sb1.append("<pageIndex>%d</pageIndex>");
		sb1.append("</GetUnhandledParkRecords>");
		sb1.append("</soap:Body>");
		sb1.append("</soap:Envelope>");
		String s = String
				.format(sb1.toString(), sectionId, pageSize, pageIndex);
		return s;
	}

	public ArrayList<String[]> getUnhandleList(int sectionId, int pageSize,
			int pageIndex) {

		DefaultHttpClient dhc = new DefaultHttpClient();
		String s = this.assemblePostTxt(sectionId, pageSize, pageIndex);
		ArrayList<String[]> al = null;
		try {
			HttpPost hp = new HttpPost(URL);

			HttpEntity param = new StringEntity(s, HTTP.UTF_8);
			hp.setHeader("Content-Type", "text/xml; charset=utf-8");
			hp.setEntity(param);
			HttpResponse hrp = dhc.execute(hp);
			HttpEntity res = hrp.getEntity();
			String s1 = EntityUtils.toString(res);
			al = this.parseStr(s1);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dhc.getConnectionManager().shutdown();
		return al;
	}

	public ArrayList<String[]> parseStr(String s) {
		ArrayList<String[]> al = new ArrayList<String[]>();
		try {
			Document dom = DocumentHelper.parseText(s);
			Map<String, String> m = new HashMap<String, String>();
			m.put("ns", "http://tempuri.org/");
			XPath xpa = dom.createXPath("//ns:ParkRecord");
			xpa.setNamespaceURIs(m);
			List<DefaultElement> l = xpa.selectNodes(dom);
			for (int i = 0; i < l.size(); i++) {
				DefaultElement e1 = (DefaultElement) l.get(i);
				e1.add(new Namespace("ns", "http://tempuri.org/"));
				String rid = e1.selectSingleNode("//ns:RecordId").getText();
				String rno = e1.selectSingleNode("//ns:RecordNo").getText();
				String sid = e1.selectSingleNode("//ns:SeatId").getText();
				String sno = e1.selectSingleNode("//ns:SeatNo").getText();
				String rt = e1.selectSingleNode("//ns:ReachTime").getText();
				String[] sA = new String[] { rid, rno, sid, sno, rt };
				al.add(sA);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al;
	}

}
