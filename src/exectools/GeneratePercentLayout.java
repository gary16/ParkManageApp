package exectools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class GeneratePercentLayout {

	public static final int baseH = 480;
	public static final int baseW = 320;

	public void generateLayout(int h, int w) {

		if (h < 480 || w < 320) {
		} else {

			String ppath = System.getProperty("user.dir") + "/res/values-" + h
					+ "x" + w;
			File f = new File(ppath);
			BufferedWriter bw = null;
			try {
				if (!f.exists()) {
					f.mkdir();
				}
				String fpath = ppath + "/lyfit.xml";
				File f1 = new File(fpath);
				if (!f1.exists()) {
					f1.createNewFile();
				} else {
					f1.delete();
				}
				bw = new BufferedWriter(new FileWriter(f1));
				bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				bw.newLine();
				bw.write("<resources>");
				bw.newLine();
				float wPer = (float) w / baseW;
				float hPer = (float) h / baseH;
				for (int i = 1; i < baseW + 1; i++) {
					float fv = wPer * i;
					bw.write("<dimen name=\"x" + i + "\">"
							+ String.format("%.2f", fv) + "px</dimen>");
					bw.newLine();
				}
				for (int i = 1; i < baseH + 1; i++) {
					float fv = hPer * i;
					bw.write("<dimen name=\"y" + i + "\">"
							+ String.format("%.2f", fv) + "px</dimen>");
					bw.newLine();
				}
				bw.write("</resources>");
				bw.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public String getResPath() {
		String path = System.getProperty("user.dir") + "/res/values/style.xml";
		return path;
	}
}
