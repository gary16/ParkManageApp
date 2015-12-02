package com.zoway.parkmanage.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class BitmapHandle {

	Bitmap bOgi = null;
	int[] bOgiPixs = null;

	public void setSrcBitmap(Bitmap bSrc) {
		bOgi = bSrc;
		bOgiPixs = new int[bSrc.getWidth() * bSrc.getHeight()];
		bSrc.getPixels(bOgiPixs, 0, bSrc.getWidth(), 0, 0, bSrc.getWidth(),
				bSrc.getHeight());
	}

	public static Bitmap getReduceBitmap(InputStream is, boolean decodebounds,
			int sampleSize, int roation) {
		Bitmap bitmap = null;
		Bitmap rotate_bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = decodebounds;
			options.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(is, null, options);
			Matrix matrix = new Matrix();
			matrix.preRotate(roation);
			rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// roation=0 为输出bitmap原图
			if (roation % 360 != 0) {
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
					System.gc();
				}
			}
		}
		return rotate_bitmap;
	}

	public static Bitmap getRectBitmap(Bitmap bSrc, float wr, float hr) {
		int hSrc = bSrc.getHeight();
		int wSrc = bSrc.getWidth();
		int h1 = hSrc / 2;
		int w1 = wSrc / 2;
		int h2 = (int) (h1 * hr);
		int w2 = (int) (w1 * wr);
		int h3 = h1 - h2;
		int w3 = w1 - w2;
		Bitmap dst = Bitmap.createBitmap(bSrc, w3, h3, w2 * 2, h2 * 2);
		Log.d("...", w3 + "," + h3 + "," + w2 * 2 + "," + h2 * 2);
		return dst;
	}

	public static void writeJpgFromBytes(String filePath, int wid, int hei,
			byte[] data, int ysb) {
		File f1 = new File(filePath);
		try {
			if (f1.exists()) {
				f1.delete();
			}
			f1.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f1));
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			Bitmap a = BitmapFactory
					.decodeByteArray(data, 0, data.length, opts);
			a.compress(Bitmap.CompressFormat.JPEG, ysb, bos);
			bos.flush();
			bos.close();
			a.recycle();
			a = null;
			System.gc();
		} catch (Exception er) {
			er.printStackTrace();
		}
	}

	public static void writeJpgFromBytes2(String filePath, int wid, int hei,
			byte[] data, int ysb) {
		File f1 = new File(filePath);
		int h1 = hei / 2;
		int w1 = wid / 2;
		int w2 = (int) (h1 * 0.15);
		int h2 = (int) (w1 * 0.625);
		int h = h1 - h2;
		int w = w1 - w2;
		int stoke = 5;
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f1));
			int lnt = data.length;
			BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(data, 0,
					lnt, true);

			BitmapFactory.Options opts = new Options();
			Bitmap a = brd.decodeRegion(new Rect(w - stoke, h - stoke, w + w2
					* 2 + stoke, h + h2 * 2 + stoke), opts);
			a.compress(Bitmap.CompressFormat.JPEG, ysb, bos);
			bos.flush();
			bos.close();
			brd.recycle();
			brd = null;
			a.recycle();
			a = null;
			System.gc();
		} catch (Exception er) {
			er.printStackTrace();
		}
	}

	public static void writeOcrJpgFromBytes(String filePath, int wid, int hei,
			byte[] data, int ysb) {
		File f1 = new File(filePath);
		Bitmap bitmap = null;
		Bitmap rotate_bitmap = null;
		Bitmap cut_bitmap = null;
		try {
			if (f1.exists()) {
				f1.delete();
			}
			f1.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f1));
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);

			Matrix matrix = new Matrix();
			matrix.preRotate(90);
			rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			float widthScale;
			float heightScale;

			if (rotate_bitmap.getWidth() > 2048
					&& rotate_bitmap.getHeight() > 1536) {
				matrix = new Matrix();
				widthScale = (float) 2048 / rotate_bitmap.getWidth();
				heightScale = (float) 1536 / rotate_bitmap.getHeight();
				matrix.postScale(widthScale, heightScale);
				cut_bitmap = Bitmap.createBitmap(rotate_bitmap, 0, 0, 2048,
						1536, matrix, true);
			} else if (rotate_bitmap.getWidth() > 2048
					&& rotate_bitmap.getHeight() <= 1536) {
				matrix = new Matrix();
				widthScale = (float) 2048 / rotate_bitmap.getWidth();
				heightScale = (float) 1.0;
				matrix.postScale(widthScale, heightScale);
				cut_bitmap = Bitmap.createBitmap(rotate_bitmap, 0, 0, 2048,
						rotate_bitmap.getHeight(), matrix, true);
			} else if (rotate_bitmap.getWidth() <= 2048
					&& rotate_bitmap.getHeight() > 1536) {
				matrix = new Matrix();
				widthScale = (float) 1.0;
				heightScale = (float) 1536 / rotate_bitmap.getHeight();
				matrix.postScale(widthScale, heightScale);
				cut_bitmap = Bitmap.createBitmap(rotate_bitmap, 0, 0,
						rotate_bitmap.getWidth(), 1536, matrix, true);
			}
			cut_bitmap.compress(Bitmap.CompressFormat.JPEG, ysb, bos);
			bos.flush();
			bos.close();
		} catch (Exception er) {
			er.printStackTrace();
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			if (rotate_bitmap != null && !rotate_bitmap.isRecycled()) {
				rotate_bitmap.recycle();
				rotate_bitmap = null;
			}
			if (cut_bitmap != null && !cut_bitmap.isRecycled()) {
				cut_bitmap.recycle();
				cut_bitmap = null;
			}
			System.gc();
		}
	}

	public static void writeJpgFromBitmap(String filePath, Bitmap b, int ysb) {
		File f1 = new File(filePath);
		try {
			if (!f1.exists()) {
				f1.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f1));
			b.compress(Bitmap.CompressFormat.JPEG, ysb, bos);
			bos.flush();
			bos.close();
		} catch (Exception er) {
			er.printStackTrace();
		}
	}

	public Bitmap getGrayImg() {
		Bitmap result = null;
		try {
			int imgWidth = this.bOgi.getWidth();
			int imgHeight = this.bOgi.getHeight();
			int alpha = 0xFF << 24;
			for (int i = 0; i < imgHeight; i++) {
				for (int j = 0; j < imgWidth; j++) {
					int grey = bOgiPixs[imgWidth * i + j];

					int red = ((grey & 0x00FF0000) >> 16);
					int green = ((grey & 0x0000FF00) >> 8);
					int blue = (grey & 0x000000FF);

					grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
					grey = alpha | (grey << 16) | (grey << 8) | grey;
					bOgiPixs[imgWidth * i + j] = grey;
				}
			}
			result = Bitmap.createBitmap(imgWidth, imgHeight, Config.RGB_565);
			result.setPixels(bOgiPixs, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Bitmap getRoughEdge() {
		int imgWidth = this.bOgi.getWidth();
		int imgHeight = this.bOgi.getHeight();
		int[] res = new int[2];
		int w1 = (int) (imgWidth / 2 - imgWidth * 0.13);
		int w2 = (int) (imgWidth / 2 + imgWidth * 0.13);
		int h0 = (int) (imgHeight * 0.18);

		try {
			for (int i = h0; i < imgHeight; i++) {
				int sum = 0;
				for (int j = w1; j < w2; j++) {
					if ((this.bOgiPixs[i * imgWidth + j] & 0xff) == 0)
						sum++;
				}
				if (sum > 20) {
					res[0] = i;
					break;
				}
			}

			for (int i = imgHeight - h0; i > 0; i--) {
				int sum = 0;
				for (int j = w1; j < w2; j++) {
					if ((this.bOgiPixs[(i - 1) * imgWidth + j] & 0xff) == 0)
						sum++;
				}
				if (sum > 20) {
					res[1] = i;
					break;
				}
			}
		} catch (Exception er) {
			er.printStackTrace();
		}
		// Log.i("...", res[0] + "," + res[1]);
		Bitmap result = Bitmap.createBitmap(imgWidth, res[1] - res[0],
				Config.RGB_565);
		result.setPixels(this.bOgiPixs, res[0] * imgWidth, imgWidth, 0, 0,
				imgWidth, res[1] - res[0]);
		return result;

	}

	public Bitmap binarization(int yuzhi, boolean borw) {
		int fntColor = 255;
		int bkgColor = 0;
		if (borw) {
			fntColor = 0;
			bkgColor = 255;
		}
		int imgWidth = this.bOgi.getWidth();
		int imgHeight = this.bOgi.getHeight();
		// 用阈值T1对图像进行二值化
		for (int i = 0; i < imgHeight; i++) {
			for (int j = 0; j < imgWidth; j++) {
				int gray = this.bOgiPixs[i * imgWidth + j] & 0xff;
				if (gray < yuzhi) {
					// 小于阈值设为白色
					bOgiPixs[i * imgWidth + j] = Color.rgb(fntColor, fntColor,
							fntColor);
				} else {
					// 大于阈值设为黑色
					bOgiPixs[i * imgWidth + j] = Color.rgb(bkgColor, bkgColor,
							bkgColor);
				}
			}
		}

		Bitmap result = Bitmap
				.createBitmap(imgWidth, imgHeight, Config.RGB_565);
		result.setPixels(bOgiPixs, 0, imgWidth, 0, 0, imgWidth, imgHeight);

		return result;
	}

	// 根据分段线数量，和取值点决定最小值
	public int getLinesMinValuesAvg(int lneNum, int pntNum) {
		int imgWidth = this.bOgi.getWidth();
		int imgHeight = this.bOgi.getHeight();
		int[] minArr = new int[lneNum * pntNum];
		int lne = imgHeight / (lneNum + 1);
		for (int i = 0; i < lneNum; i++) {
			int[] eachLne = new int[pntNum];
			for (int j = 0; j < imgWidth; j++) {
				int val = this.bOgiPixs[imgWidth * i * lne + j] & 0xff;
				for (int k = 0; k < pntNum; k++) {
					if (val > eachLne[k]) {
						eachLne[k] = val;
						break;
					}
				}
			}
			System.arraycopy(eachLne, 0, minArr, i * pntNum, pntNum);
		}
		int sum = 0;
		for (int i = 0; i < minArr.length; i++) {
			sum += minArr[i];
		}
		return (int) (sum / (lneNum * pntNum));
	}

	public Bitmap preTreateImg() {
		// 灰度
		this.getGrayImg();
		// 获取二值化阈值
		int t = (int) (this.getLinesMinValuesAvg(3, 20) * 0.8);
		// 二值化
		return this.binarization(t, true);
	}

	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------

	public Bitmap[] getAllChrBitmap(Bitmap bBinar) {
		// 粗略地划分不同字符的边界
		Bitmap b1 = this.getRoughEdge();
		// 返回水平卷积数组，参数b2接收分布图片
		Bitmap b2 = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(),
				Config.RGB_565);
		int[] arr = this.getHorEachChrRanges(b1, b2, true);
		// 划分7个字符
		ArrayList<int[]> li = this.splitEachChrByHor(arr, b1.getHeight());
		// 返回7个图片
		Bitmap[] bArr = this.getAllChrBitmapBySpread(bBinar, li);
		return bArr;
	}

	public int[] getHorEachChrRanges(Bitmap b, Bitmap bmpdst, boolean borw) {
		int freColor = 0xffffffff;
		int bkgColor = 0xff000000;
		int cmp = 0;
		if (borw) {
			freColor = 0xff000000;
			bkgColor = 0xffffffff;
			cmp = 255;
		}
		int[] rangeArrs = new int[b.getWidth()];
		try {
			int imgWidth = b.getWidth();
			int imgHeight = b.getHeight();
			int[] imgPixels = new int[imgWidth * imgHeight];
			b.getPixels(imgPixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
			int[] horPix = new int[imgWidth * imgHeight];
			Arrays.fill(horPix, freColor);
			for (int i = 0; i < imgWidth; i++) {
				int k = imgHeight - 1;
				int sum = 0;
				for (int j = 0; j < imgHeight; j++) {
					int val = imgPixels[j * imgWidth + i] & 0xff;
					if (val == cmp) {
						horPix[k * imgWidth + i] = bkgColor;
						sum++;
						k--;
					}
				}
				rangeArrs[i] = sum;
			}
			bmpdst.setPixels(horPix, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return rangeArrs;
	}

	public ArrayList<int[]> splitEachChrByHor(int[] src, int hgt) {
		ArrayList<int[]> li = new ArrayList<int[]>();
		float scale1 = 0.02f;
		float scale2 = 0.05f;
		int one = (int) (src.length * scale1);
		int other = (int) (src.length * scale2);
		int start = 0;
		int end = 0;
		for (int i = 0; i < src.length; i++) {
			if (src[i] == 0) {
				if (end >= start + other) {
					li.add(new int[] { start, end });
				} else if (end >= start + one) {
					int idx = (int) (start + (end - start) * 0.5);
					if (src[idx] > 0.7 * hgt) {
						li.add(new int[] { start, end });
					}
				}
				start = i;
			} else {
				end = i;
			}
		}
		if (end == src.length) {
			li.add(new int[] { start, end });
		}
		return li;
	}

	public Bitmap getChrBitmapByVer(Bitmap b) {
		Bitmap b1 = null;
		int w = b.getWidth();
		int h = b.getHeight();
		int[] pixArr = new int[w * h];
		b.getPixels(pixArr, 0, w, 0, 0, w, h);
		int[] verSum = new int[h];
		try {
			for (int i = 0; i < h; i++) {
				int sum = 0;
				for (int j = 0; j < w; j++) {
					int val = pixArr[i * w + j] & 0xff;
					if (val == 0) {
						sum++;
					}
				}

				verSum[i] = sum;
			}
			int start = 0;
			int yongyu = (int) (h * 0.2);
			int end = 0;
			int max = 0;
			int s1 = 0;
			for (int i = 0; i < verSum.length; i++) {
				if (verSum[i] == 0) {
					if (end >= start + yongyu) {
						int temp = end - start;
						if (temp > max) {
							max = temp;
							s1 = start;
						}
					}
					start = i;
				} else {
					end = i;
				}
			}
			b1 = Bitmap.createBitmap(b, 0, s1, w, max);
			b1 = Bitmap.createScaledBitmap(b1, 16, 32, true);
		} catch (Exception er) {
			er.printStackTrace();
		}

		return b1;
	}

	public Bitmap getChrBitmapByVerOptions(Bitmap b, int borw, int[] options,
			boolean useOptions) {
		Bitmap b1 = null;
		int w = b.getWidth();
		int h = b.getHeight();
		int[] pixArr = new int[w * h];
		b.getPixels(pixArr, 0, w, 0, 0, w, h);
		int[] verSum = new int[h];

		for (int i = 0; i < h; i++) {
			int sum = 0;
			for (int j = 0; j < w; j++) {
				int val = pixArr[i * w + j] & 0xff;
				if (val == borw) {
					sum++;
				}
			}

			verSum[i] = sum;
		}
		int start = 0;
		int yongyu = (int) (h * 0.2);
		int end = 0;
		int max = 0;
		int s1 = 0;
		for (int i = 0; i < verSum.length; i++) {
			if (verSum[i] == 0) {
				if (end >= start + yongyu) {
					int temp = end - start;
					if (temp > max) {
						max = temp;
						s1 = start;
					}
				}
				start = i;
			} else {
				end = i;
			}
		}
		// use options
		if (options.length > 1) {
			if (!useOptions) {
				options[0] = s1;
				options[1] = max;
			} else {
				int temps1 = s1;
				// 去除上园
				if (s1 < options[0] * 0.8) {
					s1 = options[0];
				}
				// 去除下圆
				if (max < options[1] * 1.1) {
					max = temps1 + max - s1;
				} else {
					max = options[1];
				}

			}
		}
		// Log.i("	LoadPhotoActivity", s1 + "," + max);
		b1 = Bitmap.createBitmap(b, 0, s1, w, max);
		return b1;
	}

	public Bitmap[] getAllChrBitmapBySpread(Bitmap bSrc, ArrayList<int[]> li) {

		if (li.size() < 4) {
			return null;
		} else {
			Bitmap[] bArr = new Bitmap[li.size()];

			try {
				int idx = (li.size() - 1) / 2;
				int[] opt = new int[2];
				// Log.i("	LoadPhotoActivity", idx + "");
				int aa[] = li.get(idx);
				Bitmap tt = Bitmap.createBitmap(bSrc, aa[0], 0, aa[1] - aa[0],
						bSrc.getHeight());
				bArr[idx] = this.getChrBitmapByVerOptions(tt, 255, opt, false);

				for (int i = 0; i < idx; i++) {
					aa = li.get(i);
					tt = Bitmap.createBitmap(bSrc, aa[0], 0, aa[1] - aa[0],
							bSrc.getHeight());
					bArr[i] = this.getChrBitmapByVerOptions(tt, 255, opt, true);
				}

				for (int j = idx + 1; j < li.size(); j++) {
					aa = li.get(j);
					tt = Bitmap.createBitmap(bSrc, aa[0], 0, aa[1] - aa[0],
							bSrc.getHeight());
					bArr[j] = this.getChrBitmapByVerOptions(tt, 255, opt, true);
				}
			} catch (Exception er) {
				er.printStackTrace();
			}
			return bArr;
		}
	}

	public float[] count10RangesVariance(int[] srcArr) {
		float[] arr = new float[10];
		if (srcArr.length < 80) {
		} else {
			int pec = 10;
			int wth = (int) (srcArr.length / pec);
			for (int i = 0; i < 10; i++) {
				int sta = i * wth;
				int sum = 0;
				for (int j = sta; j < sta + wth; j++) {
					sum += srcArr[j];
				}
				float avg = sum / wth;
				float varsum = 0;
				for (int j = sta; j < sta + wth; j++) {
					varsum += ((avg - srcArr[j]) * (avg - srcArr[j]));
				}
				float vari = varsum / wth;
				arr[i] = varsum;
			}
		}
		return arr;
	}

	public float[] count10AreasScale(int[] srcArr) {
		float[] arr = new float[10];
		if (srcArr.length < 80) {
		} else {

			int sumall = 0;
			for (int k = 0; k < srcArr.length; k++) {
				sumall += srcArr[k];
			}
			int pec = 10;
			int wth = (int) (srcArr.length / pec);
			for (int i = 0; i < 10; i++) {
				int sta = i * wth;
				int sum = 0;
				for (int j = sta; j < (sta + wth); j++) {
					sum += srcArr[j];
				}
				float avg = (float) sum / (float) sumall;
				BigDecimal b = new BigDecimal(avg);
				// Log.i("LoadPhotoActivity", String.format("%d,%d", sum,
				// sumall));
				arr[i] = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
			}
		}
		return arr;
	}

}
