package com.zoway.parkmanage.image;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Bmp1BitsBytesProcuder {

	public byte[] create352pixQRImage(String url) {
		return create352pixQRImage(url, 352, 392);
	}

	public byte[] create352pixQRImage(String url, int wid, int hei) {
		int allSum = (wid * hei / 8) + 62;
		int diff = hei - wid;
		int qr_width = wid;
		int qr_height = hei - diff;
		byte[] bmp = new byte[allSum];
		this.assemble1bitBmpHeader(bmp);
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, qr_width, qr_height, hints);
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			int idx = 61;
			int yushu = 0;
			for (int y = 0; y < diff; y++) {
				for (int x = 0; x < qr_width; x++) {
					if ((yushu = x % 8) == 0) {
						bmp[++idx] = 0;
					}
				}
			}
			yushu = 0;
			for (int y = qr_height - 1; y > 0; y--) {
				for (int x = 0; x < qr_width; x++) {
					// true black false white
					byte pixel = (byte) (bitMatrix.get(x, y) ? 1 : 0);
					if ((yushu = x % 8) == 0) {
						bmp[++idx] |= (pixel << 7);
					} else {
						bmp[idx] |= (pixel << (7 - yushu));
					}
				}
			}

		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public void assemble1bitBmpHeader(byte[] bmp) {
		// 写头文件
		// bm 0-1
		bmp[0] = (byte) 0x42;
		bmp[1] = (byte) 0x4d;
		// size 2-5
		bmp[2] = (byte) 0xbe;
		bmp[3] = (byte) 0x3c;
		bmp[4] = (byte) 0x0;
		bmp[5] = (byte) 0x0;
		// usignedshort bfReserved1 6-7
		bmp[6] = (byte) 0x0;
		bmp[7] = (byte) 0x0;
		// usignedshort bfReserved2 8-9
		bmp[8] = (byte) 0x0;
		bmp[9] = (byte) 0x0;
		// off index 10-13
		bmp[10] = (byte) 0x3e;
		bmp[11] = (byte) 0x0;
		bmp[12] = (byte) 0x0;
		bmp[13] = (byte) 0x0;
		// header size 14-17
		bmp[14] = (byte) 0x28;
		bmp[15] = (byte) 0x0;
		bmp[16] = (byte) 0x0;
		bmp[17] = (byte) 0x0;
		// 　 image_width 18-21
		bmp[18] = (byte) 0x60;
		bmp[19] = (byte) 0x01;
		bmp[20] = (byte) 0x0;
		bmp[21] = (byte) 0x0;
		// image_heigh 22-25 　
		bmp[22] = (byte) 0x60;
		bmp[23] = (byte) 0x01;
		bmp[24] = (byte) 0x0;
		bmp[25] = (byte) 0x0;
		// target level
		bmp[26] = (byte) 0x01;
		bmp[27] = (byte) 0x0;
		// bit count
		bmp[28] = (byte) 0x01;
		bmp[29] = (byte) 0x0;
		// biCompression
		bmp[30] = (byte) 0x0;
		bmp[31] = (byte) 0x0;
		bmp[32] = (byte) 0x0;
		bmp[33] = (byte) 0x0;
		// bitmap data size
		bmp[34] = (byte) 0x80;
		bmp[35] = (byte) 0x3c;
		bmp[36] = (byte) 0x0;
		bmp[37] = (byte) 0x0;
		// biXPelsPerMeter
		bmp[38] = (byte) 0x80;
		bmp[39] = (byte) 0x3c;
		bmp[40] = (byte) 0x0;
		bmp[41] = (byte) 0x0;
		// biYPelsPerMeter
		bmp[42] = (byte) 0x80;
		bmp[43] = (byte) 0x3c;
		bmp[44] = (byte) 0x0;
		bmp[45] = (byte) 0x0;
		// biClrUsed
		bmp[46] = (byte) 0x0;
		bmp[47] = (byte) 0x0;
		bmp[48] = (byte) 0x0;
		bmp[49] = (byte) 0x0;
		// biClrImportant
		bmp[50] = (byte) 0x0;
		bmp[51] = (byte) 0x0;
		bmp[52] = (byte) 0x0;
		bmp[53] = (byte) 0x0;

		// 颜色表
		// black
		bmp[54] = (byte) 0x0;
		bmp[55] = (byte) 0x0;
		bmp[56] = (byte) 0x0;
		bmp[57] = (byte) 0x0;
		// white
		bmp[58] = (byte) 0xff;
		bmp[59] = (byte) 0xff;
		bmp[60] = (byte) 0xff;
		bmp[61] = (byte) 0x0;
	}
}
